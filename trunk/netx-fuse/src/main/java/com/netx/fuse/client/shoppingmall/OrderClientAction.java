package com.netx.fuse.client.shoppingmall;

import com.netx.common.user.util.MapOrObjectUtil;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderFuseAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.service.order.ProductOrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderClientAction {

    private Logger logger = LoggerFactory.getLogger(OrderClientAction.class);

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    ProductOrderFuseAction productOrderFuseAction;

    /**
     *检查某个订单是否在规定时间内发货,若没有发货则取消订单
     */
    public void checkIsSend(Map map){
        if(StringUtils.isNotEmpty(map.get("goodsOrderId").toString())){
            try{
                int status = productOrderService.selectById((String)map.get("goodsOrderId")).getStatus();
                if (status!= OrderStatusEnum.ONWAY.getCode()){
                    CancelOrderRequestDto cancelOrderRequestDto=new CancelOrderRequestDto();
                    cancelOrderRequestDto.setId((String)map.get("goodsOrderId"));
                    boolean res= productOrderFuseAction.cancel(cancelOrderRequestDto);
                    if (!res){
                        logger.error("定时任务取消订单失败");
                    }
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }


    /**
     * 定时任务，检查是否付款7天未付款则取消订单
     */
    public void checkIsPay(Map map){
        if (StringUtils.isNotEmpty(map.get("orderId").toString())){
            try{
                int status= productOrderService.selectById((String)map.get("orderId")).getStatus();
                if (status==OrderStatusEnum.UNPAID.getCode()){
                    CancelOrderRequestDto cancelOrderRequestDto=new CancelOrderRequestDto();
                    cancelOrderRequestDto.setId((String)map.get("orderId"));
                    boolean res= productOrderFuseAction.cancel(cancelOrderRequestDto);
                    if (!res){
                        logger.error("定时任务取消订单失败");
                    }
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

    }


    /**
     * 根据注册商家的用户userId,获取上个月订单交易总额
     */
    public GetlastMonthOdersAmountResponseVo getlastMonthOdersAmount(String userId){
        if (StringUtils.isNotEmpty(userId)){
            try {
                GetlastMonthOdersAmountResponseVo res = productOrderAction.getlastMonthOdersAmount(userId);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * 根据注册商家的用户userId,获取商家每个月订单交易总额
     */
    public GetEveryMonthOrderAmountResponseDto getEveryMonthOdersAmount(GetEveryMonthOrderAmountRequestDto request){
        if (request.getStart() != null){
            try {
                GetEveryMonthOrderAmountResponseDto res = productOrderAction.getEveryMonthOrderAmounts(request);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }

    /**
     * 用来后台获取订单仲裁操作和结果
     */
    public String autoRumSettleOrderArbitrationResult(CheckOrderArbitrationRequestDto request){
        if (StringUtils.isNotEmpty(request.getArbitrationId())&&StringUtils.isNotBlank(request.getTypeId())){
            try{
                String arbitrationDescription= productOrderFuseAction.autoRumSettleOrderArbitrationResult(request);
                return arbitrationDescription;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

        return null;
    }


    /**
     * 外部服务，为定时任务修改订单和退货的状态
     */
    public boolean changeGoodsOrderStatusAndReturnStatus(CheckOrderArbitrationRequestDto request){
        if (StringUtils.isNotEmpty(request.getTypeId())&&StringUtils.isNotEmpty(request.getArbitrationId())){
            try{
                boolean bRet= productOrderAction.changeGoodsOrderStatusAndReturnStatus(request);
                return bRet;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

        return false;
    }


    /**
     * 提供检查用户是否有订单还没完成的
     */
    public boolean checkUpHaveCompleteGoodsOrder (String userId){
        if (StringUtils.isNotEmpty(userId)){
            try {
                boolean res = productOrderFuseAction.checkUpHaveCompleteGoodsOrder(userId);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return false;
    }

    /**
     * 定时任务,获取物流详情
     */
    public void getOrderLogistcsDetails(){
        try {
            productOrderAction.getOrderLogistcsDetails();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }


    /**
     * 获取主管人员userId
     */
    public String getManageUserIdByOrderId(String orderId){
        try {
            String res = productOrderFuseAction.getManageUserIdByOrderId(orderId);
            return res;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 定时任务,每1个小时获取所有商家已发货用户待收货订单， 判定从付款的时间点到当前节点，是否超过十五天未确认收货，如果超过十五天时间，则自动确认用户收货，并打款给商家
     */
    public void changeLogisticsStatusAndPay(){
        try{
            productOrderFuseAction.changeLogisticsStatusAndPay();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

//    /**
//     * 定时任务,使用冻结将钱转给商家，订单状态待评论 （外卖型配送）
//     */
//    public void changeOrderStatusAndPay(Map map){
//        if (StringUtils.isNotEmpty(map.get("orderId").toString())){
//            try{
//                CommentOrderIdRepuestDto repuestDto = MapOrObjectUtil.mapToObject(map,CommentOrderIdRepuestDto.class);
//                productOrderFuseAction.changeOrderStatusAndPay(repuestDto.getOrderId());
//            }catch (Exception e){
//                logger.error(e.getMessage(),e);
//            }
//        }
//    }



    /**
     * 定时任务,每1个小时获取所有商家已付款待商家发货订单列表， 判定从付款的时间点到当前节点，是否超过三天未发货，如果超过三天时间，则退款给用户
     */
    public void checkIsSend(){
        try{
            productOrderFuseAction.checkIsSend();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 定时任务,每1个小时获取所有商家同意延期收货的订单，判定从付款的时间点到当前节点， 是否超过十五天＋七天 未确认说话，如果超过二十三天时间，则自动确认用户收货，并打款给商家
     */
    public void changePutOffStatusAndPay(){
        try{
            productOrderFuseAction.changePutOffStatusAndPay();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        //TODO 定时任务
    }

    /**
     * 定时任务,每1个小时获取所有商家的待退款订单，判定从用户申请退款的时间点到当前节点，是否超过三天未同意退款，则自动确认商家退款给用户，并退款给用户
     */
    public void changeResturnStatusAndPay(){
        try{
            productOrderFuseAction.changeResturnStatusAndPay();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }


    /**
     * 定时任务,每1个小时获取所有商家的同意退款订单，判定从商家退款的时间点到当前节点，是否超过十五天未用户未确认收到退款，则自动确认退款交易完成
     */
    public void changeResturnStatusAndOrderStatus(){
        try{
            productOrderAction.changeResturnStatusAndOrderStatus();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 定时任务,每1个小时获取所有商家的待延期订单，判定从用户申请延期的时间点到当前节点，是否超过三天未同意延期，则自动确认商家同意延期，收货时间+7天
     */
    public void changePutOffAndOrderStatus(){
        try{
            productOrderAction.changePutOffAndOrderStatus();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 定时任务,每1个小时获取所有商家的待评论订单，判定从是否超过三天未评论，则自动确认订单已完成
     */
    public void changeOrderStatusToCompleted(){
        try{
            productOrderAction.changeOrderStatusToCompleted();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
