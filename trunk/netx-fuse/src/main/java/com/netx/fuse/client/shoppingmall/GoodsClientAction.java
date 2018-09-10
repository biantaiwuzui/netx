package com.netx.fuse.client.shoppingmall;

import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.productcenter.ProductFuseAction;
import com.netx.shopping.biz.productcenter.ProductAction;
import com.netx.shopping.vo.GetProductListResponseVo;
import com.netx.shopping.vo.GetProductListVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class GoodsClientAction {

    private Logger logger = LoggerFactory.getLogger(GoodsClientAction.class);

    @Autowired
    ProductAction productAction;

    @Autowired
    ProductFuseAction productFuseAction;

    /**
     * 获取所有商品
     * @param
     * @return
     *
     */
    public List<Map<String,String>> selectGoodsList(){
        try {
            List<Map<String,String>> list = productAction.selectGoodsList();
            return list;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }


    /**
     * 根据userid查询成功发布的商品/经营商品总数/收藏商品数
     * @param request
     * @return
     */
    public GetRelatedGoodsMessageResponseVo getRelatedGoodsMessage(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                GetRelatedGoodsMessageResponseVo res = productFuseAction.getProductCount(request.getUserId());
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

        return null;
    }


    /**
     *根据商家id查询商品ids
     */
    public List<String> getGoodsBySellerId(String merchantId) throws Exception{
        if (StringUtils.isNotEmpty(merchantId)){
            List<String> res = productAction.getProductIdByMerchantIds(merchantId);
            return res;
        }
        return null;
    }


    /**
     * 根据用户ID查询最新的商品
     * @param userId
     * @param lat
     * @param lon
     * @return
     */
    public GetProductListVo getNewestGoodsMessage(String userId, BigDecimal lat, BigDecimal lon){
        try {
            GetProductListVo res = productFuseAction.getNewestProductByUserId(userId, lat, lon);
            return res;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new GetProductListVo();
    }


    /**
     * 最新的订单
     * 根据用户ID查询
     * @param request   1.CommonUserIdRequestDto 用户ID
     * @return GetNewestOdersMessageResponseVo 对象,以下是属性
     */
    public GetNewestOdersMessageResponseVo getNewestOdersMessage(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                //todo 最新订单
                GetNewestOdersMessageResponseVo res = null;//productAction.getMessage2(request.getUserId());
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }


    /**
     * 获取网币适用范围商家商品数量
     * 根据商家ID集查询
     * @param    sellerIds 商家id集
     * @return GetSellerGoodsQuantityResponseVo 对象,以下是属性
     * 1.Integer rangeGoods    适用范围商品数
     * 2.Integer rangeSellers   适用范围商家数
     */
    /**
     * 适用范围商品数
     */
    public GetSellerGoodsQuantityResponseVo getSellerGoodsQuantity(String sellerIds){
        if (StringUtils.isNotEmpty(sellerIds)){
            try {
                //todo 获取网币适用范围商家商品数量
                GetSellerGoodsQuantityResponseVo res = null; //productAction.getMessage3(sellerIds);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * 订单
     * 根据用户ID查询
     * @param request   1.CommonUserIdRequestDto 用户ID
     * @return GetRelatedOdersMessageResponseVo 对象,以下是属性
     *  1.Integer oderSumQuantity   订单总数
     *  2.Integer oderNowQuantity   现有订单数量
     */
    public GetRelatedOdersMessageResponseVo getRelatedOdersMessage(CommonUserIdRequestDto request){
        if (StringUtils.isNotEmpty(request.getUserId())){
            try {
                //todo 根据用户ID查询订单
                GetRelatedOdersMessageResponseVo res = null; //productAction.getMessage4(request.getUserId());
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }

}
