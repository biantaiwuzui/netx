package com.netx.shopping.biz.business;

import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.ProductSearchResponse;
import com.netx.searchengine.query.ProductSearchQuery;
import com.netx.searchengine.query.SellerSearchQuery;
import com.netx.common.user.dto.common.CommonListByGeohashDto;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.*;
import com.netx.common.vo.currency.GetSellersBuyIdRequestDto;
import com.netx.shopping.enums.*;
import com.netx.shopping.model.business.*;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.model.product.SellerKind;
import com.netx.shopping.service.business.*;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.service.product.KindService;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.vo.*;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 网商-商家表 服务实现类
 * </p>
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("sellerAction")
@Transactional(rollbackFor = Exception.class)
public class SellerAction{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    SellerCategoryAction sellerCategoryAction;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    KindService kindService;

    @Autowired
    SearchServiceProvider searchServiceProvider;

   /* @Autowired
    GeoService geoService;*/

    @Autowired
    ProductService productService;

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerCategoryService sellerCategoryService;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    SellerCollectService sellerCollectService;

    public SellerService getSellerService() {
        return this.sellerService;
    }
    public Integer createEndTime(Date date, Integer effectiveTime) {
        return effectiveTime == 0 ? 0 : (int) DateTimestampUtil.addYearByDate(date, effectiveTime).getTime() / 1000;
    }

    public SellerSearchQuery getSellerSearchQuery(GetSellerListRequestDto request,double lat,double lon) {
        //构造商家搜索条件

        SellerSearchQuery sellerSearchQuery = new SellerSearchQuery();
        /**
         * 排序需求
         * 1.综合：销量>在线>距离>信用
         * 2.距离：距离>在线>距离>信用
         * 3.信用：信用>在线>距离>价格
         * 4.支持网信：网信>在线>距离>信用
         */
        if(request.getSort() == 1){
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("volume",false));
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 2){
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 3){
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("credit",false));
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }else if(request.getSort() == 4){
            sellerSearchQuery.setHoldCredit(true);
            sellerSearchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            sellerSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        }

        sellerSearchQuery.setCenterGeoPoint(new GeoPoint(lat,lon));
        sellerSearchQuery.setPage(request.getCurrent(),request.getSize());
        sellerSearchQuery.setCategoryId(request.getCategoryId());
        sellerSearchQuery.setProvinceCode(request.getProvinceCode());
        sellerSearchQuery.setCityCode(request.getCityCode());
        sellerSearchQuery.setAreaCode(request.getAreaCode());
        sellerSearchQuery.setMinVisitNum(request.getMinVisitNum());
        sellerSearchQuery.setMaxVisitNum(request.getMaxVisitNum());
        sellerSearchQuery.setName(request.getName());
        sellerSearchQuery.setMaxDistance(request.getLength());

        return sellerSearchQuery;
    }

    public List<GetSellersResponseDto> listSellersForQuartz(){
        List<Seller> sellers = sellerService.getSellerListHavesetSqlSelect();

        List<GetSellersResponseDto> getSellersResponseDtos = new ArrayList<>();
        for (Seller seller : sellers) {
            GetSellersResponseDto getSellersResponseDto = new GetSellersResponseDto();
            getSellersResponseDto.setId(seller.getId());
//            getSellersResponseDto.setCategoryId(seller.getCategoryId());
            //todo 系统类别

            getSellersResponseDtos.add(getSellersResponseDto);
        }

        return getSellersResponseDtos;
    }

    public boolean isHaveThisName(String userId, String name){
        if (sellerService.isHaveThisName(userId,name)!=null) {
            return true;
        }
        return false;
    }



    public List<String> getUntqueuUserId(List<String> list){
        List<String> result = new ArrayList<>();
        for (String i : list) {
            if (!result.contains(i)) {//判断是否有重复数据，如果没有就将数据装进临时集合
                result.add(i);
            }
        }
        return result;
    }

    public Seller updateImages(Seller seller) {
        if (seller.getLogoImagesUrl() != null) {
            //判断是否要加前缀
            if (!seller.getLogoImagesUrl().contains("http")) {
                seller.setLogoImagesUrl(addImgUrlPreUtil.getProductImgPre(seller.getLogoImagesUrl()));
            }
        }
        if (seller.getSellerImagesUrl() != null) {
            //判断是否要加前缀
            if (!seller.getSellerImagesUrl().contains("http")) {
                seller.setSellerImagesUrl(addImgUrlPreUtil.getProductImgPre(seller.getSellerImagesUrl()));
            }
        }
        if (seller.getCertiImagesUrl() != null) {
            //判断是否要加前缀
            if (!seller.getCertiImagesUrl().contains("http")) {
                seller.setCertiImagesUrl(addImgUrlPreUtil.getProductImgPre(seller.getCertiImagesUrl()));
            }
        }
        return seller;
    }


    public Seller getSellerAndChangeSellerImageUrl(String sellerId){
        Seller seller = sellerService.selectById(sellerId);
        seller = updateImages(seller);

        return seller;
    }

    public boolean optSeller(BackManageOptSellerRequestDto request){
        //商家下架，默认商家名下的所有商品状态也下架
        Seller seller = new Seller();
        BeanUtils.copyProperties(request, seller);
        if (request.getStatus() == SellerStatusEnum.NORMAL.getCode()) {//解除拉黑
            seller.setOverReason(request.getReason());
        } else {//拉黑
            seller.setBackReason(request.getReason());
            //获取商家对应所有商品
            productService.downSellerAllGoods(request.getId());
        }

        return sellerService.updateById(seller);
    }


    public Map<String, List> selectSellerListByDistanceAndTime(SelectSellerListByDistanceAndTimeRequestDto requestDto) {
        CommonListByGeohashDto commonListByGeohashDto = requestDto.getCommonListByGeohashDto();
        BigDecimal lon = commonListByGeohashDto.getLon();
        BigDecimal lat = commonListByGeohashDto.getLat();
        Integer length = commonListByGeohashDto.getLength();
        //距离远近查询
        List<Seller> list = sellerService.selectPage(requestDto,lon,lat,length);
        //List<Seller> list1=new ArrayList<>();
        for(Seller seller: list){
            seller = updateImages(seller);
        }
        /*for (Seller seller:list)
        {
            try {
                Seller seller1=this.getSellerAndChangeSellerImageUrl(seller.getId());
                list1.add(setImages(seller));
            } catch (ClientException e) {
                e.printStackTrace();
            }

        }*/
        //TODO 距离排序
        //List<DistrictVo> distanceList = DistrictUtil.getDistrictVoList(lat, lon, list);
        Map<String, List> map = new HashMap<>();
        //map.put("distanceList", distanceList);
        map.put("distanceList",list);//暂时逻辑
        return map;
    }

    public Integer getOrderNumBySellerId(String sellerId){
        return productOrderService.getProductOrderCountBySellerId(sellerId);
    }


    public Integer getGoodsNumBySellerId(String sellerId){
        return productService.getProductCountBySellerId(sellerId);
    }


    /**
     * 根据用户ID获取最新注册的商家 （只查询返回一条数据）
     */
    public GetSellerByUserIdVo getRegisterSellerByUserId(String userId){
        Seller registerSeller = sellerService.getSeller(userId, SellerStatusEnum.NORMAL.getCode());
        if (registerSeller != null) {
            GetSellerByUserIdVo register = this.getSeller(registerSeller.getId());
            return register;
        }
        return new GetSellerByUserIdVo();
    }

    /**
     * 根据用户ID获取他最近收藏的商家 （只查询返回一条数据）
     */
    public GetSellerByUserIdVo getCollectSellerByUserId(String userId){
        SellerFavorite sellerCollect = sellerCollectService.getSellerCollect(userId);
        if (sellerCollect != null) {
            GetSellerByUserIdVo collect = this.getSeller(sellerCollect.getSellerId());
            return collect;
        }
        return new GetSellerByUserIdVo();
    }



    /**
     * 根据商家id查询商家详情 （对应个人中心首页详情信息）
     */
    public GetSellerByUserIdVo getSeller(String sellerId){
        GetSellerByUserIdVo result = new GetSellerByUserIdVo();
        Seller seller = this.getSellerAndChangeSellerImageUrl(sellerId);//商家对象
        if (seller != null) {
            result = VoPoConverter.copyProperties(seller, GetSellerByUserIdVo.class);
            //result.setCategoryName(sellerCategoryAction.getCategoryName(sellerId)==null?null:sellerCategoryAction.getCategoryName(sellerId));
            result.setOrderNum(this.getOrderNumBySellerId(sellerId));
            result.setGoodsNum(this.getGoodsNumBySellerId(sellerId));
            result.setSumOrderAmount(productOrderAction.getSumOrderAmountBySellerId(sellerId));
        }
        return result;
    }


    public Boolean delete(DeleteSellerRequestDto request){
        //获取商家对应所有商品
        productService.downSellerAllGoods(request.getId());

        return sellerService.delete(request.getId());
    }


    public GetRegisterSellerMessageResponseVo getMessage1(String userId){
        GetRegisterSellerMessageResponseVo result = new GetRegisterSellerMessageResponseVo();
        result.setNowRegisterSeller(sellerService.getSellerCount(userId, SellerStatusEnum.NORMAL.getCode()));

        result.setSumRegisterSeller(sellerService.getSellerCountByUserId(userId));
        return result;
    }

    /**
     * 获取附近的商品
     *
     * @param
     * @return
     */
    /*public List<ProductAndProductSpecItem> getNearbyGoods(Integer pageNo, Integer pageSize, String userId) {
        UserGeo userGeo = geoService.getUserGeo(userId);
        Double userLat = userGeo.getLat();
        Double userLon = userGeo.getLon();
        GeoPoint geoPoint = new GeoPoint(userLat, userLon);

        ProductSearchQuery searchQuery = new ProductSearchQuery();
        searchQuery.setCenterGeoPoint(geoPoint);
        searchQuery.setFrom(pageNo);
        searchQuery.setPageSize(pageSize);
        return queryGoods(searchQuery);
    }*/
    private List<ProductAndProductSpecItem> queryGoods(ProductSearchQuery searchQuery) {
        List<ProductSearchResponse> searchResponses = searchServiceProvider.getProductSearchService().queryProducts(searchQuery);

        Map<String, SellerKind> kindMap = getKindMap();
//        Map<String, String> categoryMap = getCateogryMap();
        //todo 系统类别

        List<ProductAndProductSpecItem> goodsAndGoodsSpecItemList = new ArrayList<>();

        searchResponses.forEach(p -> {
            try {
                ProductAndProductSpecItem goods = new ProductAndProductSpecItem();

                goods.setId(p.getId());
                goods.setName(p.getName());
                //goods.setSpecId(String.join(",", p.getSpecIds()));
//                goods.setCategoryId(p.getCategoryId());

//                goods.setCategoryName(categoryMap.get(goods.getCategoryId()));
//                goods.setKindName(kindMap.get(goods.getKindId()).getName());

                goods.setDistance(p.getDistance());

                //goods.setProductImagesUrl(addImgUrlPreUtil.addImgUrlPres(p.getProductImagesUrl(), AliyunBucketType.ProductBucket));
//
//                List<ProductSpecResponseVo> goodsSpecResponseVos = productSpecAction.getSpecListByIds(goods.getSpecId());
//
//                goods.setSpecList(goodsSpecResponseVos);

                goodsAndGoodsSpecItemList.add(goods);
            } catch (Exception ex) {
                logger.error("构造商品数据失败", ex);
            }
        });

        return goodsAndGoodsSpecItemList;
    }

    private Map<String, SellerKind> getKindMap() {
        Map<String, SellerKind> kindMap = new HashMap<>();
        List<SellerKind> kinds = kindService.getKindlist();
        for (SellerKind kind : kinds) {
            if (!kindMap.containsKey(kind.getId())) {
                kindMap.put(kind.getId(), kind);
            }
        }
        return kindMap;
    }
/*
    public GeoPoint getGeoPoint(String userId) {
        UserGeo userGeo = geoService.getUserGeo(userId);
        Double userLat = userGeo.getLat();
        Double userLon = userGeo.getLon();
        GeoPoint geoPoint = new GeoPoint(userLat, userLon);
        return geoPoint;
    }*/

//    private Map<String, String> getCateogryMap() {
//        Map<String, String> categoryMap = new HashMap<>();
//        try {
//            List<SellerCategory> categories = sellerCategoryAction.list();
//            for (SellerCategory category : categories) {
//                if (!categoryMap.containsKey(category.getId())) {
//                    categoryMap.put(category.getId(), category.getName());
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("获取商家类目失败", ex);
//        }
//
//        return categoryMap;
//    }
    //todo 系统类别


    public List<GetSellersBuyIdResponseVo> getSellersBuyId(GetSellersBuyIdRequestDto request){
        List<GetSellersBuyIdResponseVo> responseVo = new ArrayList<>();
        List<Seller> sellers =sellerService.getSellerList(request.getIds());
        if (sellers.size() > 0) {
            for (Seller seller : sellers) {
                GetSellersBuyIdResponseVo getSellersBuyIdResponseVo = new GetSellersBuyIdResponseVo();
                //Seller seller1=this.getSellerAndChangeSellerImageUrl(seller.getId());
                seller = updateImages(seller);
                BeanUtils.copyProperties(seller, getSellersBuyIdResponseVo);
                responseVo.add(getSellersBuyIdResponseVo);
            }
            return responseVo;
        }
        return null;
    }


    public List<GetSellerByUserIdVo> getSellersByUserId(String userId){
        List<Seller> registerSeller = sellerService.getSellerList(userId, SellerStatusEnum.NORMAL.getCode());
        if (registerSeller != null) {
            List<Seller> registerSeller1 = new ArrayList<>();
            for (Seller seller : registerSeller) {
                //Seller seller1=this.getSellerAndChangeSellerImageUrl(seller.getId());
                seller = updateImages(seller);
                registerSeller1.add(seller);
            }
            return VoPoConverter.copyList(registerSeller1, GetSellerByUserIdVo.class);
        }
        return null;
    }

    public Integer getSuccessNum(String sellerId){
        String status = "6,7";
        return productOrderService.getProductOrderCountBySellerIdAndStatus(sellerId,status);
    }


    public double getDistance(Integer length) {

        //单位 公里Km
        Map<Integer, Integer> distanceMap = new HashMap<>();
        distanceMap.put(1, 1);
        distanceMap.put(2, 2);
        distanceMap.put(3, 3);
        distanceMap.put(4, 5);
        distanceMap.put(5, 10);
        distanceMap.put(6, 50);
        distanceMap.put(7, 100);
        distanceMap.put(8, 150);
        distanceMap.put(9, 200);
        if (distanceMap.containsKey(length)) {
            return distanceMap.get(length);
        }
        return 2500;
    }

    public boolean updateDayNum(){
        return sellerService.updateDayNum();
    }

    public boolean updateSellerRedpacket(){
        return sellerService.updateSellerRedpacket();

    }

    public boolean emptySellerRedpacket(){
        return sellerService.emptySellerRedpacket();
    }

    public boolean updateById(Seller seller){
        return sellerService.updateById(seller);
    }

    public List<Seller> export(){
        return sellerService.getSellerWhite();
    }
}