package com.netx.fuse.biz.shoppingmall.order;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.AgreePutOffRequestDto;
import com.netx.common.vo.business.ApplyPutOffRequestDto;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.order.ProductOrderPutOffAction;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.enums.ProductPutOffStatusEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.model.order.ProductOrderPutOff;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.order.ProductOrderPutOffService;
import com.netx.shopping.service.order.ProductOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductOrderPutOffFuseAction {

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductOrderPutOffService productOrderPutOffService;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    ProductOrderPutOffAction productOrderPutOffAction;

    @Autowired
    MessagePushProxy messagePushProxy;

    public ProductOrderPutOff applyPutOff(ApplyPutOffRequestDto request) throws Exception {

        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        String sellerUserId = null;
        if( null != productOrder){
            Seller seller = sellerService.selectById(productOrder.getSellerId());
            if( null != seller){
                sellerUserId = seller.getUserId();
            }
        }
        if (productOrder.getStatus()!= OrderStatusEnum.ONWAY.getCode()){
            throw new Exception("订单状态不是物流中，不能申请延迟收货");
        }
        ProductOrderPutOff productOrderPutOff = new ProductOrderPutOff();
        BeanUtils.copyProperties(request, productOrderPutOff);

        productOrderPutOff.setSellerUserId(sellerUserId);
        productOrderPutOff.setApplyTime(new Date());
        productOrderPutOff.setStatus(ProductPutOffStatusEnum.USER_APPLY.getCode());
        productOrderPutOff.setCreateUserId(request.getUserId());
        productOrderPutOff.setDeleted(0);
        productOrderPutOffService.insert(productOrderPutOff);
        //改变订单状态为延迟
        ProductOrder order = new ProductOrder();
        order.setId(productOrder.getId());
        order.setStatus(OrderStatusEnum.PUTOFF.getCode());
        productOrderService.updateById(order);

        //获取订单商家
        Seller seller= sellerService.selectById(productOrder.getSellerId());
        //发送消息给商家相关人员
        MessageFormat mf=new MessageFormat("{0}于{1}申请延迟收货，订单号为{2}，请及时处理");
        //获取认购者用户昵称
        List<String> userIds = new ArrayList<>();
        userIds.add(productOrder.getUserId());
        Map<String, UserSynopsisData> userSynopsisDataMap = userClientProxy.selectUserMapByIds(userIds);

        UserSynopsisData userSynopsisData = userSynopsisDataMap.get(productOrder.getUserId());

        //获取当前时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String nowDate=simpleDateFormat.format(new Date().getTime());
        String alertMsg=mf.format(new String[]{userSynopsisData.getNickName(),nowDate, productOrder.getOrderNum()});
        //获取商家先关人员 去重后发消息
        List<String> userIdList= sellerFuseAction.getSellerAllUserId(seller.getId());
        List<String> userIdListone= sellerAction.getUntqueuUserId(userIdList);
        for (String string:userIdListone){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"延迟收货通知",string, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
        }

        return productOrderPutOff;
    }

    public boolean agreePutOff(AgreePutOffRequestDto request) {
        ProductOrderPutOff productOrderPutOff = productOrderPutOffService.getProductOrderPutOff(request.getOrderId());
        if(productOrderPutOff ==null){
            return false;
        }

        //同意
        if (request.getIsAgree()==0){
            BeanUtils.copyProperties(request, productOrderPutOff);
            productOrderPutOff.setAgreeTime(new Date());
            productOrderPutOff.setStatus(ProductPutOffStatusEnum.SELLER_AGREE.getCode());
            //获取订单发货时间 并计算出延迟收货到期时间
            long sendTime= productOrderService.selectById(productOrderPutOff.getOrderId()).getSendTime()==null?null: productOrderService.selectById(productOrderPutOff.getOrderId()).getSendTime().getTime();

            long expirationTime= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(sendTime,7,2);
            productOrderPutOff.setExpirationTime(new Date(productOrderPutOffAction.getCurrentTimeStr(expirationTime)));

            //发信息给认购者
            MessageFormat mf=new MessageFormat("你{0}的延迟收货申请，已获商家同意");
            //获取当前时间
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            String nowDate=simpleDateFormat.format(new Date().getTime());
            String alertMsg=mf.format(new String[]{nowDate});

            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"商家同意延迟通知", productOrderPutOff.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail, productOrderPutOff.getOrderId());
        }else {
            BeanUtils.copyProperties(request, productOrderPutOff);
            productOrderPutOff.setStatus(ProductPutOffStatusEnum.REFUSE.getCode());

            //发信息给认购者
            MessageFormat mf=new MessageFormat("你{0}的延迟收货申请，商家拒接延迟");
            //获取当前时间
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            String nowDate=simpleDateFormat.format(new Date().getTime());
            String alertMsg=mf.format(new String[]{nowDate});

            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"商家拒接延迟通知", productOrderPutOff.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail, productOrderPutOff.getOrderId());
        }

        return productOrderPutOffService.updateById(productOrderPutOff);
    }

}
