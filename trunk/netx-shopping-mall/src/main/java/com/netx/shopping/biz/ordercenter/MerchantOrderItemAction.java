package com.netx.shopping.biz.ordercenter;


import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.shopping.biz.productcenter.ProductPictureAction;
import com.netx.shopping.biz.productcenter.ProductSkuSpecAction;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.MerchantOrderItem;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.model.ordercenter.constants.PayStatus;
import com.netx.shopping.model.ordercenter.constants.ShippingStatus;
import com.netx.shopping.model.ordercenter.constants.TradeStatus;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.service.ordercenter.MerchantOrderItemService;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class MerchantOrderItemAction {

    private Logger logger = LoggerFactory.getLogger(MerchantOrderItemAction.class);

    @Autowired
    private MerchantOrderItemService merchantOrderItemService;

    @Autowired
    private ProductPictureAction productPictureAction;

    @Autowired
    private ProductSkuSpecAction productSkuSpecAction;

    public MerchantOrderItemService getMerchantOrderItemService() {
        return merchantOrderItemService;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean send(String orderNo){
        MerchantOrderItem merchantOrderItem = new MerchantOrderItem();
        merchantOrderItem.setShippingStatus(ShippingStatus.SS_SHIPPING.name());
        merchantOrderItem.setOrderStatus(OrderStatusEnum.OS_SHIPPING.name());
        return merchantOrderItemService.updateOrderItem(orderNo,merchantOrderItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean add(MerchantOrderInfo merchantOrderInfo, String skuId, Product product,long price,long finalPrice,int quantity){
        MerchantOrderItem merchantOrderItem = create(merchantOrderInfo,skuId,product,price,finalPrice);
        Boolean flag = true;
        for(int i=0;i<quantity;i++){
            flag = merchantOrderItemService.insert(merchantOrderItem);
            merchantOrderItem.setId(null);
        }
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(String orderNo,OrderStatusEnum orderStatusEnum,PayStatus payStatus,TradeStatus tradeStatus,ShippingStatus shippingStatus){
        MerchantOrderItem merchantOrderItem = new MerchantOrderItem();
        if(payStatus!=null){
            merchantOrderItem.setPayStatus(payStatus.name());
        }
        if(shippingStatus!=null){
            merchantOrderItem.setShippingStatus(shippingStatus.name());
        }
        if(tradeStatus!=null){
            merchantOrderItem.setTradeStatus(tradeStatus.name());
        }
        if(orderStatusEnum!=null){
            merchantOrderItem.setOrderStatus(orderStatusEnum.name());
        }
        return merchantOrderItemService.updateOrderItem(orderNo,merchantOrderItem);
    }

    private MerchantOrderItem create(MerchantOrderInfo merchantOrderInfo, String skuId, Product product, long price,long finalPrice){
        MerchantOrderItem merchantOrderItem = new MerchantOrderItem();
        merchantOrderItem.setUserId(merchantOrderInfo.getUserId());
        merchantOrderItem.setProductId(product.getId());
        merchantOrderItem.setSkuId(skuId);
        merchantOrderItem.setSkuDesc(productSkuSpecAction.getValueNames(skuId));
        merchantOrderItem.setTradeStatus(TradeStatus.TS_WATTING.name());//交易状态
        merchantOrderItem.setUnitPrice(price);
        merchantOrderItem.setShippingStatus(ShippingStatus.SS_UNSHIPPED.name());//物流状态
        merchantOrderItem.setPayStatus(PayStatus.PS_UNPAID.name());//支付状态
        merchantOrderItem.setProductImgUrl(productPictureAction.getProductPictureService().getPictureUrlOne(product.getId(), ProductPictureTypeEnum.NONE));//商品图片
        merchantOrderItem.setProductName(product.getName());//商品名
        merchantOrderItem.setOrderNo(merchantOrderInfo.getOrderNo());//订单号
        merchantOrderItem.setOrderStatus(merchantOrderInfo.getOrderStatus());//订单状态
        merchantOrderItem.setFinalUnitPrice(finalPrice);
        merchantOrderItem.setMerchantId(merchantOrderInfo.getMerchantId());//商家id
        return merchantOrderItem;
    }
}
