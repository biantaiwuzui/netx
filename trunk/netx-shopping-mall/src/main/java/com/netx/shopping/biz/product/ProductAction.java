package com.netx.shopping.biz.product;

import com.netx.common.common.enums.AliyunBucketType;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.vo.QueryBusinessProductRequestDto;
import com.netx.utils.DistrictUtil;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.*;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.biz.order.HashCheckoutAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.order.ProductOrderItemService;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.service.product.ProductCollectService;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.vo.ProductMySort;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 网商-商品表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductAction {

    private Logger logger = LoggerFactory.getLogger(ProductAction.class);
    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    HashCheckoutAction hashCheckoutAction;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductOrderItemService productOrderItemService;

    @Autowired
    ProductCollectService productCollectService;

    @Autowired
    ProductCategoryAction productCategoryAction;

    @Autowired
    ProductSpeAction productSpeAction;



    public Product releaseGoods(ReleaseGoodsRequestDto request) throws Exception{
        //判断是否重复提交
        boolean res= hashCheckoutAction.hashCheckout(request.getHash());
        if (!res){
            throw new Exception("机器无效提交订单");
        }

        Product product = new Product();
        Boolean flag = StringUtils.isNotBlank(request.getId());
        if(flag) {
            Product goodsone = productService.selectById(request.getId());
            if (goodsone != null) {
                //当其他管理人员修改时，保持注册userId一致
                request.setUserId(goodsone.getUserId());
            }
        }
        BeanUtils.copyProperties(request, product);
        product.setDelivery(request.getIsDelivery());
        product.setReturn(request.getIsReturn());
        product.setStatus(ProductStatusEnum.UP.getCode());
        if(StringUtils.isNotBlank(request.getId())){
            product.setUpdateUserId(request.getUserId());
        }else{
            product.setCreateUserId(request.getUserId());
        }
        product.setDeleted(0);
        productService.insertOrUpdate(product);
        //操作商品目录关系表
        //productCategoryAction.insertOrUpdate(flag,product.getId(),request.getCategoryId(),request.getTagIds(),request.getUserId());
        //操作商品规格
        productSpeAction.insertOrUpdate(flag,product.getId(),request.getSpeRequertDtoList(),request.getUserId());
        return productService.selectById(product.getId());
    }

    /**
     *
     * @param product
     * @param distance
     * @return
     */
    private ProductMySort createGoodsMySort(Product product, Double distance){
        ProductMySort productMySort = new ProductMySort();
        productMySort.setProduct(product);
        productMySort.setOrder((int)(distance * 100));
        return productMySort;
    }


    /**
     * 获取商品成交次数
     * @param goodsId
     * @return
     */
    public Long getGoodsOrderItemNum(String goodsId){
        try {
            List<String> oderIdList = getGoodsOrderItemIds(goodsId);
            return oderIdList.size()<1 ? 0l: productOrderService.getProductOrderCount(oderIdList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return 0l;
    }

    /**
     * 获取商品成交的订单ids
     * @param goodsId
     * @return
     */
    private List<String> getGoodsOrderItemIds(String goodsId){

        return productOrderItemService.getProductOrderIds(goodsId);
    }

    public boolean optGoods(BackManageOptGoodsRequestDto request){
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        if(request.getStatus() == ProductStatusEnum.UP.getCode() && request.getReason() != null){//上架
            product.setUpReason(request.getReason());
        }else{//下架
            if (request.getReason() != null){
                product.setDownReason(request.getReason());
            }
        }
        return productService.updateById(product);
    }

    public Integer goodsQuantity(String userId, Integer status){
        Integer  resultQuantity=null;
        if (status != 0)
            return productService.getAllUpProductCount(userId);
        else
            return productService.getAllProductCount(userId);
    }

    public BigDecimal getGoodsDealAmount(String goodsId){
        return productService.getGoodsDealAmount(goodsId);
    }

    public GetNewestOdersMessageResponseVo getMessage2(String userId){

        List<String> list= sellerService.getSellersIdList(userId);
        if (list == null){
            return null;
        }

        ProductOrder productOrder = productOrderService.getproductOrder(list);
        return productOrder ==null?new GetNewestOdersMessageResponseVo(): VoPoConverter.copyProperties(productOrder,GetNewestOdersMessageResponseVo.class) ;
    }



    public GetRelatedGoodsMessageResponseVo getMessage1(String userId){
        GetRelatedGoodsMessageResponseVo result=new GetRelatedGoodsMessageResponseVo();
        Integer sumQuantity=goodsQuantity(userId,0)==null?0:goodsQuantity(userId,0);
        Integer nowQuantity=goodsQuantity(userId,1)==null?0:goodsQuantity(userId,1);
        Integer goodsCollectQuantity=this.getGoodsCollectQuantity(userId)==null?0:this.getGoodsCollectQuantity(userId);
        result.setNowQuantity(nowQuantity);
        result.setSumQuantity(sumQuantity);
        result.setGoodsCollectQuantity(goodsCollectQuantity);

        return result;

    }


    public Boolean delete(DeleteGoodsRequestDto request){
        return productService.delete(request.getId());
    }


    public void downSellerAllGoods(String sellerId){
        productService.downSellerAllGoods(sellerId);
    }


    public GetSellerGoodsQuantityResponseVo getMessage3(String sellerIds){
        GetSellerGoodsQuantityResponseVo result=new GetSellerGoodsQuantityResponseVo();
        //String转换为list集合,遍历出商家数量
        String[] arr=sellerIds.split(",");
        List<String> ids=new ArrayList<>();
        for (int i =0;i<arr.length;i++)
        {
            Seller seller= sellerService.selectById(arr[i]);
            if (seller!=null)
            {
                ids.add(arr[i]);
            }
        }
        result.setRangeSellers(ids.size());
        //根据商家ids查询商品数量
        if(ids.size()==0)
        {
            result.setRangeGoods(0);
            return result;
        }

        result.setRangeGoods(productService.getProductCountBySellerId(ids));
        return result;
    }


    public GetRelatedOdersMessageResponseVo getMessage4(String userId){
        GetRelatedOdersMessageResponseVo result=new GetRelatedOdersMessageResponseVo();
        Integer oderSumQuantity=productOrderAction.oderQuantity(userId,8)==null?0:productOrderAction.oderQuantity(userId,8);
        Integer oderNowQuantity=productOrderAction.oderQuantity(userId,7)==null?0:productOrderAction.oderQuantity(userId,7);
//        oderSumQuantity= productOrderAction.oderQuantity(request.getUserId(),8);
//        oderNowQuantity= productOrderAction.oderQuantity(request.getUserId(),7);
        result.setOderNowQuantity(oderNowQuantity);
        result.setOderSumQuantity(oderSumQuantity);
        return result;
    }

    public double getDistance(BigDecimal centerLat, BigDecimal centerLon, String sellerId) throws Exception{
        //获取商家
        Seller seller= sellerService.selectById(sellerId);
        if(seller==null)
        {
            throw  new Exception("获取商品对应商家经纬度失败");
        }
        //获取距离
        double distance= DistrictUtil.calcDistance(centerLat,centerLon,seller.getLat(),seller.getLon());
        return distance;
    }


    public List<String> getGoodsBySellerId(String sellerId){
        return productService.getProductIdsBySellerId(sellerId);
    }


    public Integer getGoodsCollectQuantity(String userId){
        try {
            return productCollectService.getProductCollectCountByUserId(userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return 0;
    }


    public boolean isHaveThisName(String userId, String name){

        int i=productService.isHaveThisName(userId,name);
        if (i>0){
            return true;
        }
        return false;
    }


    public void updateImages(Product product){
        if (product.getProductImagesUrl()!=null) {
            //判断是否要加前缀
            if (!product.getProductImagesUrl().contains("http")) {
                product.setProductImagesUrl(addImgUrlPreUtil.addImgUrlPres(product.getProductImagesUrl(),AliyunBucketType.ProductBucket));
            }
        }
        if (product.getDetailImagesUrl()!=null){
            //判断是否要加前缀
            if (!product.getDetailImagesUrl().contains("http")) {
                product.setDetailImagesUrl(addImgUrlPreUtil.addImgUrlPres(product.getDetailImagesUrl(),AliyunBucketType.ProductBucket));
            }
        }
    }

    public String updateImnagesUrl(String imangesUrl){
        if (imangesUrl != null){
            if (!imangesUrl.contains("http")) {
                return addImgUrlPreUtil.getProductImgPre(imangesUrl);
            }
            return imangesUrl;
        }
        return null;
    }


    public boolean isCanHandle(IsCanHandleRequestDto request){
        //商家判断
        if (StringUtils.isNotEmpty(request.getSellerId())){
            int count = productOrderService.getNoCompletesProductOrderCountBySellerId(request.getSellerId());
            if (count>0){
                return false;
            }
        }else if (StringUtils.isNotEmpty(request.getGoodsId())){
            //商品判断
            List<String> orderIds= productOrderItemService.getProductOrderIdByProductId(request.getGoodsId());

            int count = productOrderService.getProductOrderCountByOrderIds(orderIds);
            if (count>0){
                return false;
            }
        }
        return true;
    }

    public List<Map<String,String>> selectGoodsList(){
        return productService.selectGoodsList();
    }
}