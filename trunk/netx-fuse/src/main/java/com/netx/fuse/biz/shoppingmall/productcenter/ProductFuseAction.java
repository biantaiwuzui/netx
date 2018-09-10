package com.netx.fuse.biz.shoppingmall.productcenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.GetGoodsListRequestDto;
import com.netx.common.vo.business.GetGoodsRequestDto;
import com.netx.common.vo.business.GetListByUserIdDto;
import com.netx.common.vo.business.GetRelatedGoodsMessageResponseVo;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.ProductSearchResponse;
import com.netx.searchengine.query.ProductSearchQuery;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.merchantcenter.MerchantPictureAction;
import com.netx.shopping.biz.productcenter.*;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import com.netx.shopping.model.productcenter.*;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantPacketSetService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.shopping.service.productcenter.ProductService;
import com.netx.shopping.service.productcenter.SkuService;
import com.netx.shopping.vo.*;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("newProductFuseAction")
public class ProductFuseAction {

    private Logger logger = LoggerFactory.getLogger(ProductAction.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantPictureAction merchantPictureAction;
    @Autowired
    private ProductPictureAction productPictureAction;
    @Autowired
    private MerchantFuseAction merchantFuseAction;
    @Autowired
    private CategoryProductAction categoryProductAction;
    @Autowired
    private SkuAction skuAction;
    @Autowired
    private ProductSkuSpecAction productSkuSpecAction;
    @Autowired
    private ProductFavoriteAction productFavoriteAction;
    @Autowired
    private SkuService skuService;
    @Autowired
    private ProductAction productAction;
    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private UserService userService;
    @Autowired
    private MerchantPacketSetService merchantPacketSetService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MerchantManagerService merchantManagerService;
    @Autowired
    private SearchServiceProvider searchServiceProvider;
    @Autowired
    private ProductDeliveryAction productDeliveryAction;

    /**
     * 获取商品详情-商品详情页
     * @param request
     * @param lat
     * @param lon
     * @return
     */
    public GetProductDetailResponseVo getProductDetail(GetGoodsRequestDto request, Double lat, Double lon){
        GetProductDetailResponseVo response = new GetProductDetailResponseVo();
        //获取商品详情
        Product product = productService.selectById(request.getId());
        if(product == null){
            return null;
        }
        VoPoConverter.copyProperties(product, response);
        //返回商品规格最低价格
        Map<String, Object> map = skuService.getMaxAndMinPrice(product.getId());
        if(map != null) {
            Sku sku = skuService.getSkuMinMarketPrice(product.getId());
            if(StringUtils.isNotBlank(sku.getUnit())){
                response.setUnit(sku.getUnit());
            }
            response.setMinPrice(new BigDecimal(Money.getMoneyString((Long)map.get("min"))));
            response.setMaxPrice(new BigDecimal(Money.getMoneyString((Long)map.get("max"))));
        }
        //查询商品配送方式
        response.setDeliveryWayList(productDeliveryAction.queryDeliveryByProductId(request.getId()));
        //获取商家和用户部分信息
        GetMerchantListVo merchantList = merchantFuseAction.getMerchantById(product.getMerchantId(), new BigDecimal(lat), new BigDecimal(lon));
        response.setMerchantList(merchantList);
        //获取商品图片
        List<String> marchantImagesUrl = merchantPictureAction.getPictureUrl(product.getMerchantId(), MerchantPictureEnum.LOGO.getType());
        if(marchantImagesUrl != null && marchantImagesUrl.size() > 0) {
            response.setMarchantImagesUrl(marchantImagesUrl.get(0));
        }
        //获取商品图片
        List<PictureResponseVo> logoImageUrl = productPictureAction.getPicture(request.getId(), ProductPictureTypeEnum.NONE);
        List<PictureResponseVo> detailImageUrl = productPictureAction.getPicture(request.getId(), ProductPictureTypeEnum.DETAIL);
        if(logoImageUrl != null && logoImageUrl.size() > 0){
            response.setLogoImageUrl(logoImageUrl);
        }
        if(detailImageUrl != null && detailImageUrl.size() > 0){
            response.setDetailImageUrl(detailImageUrl);
        }
        //获取类目
        List<String> categoryId = categoryProductAction.getCategoryIdByProductId(product.getId());
        if(categoryId != null && categoryId.size() > 0){
            List<Category> categories = categoryService.getParentCategory(categoryId);
            List<Category> tages = categoryService.getKidCategory(categoryId);
            if(categories != null && categories.size() > 0){
                response.setCategories(categories);
            }
            if(tages != null && tages.size() > 0){
                response.setTages(tages);
            }
        }
        //获取商品属性和对应属性值
        List<String> skuIds = skuAction.getSkuIdByProductId(product.getId());
        List<GetPropertyResponseVo> list = productSkuSpecAction.getPropertyOne(skuIds);
        if(list != null && list.size() > 0){
            response.setPropertyList(list);
        }
        //获取是否是收藏商家
        ProductFavorite productFavorite = productFavoriteAction.getProductFavoriteByUserIdAndProductId(request.getUserId(), request.getId());
        if(productFavorite != null){
            response.setIsHaveCollect(1);
        }else{
            response.setIsHaveCollect(2);
        }
        //获取客服人员
        response.setCustomerServiceUserId(merchantList.getUserId());
        String customerServiceUserNumber = merchantManagerService.getUserNetworkNumByMerchantId(product.getMerchantId(), MerchantManagerEnum.CUSTOMERSERVICE);
        if(StringUtils.isNotBlank(customerServiceUserNumber)) {
            String customerServiceUserId = userService.getUserIdByUserNumber(customerServiceUserNumber);
            if(StringUtils.isNotBlank(customerServiceUserId)) {
                response.setCustomerServiceUserId(customerServiceUserId);
            }
        }
        //判断可编辑
        String userNumber = userService.getUserNumberByUserId(request.getUserId());
        if(userNumber != null){
            MerchantManager merchantManager = merchantManagerService.getNoCashierByUserNetworkNumAndMerchantId(userNumber, product.getMerchantId());
            if(merchantManager != null){
                response.setEdit(true);
            }
        }
        //修改访问次数
        if(product.getVisitCount() == null){
            product.setVisitCount(0);
        }
        product.setVisitCount(product.getVisitCount()+1);
        productService.updateById(product);
        return response;
    }

    /**
     * 获取规格对应的价格、库存等（SKU）
     * @param valueIds
     * @return
     */
    public Sku getSku(List<String> valueIds, String productId){
        List<String> skuIds = new ArrayList<>();
        for(String valueId : valueIds){
            skuIds = productSkuSpecAction.getSkuIds(skuIds, valueId);
        }
        if(skuIds != null && skuIds.size() > 0){
            return skuService.getSkuByIdsAndProductId(skuIds, productId);
        }
        return null;
    }

    public GetProductListVo newgetMessage(String userId){
        User user = userService.selectById(userId);
        if (user != null){
            GetListByUserIdDto getListByUserIdDto = new GetListByUserIdDto();
            getListByUserIdDto.setUserId(userId);
            getListByUserIdDto.setCurrent(1);
            getListByUserIdDto.setSize(1);
            getListByUserIdDto.setStatus(1);
            List<GetProductListVo> list = getProductListByUserId(getListByUserIdDto, user.getLat(), user.getLon());
            if (list != null && list.size() >0){
                return list.get(0);
            }
        }
        return new GetProductListVo();
    }

    /**
     * 根据商家id获取商品列表
     * @param requestDto
     * @param lat
     * @param lon
     * @return
     */
    public List<GetProductAndSpecListVo> getProductListByMerchantId(QueryProductListRequestDto requestDto, BigDecimal lat, BigDecimal lon){
        List<GetProductAndSpecListVo> getProductAndSpecListVos = new ArrayList<>();
        //获取商品列表数据
        List<Product> products = productAction.getProductByMerchantId(requestDto);
        for(Product product : products){
            GetProductAndSpecListVo getProductAndSpecListVo = new GetProductAndSpecListVo();
            GetProductListVo getProductListVo = createProduct(product, lat, lon);
            VoPoConverter.copyProperties(getProductListVo, getProductAndSpecListVo);
            //返回商品规格最低价格
            Map<String, Object> map = skuService.getMaxAndMinPrice(product.getId());
            if(map != null) {
                Sku sku = skuService.getSkuMinMarketPrice(product.getId());
                if(StringUtils.isNotBlank(sku.getUnit())){
                    getProductAndSpecListVo.setUnit(sku.getUnit());
                }
                getProductAndSpecListVo.setMinPrice(new BigDecimal(Money.getMoneyString((Long)map.get("min"))));
                getProductAndSpecListVo.setMaxPrice(new BigDecimal(Money.getMoneyString((Long)map.get("max"))));
            }
            //获取商品属性和对应属性值
            List<String> skuIds = skuAction.getSkuIdByProductId(product.getId());
            List<GetPropertyResponseVo> list = productSkuSpecAction.getPropertyOne(skuIds);
            if(list != null && list.size() > 0){
                getProductAndSpecListVo.setPropertyList(list);
            }
            getProductAndSpecListVos.add(getProductAndSpecListVo);
        }
        return getProductAndSpecListVos;
    }

    /**
     * 根据userid查询最新发布商品(一条)
     * @param userId
     * @param lat
     * @param lon
     * @return
     */
    public GetProductListVo getNewestProductByUserId(String userId, BigDecimal lat, BigDecimal lon){
        List<Product> products = getProductByUserId(userId, 1);
        if(products != null && products.size() > 0){
            return createProduct(products.get(0), lat ,lon);
        }
        return null;
    }

    /**
     * 根据id查询发布商品(一条)
     * @param id
     * @param lat
     * @param lon
     * @return
     */
    public GetProductListVo getProductById(String id, BigDecimal lat, BigDecimal lon){
        Product product = productAction.getProductByd(id);
        if(product != null){
            return createProduct(product, lat ,lon);
        }
        return null;
    }
    /**
     * 当status=1根据用户id获取经营的商品
     * 当status=2根据用户id获取收藏的商品
     * @param getListByUserIdDto
     * @param lat
     * @param lon
     * @return
     */
    public List<GetProductListVo> getProductListByUserId(GetListByUserIdDto getListByUserIdDto, BigDecimal lat, BigDecimal lon){
        List<GetProductListVo> getProductListVos = new ArrayList<>();
        List<Product> products = getProductByUserId(getListByUserIdDto.getUserId(), getListByUserIdDto.getStatus(),new Page(getListByUserIdDto.getCurrent(),getListByUserIdDto.getSize()));
        for(Product product : products){
            getProductListVos.add(createProduct(product, lat, lon));
        }
        return getProductListVos;
    }

    /**
     * 
     * 分页获取商品列表
     *
     */
    public List<Product> getProductByUserId(String userId,Integer status,Page page){
        List<Product> products = new ArrayList<>();
        if(status == 1){
            String userNumber = userService.getUserNumberByUserId(userId);
            List<String> list = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
            List<String> merchantIds = new ArrayList<>(new HashSet(list));
            if(merchantIds != null && merchantIds.size() > 0) {
                products = productAction.getProductByMerchantIds(merchantIds,page);
            }
        }else{
            List<ProductFavorite> productIds = productFavoriteAction.getProductIdByUserId(userId,page);
            if(productIds != null && productIds.size() > 0){
                List<String> productIdList = productIds.stream().map(ProductFavorite::getProductId).collect(Collectors.toList());
                products = productService.selectBatchIds(productIdList);
            }
        }
        return products;
    }
    
    /*
    * 根据用户Id和状态获取商品列表 
    */
    
    public List<Product> getProductByUserId(String userId, Integer status){
        List<Product> products = new ArrayList<>();
        if(status == 1){
            String userNumber = userService.getUserNumberByUserId(userId);
            List<String> list = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
            List<String> merchantIds = new ArrayList<>(new HashSet(list));
            if(merchantIds != null && merchantIds.size() > 0) {
                products = productAction.getProductByMerchantIds(merchantIds);
            }
        }else{
            List<String> productIds = productFavoriteAction.getProductIdByUserId(userId);
            if(productIds != null && productIds.size() > 0){
                products = productService.selectBatchIds(productIds);
            }
        }
        return products;
    }

    public GetProductListVo createProduct(Product product, BigDecimal lat, BigDecimal lon){
        GetProductListVo getProductListVo = new GetProductListVo();
        VoPoConverter.copyProperties(product, getProductListVo);
        /*此处添加了多配送信息**/
        getProductListVo.setDeliveryWayList(productDeliveryAction.queryDeliveryByProductId(product.getId()));
        //返回商品规格最低价格
        Sku sku = skuAction.getSkuMinMarketPrice(product.getId());
        if(sku != null) {
            getProductListVo.setPrice(new BigDecimal(Money.getMoneyString(sku.getPrice())));
            getProductListVo.setMarketPrice(new BigDecimal(Money.getMoneyString(sku.getMarketPrice())));
        }
        //返回商品总销量
        Long volume = skuAction.getSumSellNums(product.getId());
        getProductListVo.setVolume(volume);
        //首单红包
        BigDecimal firstRate = merchantPacketSetService.getFirstRateByMerchatId(product.getMerchantId());
        if(firstRate != null){
            getProductListVo.setFirstRate(firstRate);
        }
        Merchant merchant = merchantService.selectById(product.getMerchantId());
        if(merchant != null){
            //信用
            Integer credit = userService.getCreditByUserId(merchant.getUserId());
            if(credit != null){
                getProductListVo.setCredit(credit);
            }
            getProductListVo.setCityCode(merchant.getCityCode());
            getProductListVo.setMerchantName(merchant.getName());
            getProductListVo.setHoldCredit(merchant.getSupportCredit());
            getProductListVo.setPayStatus(merchant.getPayStatus());
            getProductListVo.setDistance(DistrictUtil.calcDistance(lat, lon, merchant.getLat(), merchant.getLon()));
        }
        //获取类目
        List<String> categoryIds = categoryProductAction.getCategoryIdByProductId(product.getId());
        if(categoryIds!=null && categoryIds.size()>0){
            List<String> tages = categoryService.getKidCategoryName(categoryIds);
            List<String> categories = categoryService.getParentCategoryName(categoryIds);
            if(tages != null && tages.size() > 0){
                getProductListVo.setTages(new ArrayList(new HashSet(tages)));
            }
            if(categories != null && categories.size() > 0){
                getProductListVo.setCategories(new ArrayList(new HashSet(categories)));
            }
        }
        //获取商品图片
        List<String> logoImageUrl = productPictureAction.getProductPictureService().getUrlByTypeAndProductId(product.getId(), ProductPictureTypeEnum.NONE);
        if(logoImageUrl != null && logoImageUrl.size() > 0){
            getProductListVo.setProductImagesUrl(logoImageUrl.get(0));
        }
        return getProductListVo;
    }

    /**
     * 根据userid查询成功发布的商品/经营商品总数/收藏商品数
     * @param userId
     * @return
     */
    public GetRelatedGoodsMessageResponseVo getProductCount(String userId){
        GetRelatedGoodsMessageResponseVo responseVo = new GetRelatedGoodsMessageResponseVo();
        String userNumber = userService.getUserNumberByUserId(userId);
        List<String> merchantIds = new ArrayList<>(new HashSet<>(merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber)));
        //经营总数
        if(merchantIds != null && merchantIds.size() > 0) {
            Integer now = productAction.getAllProductCount(merchantIds);
            if(now != null) {
                responseVo.setNowQuantity(now);
            }
        }
        //发布总数
        Integer sum = productAction.getAllUpProductCount(userId);
        Integer goods = productFavoriteAction.countProductByUserId(userId);
        if(sum != null) {
            responseVo.setSumQuantity(sum);
        }
        if(goods != null) {
            responseVo.setGoodsCollectQuantity(goods);
        }
        return responseVo;
    }

    public List<GetProductListVo> list(GetGoodsListRequestDto request, double lat, double lon) throws Exception {
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
            searchQuery.addFristAscQueries(new LastAscQuery("marketPrice", true));
            searchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        }else if(request.getSort()==4){
            searchQuery.setHoldCredit(true);
            searchQuery.addFristAscQueries(new LastAscQuery("isLogin",false));
            searchQuery.addLastAscQuery(new LastAscQuery("credit", false));
        }

        //搜索
        List<ProductSearchResponse> searchResponses = searchServiceProvider.getProductSearchService().queryProducts(searchQuery);
        List<GetProductListVo> resultList = new ArrayList<>();
        for (int i=0;i<searchResponses.size();i++) {
            if (searchResponses.get(i).getOnlineStatus()){
                resultList.add(createGetProductListResponseVo(searchResponses.get(i)));
            }
        }
        return resultList;
    }

    private GetProductListVo createGetProductListResponseVo(ProductSearchResponse searchResponse){
        GetProductListVo getProductListVo = new GetProductListVo();
        VoPoConverter.copyProperties(searchResponse, getProductListVo);
        getProductListVo.setPrice(new BigDecimal(Money.getMoneyString(searchResponse.getPrice())));
        getProductListVo.setMarketPrice(new BigDecimal(Money.getMoneyString(searchResponse.getMarketPrice())));
        List<String> productImagesUrl = searchResponse.getProductImagesUrl();
        if(productImagesUrl != null && productImagesUrl.size()>0){
            getProductListVo.setProductImagesUrl(productPictureAction.updateImnagesUrl(productImagesUrl.get(0)));
        }
        List<String> categoryNames = searchResponse.getCategoryNames();
        List<String> categoryPids = searchResponse.getCategoryParentIds();
        List<String> tages = new ArrayList<>();
        List<String> categorys = new ArrayList<>();
        if(categoryNames != null && categoryPids != null && categoryNames.size() > 0 && categoryPids.size() > 0){
            for(int i=0; i<categoryPids.size(); i++){
                if("0".equals(categoryPids.get(i))){
                    categorys.add(categoryNames.get(i));
                }else{
                    tages.add(categoryNames.get(i));
                }
            }
        }
        getProductListVo.setTages(new ArrayList(new HashSet(tages)));
        getProductListVo.setCategories(new ArrayList(new HashSet(categorys)));
        return getProductListVo;
    }

    /**
     * boss
     * 查询商品列表
     * @param requestDto
     * @return
     */
    public Map<String, Object> queryMerchantProductList(QueryBusinessProductRequestDto requestDto){
        List<String> merchantIds = new ArrayList<>();
        if(StringUtils.isNotBlank(requestDto.getMerchantName())) {
            merchantIds = merchantService.queryMerchantIdByName(productAction.searchProcessing(requestDto.getMerchantName()));
        }
        if(StringUtils.isNotBlank(requestDto.getProductName())){
            requestDto.setProductName(productAction.searchProcessing(requestDto.getProductName()));
        }
        Page<Product> page = productService.selectProductList(merchantIds, requestDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", createBossProduct(page.getRecords()));
        return map;
    }

    public List<QueryBossProductResponseDto> createBossProduct(List<Product> products){
        List<QueryBossProductResponseDto> list = new ArrayList<>();
        for(Product product:products){
            QueryBossProductResponseDto responseDto = new QueryBossProductResponseDto();
            VoPoConverter.copyProperties(product, responseDto);
            String merchantName = merchantService.getMerchantNameById(product.getMerchantId());
            if(StringUtils.isNotBlank(merchantName)){
                responseDto.setMerchantName(merchantName);
            }
            String nickName = userService.getNickNameById(product.getPublisherUserId());
            if(StringUtils.isNotBlank(nickName)){
                responseDto.setNickName(nickName);
            }
            list.add(responseDto);
        }
        return list;
    }

}
