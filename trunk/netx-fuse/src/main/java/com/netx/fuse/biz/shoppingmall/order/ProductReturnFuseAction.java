package com.netx.fuse.biz.shoppingmall.order;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.order.ProductOrderItemAction;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.enums.ProductReturnStatusEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.model.order.ProductOrderItem;
import com.netx.shopping.model.order.ProductReturn;
import com.netx.shopping.model.product.ProductSpe;
import com.netx.shopping.service.business.ManageService;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.service.order.ProductReturnService;
import com.netx.shopping.service.product.ProductSpeService;
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
public class ProductReturnFuseAction {

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductReturnService productReturnService;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    MessagePushProxy messagePushProxy;

    @Autowired
    ProductOrderFuseAction productOrderFuseAction;

    @Autowired
    ManageService manageService;

    @Autowired
    ProductOrderItemAction productOrderItemAction;

    @Autowired
    ProductSpeService productSpeService;

    public ProductReturn applyReturn(ApplyReturnRequestDto request) {
        ProductReturn productReturn = new ProductReturn();
        BeanUtils.copyProperties(request, productReturn);
        String sellerUserId = null;
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        if( null != productOrder){
            Seller seller = sellerService.selectById(productOrder.getSellerId());
            if( null != seller){
                sellerUserId = seller.getUserId();
            }
        }
        productReturn.setSellerUserId(sellerUserId);
        productReturn.setApplyTime(new Date());
        productReturn.setStatus(ProductReturnStatusEnum.USER_APPLY.getCode());
        productReturn.setCreateUserId(request.getUserId());
        productReturn.setDeleted(0);
        productReturnService.insert(productReturn);
        //改变订单状态为退货中
        ProductOrder order = new ProductOrder();
        order.setId(productOrder.getId());
        order.setStatus(OrderStatusEnum.INRETURN.getCode());
        productOrderService.updateById(order);

        //获取订单商家
        Seller seller= sellerService.selectById(productOrder.getSellerId());
        //发送消息给商家业务主管
        MessageFormat mf=new MessageFormat("{0}于{1}申请退单，订单号为{2}，请及时处理");
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
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"申请退货通知",string, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
        }

        return productReturn;
    }


    public boolean agreeReturn(AgreeReturnRequestDto request) throws Exception{

        ProductReturn productReturn = productReturnService.selectById(request.getId());
        if(productReturn ==null){
            return false;
        }
        //判断订单状态 代发货状态下退回用户付款金额
        ProductOrder productOrder = productOrderService.selectById(productReturn.getOrderId());
        if (productOrder !=null){
            if (productOrder.getStatus()==OrderStatusEnum.UNSEND.getCode()){
                //给用户退款
//                productOrderAction.refund(productOrder, sellerAction.selectById(productOrder.getSellerId()).getUserId(), productOrder.getUserId()); //给用户退款
                productOrderFuseAction.repealFrozen(productOrder.getId(), productOrder.getUserId());
            }
        }
        //发信息给认购者
        MessageFormat mf=new MessageFormat("你{0}的退货申请，已获商家同意");
        //获取当前时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String nowDate=simpleDateFormat.format(new Date().getTime());
        String alertMsg=mf.format(new String[]{nowDate});

        messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"商家同意退货通知", productReturn.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail, productReturn.getOrderId());

        BeanUtils.copyProperties(request, productReturn);
        productReturn.setAgreeTime(new Date());
        productReturn.setUpdateTime(new Date());
        productReturn.setStatus(ProductReturnStatusEnum.SELLER_AGREE.getCode());

        return productReturnService.updateById(productReturn);
    }

    public boolean confirmReturn(ConfirmReturnRequestDto request) {
        boolean result = false;
        ProductReturn productReturn = new ProductReturn();
        productReturn.setId(request.getId());
        productReturn.setStatus(request.getStatus());
        if(request.getStatus() == ProductReturnStatusEnum.USER_CONFIRM.getCode()){//确认
            productReturn.setConfirmTime(new Date());
            productReturn.setLogisticsName(request.getLogisticsName());
            productReturn.setLogisticsNo(request.getLogisticsNo());
        }else if(request.getStatus() == ProductReturnStatusEnum.USER_CANCEL.getCode()){//撤销
            productReturn.setCancelTime(new Date());
        }
        productReturnService.updateById(productReturn);

        //获取商家业务主管userId
        String oderId=productReturnService.selectById(request.getId()).getOrderId();
        ProductOrder productOrder1 = productOrderService.selectById(oderId);
        Seller seller= sellerService.selectById(productOrder1.getSellerId());
        String manageUserId = manageService.selectById(seller.getManageId()).getUserId();

        if(request.getStatus() == ProductReturnStatusEnum.USER_CANCEL.getCode()){//撤销
            //改变订单状态为物流中
            ProductReturn dbProductReturn = productReturnService.selectById(productReturn.getId());
            ProductOrder productOrder = new ProductOrder();
            productOrder.setId(dbProductReturn.getOrderId());
            productOrder.setStatus(OrderStatusEnum.ONWAY.getCode());
            productOrderService.updateById(productOrder);

            //发送消息给商家业务主管 撤销
            MessageFormat mf=new MessageFormat("订单号{0}已被用户撤销退货");
            String alertMsg=mf.format(new String[]{productOrder1.getOrderNum()});
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"撤销退货通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder1.getId());
        }else {
            //判断订单状态是否为待发货
            if (productOrder1.getStatus()==OrderStatusEnum.UNSEND.getCode()){
                //改变订单状态为已完成
                ProductReturn dbProductReturn = productReturnService.selectById(productReturn.getId());
                ProductOrder productOrder = new ProductOrder();
                productOrder.setId(dbProductReturn.getOrderId());
                productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                productOrderService.updateById(productOrder);

            }

            //发送消息给商家业务主管 确认
            MessageFormat mf=new MessageFormat("订单号{0}已被用户确认退货");
            String alertMsg=mf.format(new String[]{productOrder1.getOrderNum()});
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"确认退货通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder1.getId());
        }
        result = true;
        return result;
    }

    public boolean userAppealArbitration(UserAppealArbitrationRequestDto requestDto) {
        ProductReturn goodReturn=productReturnService.getProductReturnByOrderId(requestDto.getOrderId());
        if(goodReturn!=null){
            goodReturn.setStatus(ProductReturnStatusEnum.RETURN_APPEAL.getCode());
            if(productReturnService.updateById(goodReturn)){
                return true;
            }
        }
        //发消息给商家业务主管
        ProductOrder productOrder = productOrderService.selectById(requestDto.getOrderId());
        Seller seller= sellerService.selectById(productOrder.getSellerId());
        String manageUserId = manageService.selectById(seller.getManageId()).getUserId();

        MessageFormat mf=new MessageFormat("订单号{0}已被用户发起退货申述");
        String alertMsg=mf.format(new String[]{productOrder.getOrderNum()});
        messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"退货申述通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
        return false;
    }

    private void addProductSpeStock(String orderId){
        List<ProductOrderItem> list = productOrderItemAction.getListByOrderId(orderId);
        if (list !=null && list.size()>0){
            for (ProductOrderItem productOrderItem : list){
                ProductSpe productSpe = productSpeService.selectById(productOrderItem.getSpeId());
                if (productSpe != null){
                    productSpe.setStock(productSpe.getStock()+productOrderItem.getQuantity());
                }
            }
        }
    }

    public boolean successReturn(SuccessReturnRequestDto request) throws Exception{
        boolean result = false;
        ProductReturn productReturn = new ProductReturn();
        BeanUtils.copyProperties(request, productReturn);
        productReturn.setStatus(ProductReturnStatusEnum.SUCCESS.getCode());
        productReturn.setSuccessTime(new Date());
        productReturnService.updateById(productReturn);
        //改变订单状态为已完成
        ProductReturn dbProductReturn = productReturnService.selectById(request.getId());
        ProductOrder dbProductOrder = productOrderService.selectById(dbProductReturn.getOrderId());
        ProductOrder order = new ProductOrder();
        order.setId(dbProductOrder.getId());
        order.setStatus(OrderStatusEnum.COMPLETED.getCode());
        productOrderService.updateById(order);
        //给用户退款
        productOrderFuseAction.repealFrozen(dbProductOrder.getId(), dbProductOrder.getUserId());
        //增加库存量
        addProductSpeStock(dbProductOrder.getId());
        result = true;
        return result;
    }
}
