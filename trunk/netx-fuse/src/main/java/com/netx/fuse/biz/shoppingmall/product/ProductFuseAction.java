package com.netx.fuse.biz.shoppingmall.product;

import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.ProductSearchResponse;
import com.netx.searchengine.query.ProductSearchQuery;
import com.netx.shopping.biz.business.RedpacketSendAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.biz.product.*;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.order.ProductOrderItem;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.model.product.ProductFavorite;
import com.netx.shopping.model.product.ProductSpe;
import com.netx.shopping.service.business.*;
import com.netx.shopping.service.order.ProductOrderItemService;
import com.netx.shopping.service.product.*;
import com.netx.shopping.vo.GetProductListResponseVo;
import com.netx.shopping.vo.GetProductResponseVo;
import com.netx.shopping.vo.ProductCategoryVo;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class ProductFuseAction {

    private Logger logger = LoggerFactory.getLogger(ProductAction.class);

    @Autowired
    ProductAction productAction;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductFavoriteService productFavoriteService;

    @Autowired
    RedpacketSendAction redpacketSendAction;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    SearchServiceProvider searchServiceProvider;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    ProductOrderItemService productOrderItemService;

    @Autowired
    ProductCollectService productCollectService;

    @Autowired
    ProductCategoryAction productCategoryAction;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ProductSpeAction productSpeAction;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    PacketSetService packetSetService;

    @Autowired
    UserAction userAction;

    @Autowired
    ProductSpeService productSpeService;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    public GetProductResponseVo createGetGoodsResponseVo(Product product, String goodsOrderItemSpecId){
        productAction.updateImages(product);
        GetProductResponseVo getGoodsResponseVo = VoPoConverter.copyProperties(product,GetProductResponseVo.class);

//        ProductCategoryVo productCategoryVo = productCategoryAction.getProductCategoryVo(product.getId());
//        getGoodsResponseVo.setCategory(productCategoryVo.getCategory());
//        getGoodsResponseVo.setTages(productCategoryVo.getTages());
//        getGoodsResponseVo.setGoodsDealAmount(productAction.getGoodsDealAmount(product.getId()));//成交额

//        try {
//            getGoodsResponseVo.setPackageList(productPackageAction.getGoodsPackageListByIds(product.getPackageId()));//包装明细
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//        }

//        if(product.getSpecId()!=null){
//            if (goodsOrderItemSpecId!=null){
//                try {
//                    getGoodsResponseVo.setSpecList(productSpecAction.getSpecListBySpecItemId(goodsOrderItemSpecId));
//                }catch (Exception e){
//                    logger.error(e.getMessage(),e);
//                }
//            }else {
//                try {
//                    getGoodsResponseVo.setSpecList(productSpecAction.getSpecListByIds(product.getSpecId()));
//                }catch (Exception e){
//                    logger.error(e.getMessage(),e);
//                }
//            }
//        }else {
//            List<ProductSpecResponseVo> list=new ArrayList<>();
//            getGoodsResponseVo.setSpecList(list);
//        }

        getGoodsResponseVo.setSpeList(productSpeAction.getProductSpeList(product.getId()));

        if(product.getSellerId()!=null){
            Seller seller = sellerService.selectById(product.getSellerId());
            if(seller==null){
                getGoodsResponseVo.setSellerName("");
            }else {
                getGoodsResponseVo.setSellerName(seller.getName() == null ? "" : seller.getName());
                getGoodsResponseVo.setSellerUserId(seller.getUserId());
                getGoodsResponseVo.setSellerImagesUrl(addImgUrlPreUtil.getProductImgPre(seller.getSellerImagesUrl()));
            }
        }

        //返回今日红包总额
        BigDecimal dto= redpacketSendAction.getLastDayAmount();
        getGoodsResponseVo.setPacketPoolAmount(dto==null?new BigDecimal(0):dto);

        //根据userId查询用户昵称、等级、性别
        if(product.getUserId()!=null){
            List<String> userIds = new ArrayList<>();
            userIds.add(product.getUserId());
            Map<String, UserSynopsisData> userSynopsisDataMap = userClientProxy.selectUserMapByIds(userIds);

            UserSynopsisData userSynopsisData = userSynopsisDataMap.get(product.getUserId());
            if(userSynopsisData!=null){
                getGoodsResponseVo.setNickname(userSynopsisData.getNickName());
                getGoodsResponseVo.setLv(Integer.parseInt(userSynopsisData.getLv()));
                getGoodsResponseVo.setSex(userSynopsisData.getSex());
                getGoodsResponseVo.setAge(userSynopsisData.getCredit());
            }
        }
        //返回商家相关人员userId 供前端判断是否有权限操作
        try {
            List<String> sellerUserIds = sellerFuseAction.getSellerAllUserId(product.getSellerId());
            if (sellerUserIds != null && sellerUserIds.size() >0){
                getGoodsResponseVo.setSellerUserId(sellerUserIds.get(0));
                getGoodsResponseVo.setSellerManageUserId(sellerUserIds.get(1));
                getGoodsResponseVo.setSellerMoneyUserId(sellerUserIds.get(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getGoodsResponseVo;
    }

    public List<GetProductListResponseVo> list(GetGoodsListRequestDto request, double lat, double lon) throws Exception {
        //TODO 构造商品搜索条件
        ProductSearchQuery searchQuery = new ProductSearchQuery();
        VoPoConverter.copyProperties(request, searchQuery);
        searchQuery.setCenterGeoPoint(new GeoPoint(lat,lon));
        searchQuery.setPage(request.getCurrent(),request.getSize());
        if(request.getLength()>0) {
            searchQuery.setMaxDistance(request.getLength());
        }
        /**
         * 排序需求
         * 1.综合：销量>在线>距离>信用
         * 2.销量：销量>距离>在线>信用
         * 3.价格：价格>在线>距离>信用
         * 4.支持网信：网信>在线>距离>信用
         */
        if (request.getSort() == 1) {
            searchQuery.addFristAscQueries(new LastAscQuery("volume",false));
            searchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        }else if (request.getSort() == 2) {
            searchQuery.addFristAscQueries(new LastAscQuery("volume", false));
            searchQuery.addLastAscQuery(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        } else if (request.getSort() == 3) {
            searchQuery.addFristAscQueries(new LastAscQuery("price", true));
            searchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        }else if(request.getSort()==4){
            searchQuery.setHoldCredit(true);
            searchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        }

        //搜索
        List<ProductSearchResponse> searchResponses = searchServiceProvider.getProductSearchService().queryProducts(searchQuery);
        List<GetProductListResponseVo> resultList = new ArrayList<>();
        for (int i=0;searchResponses.size()>i;i++) {
            //這裡只有上架的商品才可以查出來
            if (!searchResponses.get(i).getOnlineStatus()){
                resultList.add(createGetProductListResponseVo(searchResponses.get(i)));
            }

        }


        /*for (ProductSearchResponse searchResponse: searchResponses) {
            Product product = new Product();
            product.setId(searchResponse.getId());
            product.setName(searchResponse.getName());
//          product.setCategoryId(searchResponse.getCategoryId());
            GetProductResponseVo getGoodsResponseVo = createGetGoodsResponseVo(product,null);

            //获取商家上下架操作者昵称
            if(product.getHandlersId()!=null)
            {
                List<SelectFieldEnum> selectFieldEnumList=new ArrayList<>();//设置查询返回结果
                selectFieldEnumList.add(SelectFieldEnum.NICKNAME);
                UserInfoResponseDto userInfoResponseDto = userClientProxy.selectUserInfo(new UserInfoRequestDto(product.getHandlersId(), SelectConditionEnum.USER_ID,selectFieldEnumList));//跨域查询
                getGoodsResponseVo.setHandlersNikname(userInfoResponseDto==null?null:userInfoResponseDto.getNickname());
            }

            resultList.add(getGoodsResponseVo);
        }*/
        return resultList;
    }

    private GetProductListResponseVo createGetProductListResponseVo(ProductSearchResponse searchResponse){
        GetProductListResponseVo getProductListResponseVo = new GetProductListResponseVo();
        VoPoConverter.copyProperties(searchResponse, getProductListResponseVo);
        getProductListResponseVo.setPrice(new BigDecimal(Money.getMoneyString(searchResponse.getPrice())));
        List<String> productImagesUrl = searchResponse.getProductImagesUrl();
        if(productImagesUrl != null && productImagesUrl.size()>0){
            getProductListResponseVo.setProductImagesUrl(productAction.updateImnagesUrl(productImagesUrl.get(0)));
        }
        List<String> categoryNames = searchResponse.getCategoryNames();
        List<String> categoryPids = searchResponse.getCategoryParentIds();
        List<String> tages = new ArrayList<>();
        List<String> categorys = new ArrayList<>();
        if(categoryNames != null && categoryPids != null && categoryNames.size() > 0 && categoryPids.size() > 0){
            for(int i=0; i<categoryPids.size(); i++){
                if("0".equals(categoryPids.get(i))){
                    tages.add(categoryNames.get(i));
                }else{
                    categorys.add(categoryNames.get(i));
                }
            }
        }
        getProductListResponseVo.setTages(new ArrayList(new HashSet(tages)));
        getProductListResponseVo.setCategories(new ArrayList(new HashSet(categorys)));
        return getProductListResponseVo;
    }

    public List<GetProductResponseVo> goodsList(BackManageGoodsListRequestDto request){
        List<GetProductResponseVo> resultList = new ArrayList<>();
        List<Product> goodList = productService.getProductByName(request);
        for (Product product : goodList) {
            GetProductResponseVo getGoodsResponseVo = createGetGoodsResponseVo(product,null);

            //获取商家上下架操作者昵称
            if(product.getHandlersId()!=null)
            {
                List<SelectFieldEnum> selectFieldEnumList=new ArrayList<>();//设置查询返回结果
                selectFieldEnumList.add(SelectFieldEnum.NICKNAME);
                UserInfoResponseDto userInfoResponseDto = userClientProxy.selectUserInfo(new UserInfoRequestDto(product.getHandlersId(),SelectConditionEnum.USER_ID,selectFieldEnumList));//跨域查询
                getGoodsResponseVo.setHandlersNikname(userInfoResponseDto==null?null:userInfoResponseDto.getNickname());
            }

            resultList.add(getGoodsResponseVo);
        }
        return resultList;
    }

    public GetProductListResponseVo newgetMessage(String userId){
        User user = userAction.getUserService().selectById(userId);
        if (user != null){
            double lat = user.getLat().doubleValue();
            double lon = user.getLon().doubleValue();

            GetListByUserIdDto getListByUserIdDto = new GetListByUserIdDto();
            getListByUserIdDto.setUserId(userId);
            getListByUserIdDto.setCurrent(1);
            getListByUserIdDto.setSize(1);
            getListByUserIdDto.setStatus(1);

            List<GetProductListResponseVo> list = getProductListByUserId(getListByUserIdDto,lat,lon);
            if (list != null && list.size() >0){
                return list.get(0);
            }
        }
        return new GetProductListResponseVo();
    }

    public GetNewestGoodsMessageResponseVo getMessage(String userId){
        GetNewestGoodsMessageResponseVo result=new GetNewestGoodsMessageResponseVo();
        Product product = productService.getLatestProduct(userId);
        if(product != null){
            productAction.updateImages(product);
            result= VoPoConverter.copyProperties(product,GetNewestGoodsMessageResponseVo.class);
            //返回商品规格最低价格
            long price = productSpeService.getMinPrice(product.getId());
            result.setPrice(new BigDecimal(Money.getMoneyString(price)));
            //result.setCategoryName(productCategoryAction.getCategoryName(product.getId())==null?null:productCategoryAction.getCategoryName(product.getId()));
            //result.setGoodsDealAmount(productAction.getGoodsDealAmount(product.getId())==null?null:productAction.getGoodsDealAmount(product.getId()));//成交额
            //result.setTagesName(productCategoryAction.getTagesName(product.getId())==null?null:productCategoryAction.getTagesName(product.getId()));
            //result.setSellerName(sellerService.selectById(product.getSellerId())==null?null: sellerService.selectById(product.getSellerId()).getName());
            if(product.getSellerId()!=null){
                Seller seller = sellerService.selectById(product.getSellerId());
                result.setSellerName(seller==null?"":seller.getName()==null?"":seller.getName());
                if(seller.getVerifyPhone()!=null){
                    //根据商家Id查询用户信用值
                    List<SelectFieldEnum> selectFieldEnumList=new ArrayList<>();//设置查询返回结果
                    selectFieldEnumList.add(SelectFieldEnum.CREDIT);
                    UserInfoResponseDto userInfoResponseDto=userClientProxy.selectUserInfo(new UserInfoRequestDto(seller.getVerifyPhone(),SelectConditionEnum.MOBILE,selectFieldEnumList));//跨域查询
                    result.setCredit(userInfoResponseDto==null?null:userInfoResponseDto.getCredit());
                }

            }else{
                result.setSellerName("");
            }

            //今日下单量
            result.setTodayOderQuantity(productOrderAction.oneDayOrderQuantity(product.getId()));

            //TODO 红包
        }
        return result;
    }

    public List<GetProductResponseVo> getGoodsListByOrderId(String orderId){
        List<GetProductResponseVo> resultList = new ArrayList<>();

        List<ProductOrderItem> productOrderItemList = productOrderItemService.getProductOrderItemListByOrderId(orderId);
        if (productOrderItemList ==null){
            return resultList;
        }

        for (ProductOrderItem productOrderItem : productOrderItemList) {
            Product product =productService.selectById(productOrderItem.getProductId());
            GetProductResponseVo getGoodsResponseVo = createGetGoodsResponseVo(product, productOrderItem.getSpeId());

            //获取商家的经纬度
            Seller seller= sellerService.selectById(product.getSellerId());
            getGoodsResponseVo.setLat(seller.getLat());
            getGoodsResponseVo.setLon(seller.getLon());
            resultList.add(getGoodsResponseVo);
        }
        return resultList;
    }

    public GetProductResponseVo get(GetGoodsRequestDto request){

        Product product = productService.selectById(request.getId());
        if(product ==null) return new GetProductResponseVo();
        GetProductResponseVo getGoodsResponseVo = createGetGoodsResponseVo(product,null);
        //商品成交次数
        getGoodsResponseVo.setOderSumQuantity(productAction.getGoodsOrderItemNum(product.getId()));
        ProductFavorite productCollect =new ProductFavorite();
        productCollect.setProductId(request.getId());
        productCollect.setUserId(request.getUserId());
        if(null!= productCollectService.isHaveCollect(productCollect)){
            getGoodsResponseVo.setIsHaveCollect(1);
        }else {
            getGoodsResponseVo.setIsHaveCollect(2);
        }
        //修改访问
        Product updateProduct =new Product();
        updateProduct.setId(product.getId());
        updateProduct.setVisitNum(product.getVisitNum()==null?0: product.getVisitNum()+1);
        productService.updateById(updateProduct);

        return getGoodsResponseVo;
    }


    public List<GetProductResponseVo> searchGoods(SearchGoodsRequestDto requestDto){

        List<Product> list=productService.getProductsByStatusAndName(requestDto.getCondition(),requestDto.getSellerName());
        List<GetProductResponseVo> res=new ArrayList<>();
        for (Product product :list){
            GetGoodsRequestDto dto=new GetGoodsRequestDto();
            dto.setId(product.getId());
            res.add(this.get(dto));
        }
        return res;
    }

    public List<GetProductListResponseVo> getProductListByUserId(GetListByUserIdDto getListByUserIdDto,double lat,double lon){
        List<GetProductListResponseVo> list = new ArrayList<>();
        List<String> productIds = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        if(getListByUserIdDto.getStatus() == 1){
            products = productService.selectProductListByUserId(getListByUserIdDto.getUserId(), getListByUserIdDto.getCurrent(), getListByUserIdDto.getSize());
        }else if(getListByUserIdDto.getStatus() == 2){
            productIds = productFavoriteService.getProductIdByUserId(getListByUserIdDto.getUserId());
            if(productIds != null && productIds.size()>0) {
                products = productService.selectProductListByProdectId(productIds, getListByUserIdDto.getCurrent(), getListByUserIdDto.getSize());
            }
        }else{
            return list;
        }
        if(products != null && products.size()>0){
            for (Product product : products) {
                list.add(createProduct(product, lat, lon));
            }
        }
        return list;
    }

    public GetProductListResponseVo createProduct(Product product, double lat, double lon){
        GetProductListResponseVo getProductListResponseVo = new GetProductListResponseVo();
        productAction.updateImages(product);
        VoPoConverter.copyProperties(product,getProductListResponseVo);
        //返回商品规格最低价格
        long price = productSpeService.getMinPrice(product.getId());
        getProductListResponseVo.setPrice(new BigDecimal(Money.getMoneyString(price)));

        List<String> categoryIds = productCategoryService.getCategoryIdByProductId(product.getId());
//        if(categoryIds!=null && categoryIds.size()>0){
//            List<String> categories = categoryService.getTagesByProductId(categoryIds,false);
//            getProductListResponseVo.setCategories(new ArrayList(new HashSet(categories)));
//            List<String> tages = categoryService.getTagesByProductId(categoryIds,true);
//            if(tages == null || tages.size()<=0){
//                List<String> pids = categoryService.getPidByCategoryIds(categoryIds);
//                if(pids != null && pids.size()>0) {
//                    tages = categoryService.getPidById(pids);
//                }
//            }
//            getProductListResponseVo.setTages(new ArrayList(new HashSet(tages)));
//        }
        Integer credit = userService.getCreditByUserId(product.getUserId());
        getProductListResponseVo.setCredit(credit);
        BigDecimal firstRate = packetSetService.getFirstRate(product.getSellerId());
        if(firstRate != null){
            getProductListResponseVo.setFirstRate(firstRate);
        }
        Seller seller = sellerService.getSeller(product.getSellerId());
        if(seller!=null){
            getProductListResponseVo.setCityCode(seller.getCityCode());
            getProductListResponseVo.setSellerName(seller.getName());
            getProductListResponseVo.setHoldCredit(seller.getHoldCredit());
            if(seller.getPayStatus() != null) {
                getProductListResponseVo.setPayStatus(seller.getPayStatus());
            }
            if (seller.getLat() != null && seller.getLon() != null) {
                Double distance = DistrictUtil.calcDistance(lat, lon, seller.getLat().doubleValue(), seller.getLon().doubleValue());
                //保留小数点后两位小数
                if(distance!=null){
                    getProductListResponseVo.setDistance(new BigDecimal(distance).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }else{
                    getProductListResponseVo.setDistance(999.99);
                }
            }
        }
        return getProductListResponseVo;
    }

    public List<GetProductListResponseVo> getProductListBySellerId(GetListBySellerIdDto getListBySellerIdDto, double lat, double lon){
        List<GetProductListResponseVo> list = new ArrayList<>();
        List<Product> products = productService.selectBySellerId(getListBySellerIdDto);
        if(products != null && products.size()>0) {
            for (Product product : products) {
                GetProductListResponseVo getProductListResponseVo = new GetProductListResponseVo();
                getProductListResponseVo = createProduct(product, lat, lon);
                List<ProductSpe> speList = productSpeService.selectByProductId(product.getId());
                if(speList != null && speList.size() > 0){
                    getProductListResponseVo.setSpeList(speList);
                }
                list.add(getProductListResponseVo);
            }
        }
        return list;
    }
}
