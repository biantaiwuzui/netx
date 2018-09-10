package com.netx.api.controller.shoppingmall.order;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.common.CommonListByUserIdDto;
import com.netx.common.vo.business.*;
import com.netx.common.vo.common.ArbitrationAddComplaintRequestDto;
import com.netx.common.vo.common.ArbitrationAppealRequestDto;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderFuseAction;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderPutOffFuseAction;
import com.netx.fuse.biz.shoppingmall.order.ProductReturnFuseAction;
import com.netx.fuse.proxy.ArbitrationProxy;
import com.netx.shopping.biz.order.HashCheckoutAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.model.order.ProductOrderPutOff;
import com.netx.shopping.model.order.ProductReturn;
import com.netx.shopping.biz.order.ProductReturnAction;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.vo.GetActivityOrderResponseVo;
import com.netx.shopping.vo.GetProductOrderResponseVo;
import com.netx.shopping.vo.TestQuartzRequestDto;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 商品订单控制层
 * Date: 2017-09-16
 */
@Api(description = "商品订单相关接口")
@RestController
@RequestMapping("/api/business/order")
public class OrderController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    ProductReturnAction productReturnAction;

    @Autowired
    ArbitrationProxy arbitrationProxy;

    @Autowired
    HashCheckoutAction hashCheckoutAction;

    @Autowired
    ProductOrderFuseAction productOrderFuseAction;

    @Autowired
    ProductReturnFuseAction productReturnFuseAction;

    @Autowired
    ProductOrderPutOffFuseAction productOrderPutOffFuseAction;

    @ApiOperation(value = "加入购物车或立即购买", notes = "加入购物车")
    @PostMapping("/commit")
    public JsonResult commit(@Valid @RequestBody CommitOrderRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            ProductOrder productOrder = productOrderAction.commit(request);
            if(productOrder != null){
                return JsonResult.success().addResult("productOrder", productOrder);
            }
            return JsonResult.fail("操作失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);

            return JsonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "订单详情", notes = "订单详情")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody GetGoodsOrderRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            GetProductOrderResponseVo getGoodsOrderResponseVo = productOrderFuseAction.get(request);
            if (getGoodsOrderResponseVo != null){
                return JsonResult.success().addResult("getGoodsOrderResponseVo",getGoodsOrderResponseVo);
            }
            return JsonResult.fail("获取订单详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);

            return JsonResult.fail("获取订单详情异常！");
        }
    }

    @ApiOperation(value = "订单详情(网能模块使用)", notes = "订单详情(网能模块使用)")
    @PostMapping("/getActivityOrder")
    public JsonResult getActivityOrder(String orderId, HttpServletRequest requestDto){
        if(StringUtils.isEmpty(orderId)){
            return JsonResult.fail("订单id不能为空！");
        }
        try {
            GetActivityOrderResponseVo getActivityOrderResponseVo = productOrderFuseAction.getActivityOrder(orderId,getLat(requestDto),getLon(requestDto));
            if (getActivityOrderResponseVo != null){
                return JsonResult.success().addResult("getActivityOrderResponseVo",getActivityOrderResponseVo);
            }
            return JsonResult.fail("获取订单详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("获取订单详情异常！");
        }
    }

    @ApiOperation(value = "订单列表", notes = "通用的订单列表接口，根据不同的条件返回")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody GetGoodsOrderListRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetProductOrderResponseVo> list = productOrderFuseAction.list(request);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取订单列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("获取订单列表异常！");
        }
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @PostMapping("/cancel")
    public JsonResult cancel(@Valid @RequestBody CancelOrderRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(!StringUtils.isNotEmpty(request.getId())){
            return JsonResult.fail( "id不能为空");
        }
        try {
            boolean res = productOrderFuseAction.cancel(request);
            if(!res){
                return JsonResult.fail("取消订单失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("取消订单异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "订单发货", notes = "订单发货")
    @PostMapping("/send")
    public JsonResult send(@Valid @RequestBody SendOrderRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderFuseAction.send(request);
            return getResult(res,"订单发货失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("订单发货异常!");
        }
    }

    @ApiOperation(value = "修改订单信息", notes = "修改订单信息")
    @PostMapping("/update")
    public JsonResult update(@Valid @RequestBody UpdateOrderRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderAction.update(request);
            return getResult(res,"修改订单消息失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改订单消息异常!");
        }
    }

    @ApiOperation(value = "订单支付", notes = "立即支付，如果选择零钱支付零钱不够，提示充值后再支付；如果选择网币支付不够，提示申购网币后再支付，" +
            "不申购则选用网币和零钱支付。")
    @PostMapping("/payOrder")
    public JsonResult payOrder(@Valid @RequestBody PayOrderRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            boolean res = productOrderFuseAction.newpayOrder(request);
            return getResult(res,"订单支付失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "用户申请退货", notes = "用户申请退货")
    @PostMapping("/applyReturn")
    public JsonResult applyReturn(@Valid @RequestBody ApplyReturnRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            ProductReturn productReturn = productReturnAction.getGoodsReturnByOrderId(request.getOrderId());
            if (productReturn != null){
                return JsonResult.fail("不能重复发起退货！");
            }
            ProductReturn res = productReturnFuseAction.applyReturn(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("用户申请退货失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("用户申请退货异常！");
        }
    }

    @ApiOperation(value = "商家同意退货", notes = "商家同意退货")
    @PostMapping("/agreeReturn")
    public JsonResult agreeReturn(@Valid @RequestBody AgreeReturnRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productReturnFuseAction.agreeReturn(request);
            return getResult(res,"商家不同意退货");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家同意退货异常！");
        }
    }

    @ApiOperation(value = "获取退货详情", notes = "获取退货详情")
    @PostMapping("/getProductReturn")
    public JsonResult getGoodsReturn(@Valid @RequestBody GetGoodsReturnRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            ProductReturn productReturn = productReturnAction.get(request);
            if (productReturn != null){
                return JsonResult.success().addResult("productReturn",productReturn);
            }
            return JsonResult.fail("获取退货详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取退货详情异常！");
        }
    }

    @ApiOperation(value = "用户确认或撤销退货", notes = "用户确认或撤销退货")
    @PostMapping("/confirmReturn")
    public JsonResult confirmReturn(@Valid @RequestBody ConfirmReturnRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            boolean res = productReturnFuseAction.confirmReturn(request);
            return getResult(res,"确认或撤销退货操作失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("确认或撤销退货操作异常!");
        }
    }

    @ApiOperation(value = "商家退货并退款", notes = "商家退货并退款")
    @PostMapping("/successReturn")
    public JsonResult successReturn(@Valid @RequestBody SuccessReturnRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productReturnFuseAction.successReturn(request);
            return getResult(res,"退货并退款操作失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家退货并退款操作异常!");
        }
    }

    @ApiOperation(value="用户确认收货",notes = "用户确认收货")
    @PostMapping("/receive")
    public JsonResult receive(@Valid @RequestBody OperateOrderRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            boolean res = productOrderFuseAction.receive(request);
            return getResult(res,"确认收货失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("确认收货异常!");
        }
    }

    @ApiOperation(value = "用户提醒发货",notes = "点击“提醒发货”后，给当前商家的业务主管发送信息")
    @PostMapping("remind")
    public JsonResult remind(@Valid @RequestBody OperateOrderRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            boolean res= productOrderFuseAction.remind(request);
            if (res){
                return JsonResult.success("催单成功,商家已收到你的催单");
            }else {
                return JsonResult.success("你已催单,每天可催单一次");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("提醒发货异常！");
        }
    }

    @ApiOperation(value = "用户催单提醒",notes = "点击“催单提醒”后，给当前商家的业务主管发送信息")
    @PostMapping("remind1")
    public JsonResult remind1(@Valid @RequestBody OperateOrderRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            boolean res= productOrderFuseAction.remind1(request);
            if (res){
                return JsonResult.success("催单成功,商家已收到你的催单");
            }else {
                return JsonResult.success("你已催单,每10分钟可催单一次");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("催单提醒异常！");
        }
    }

    @ApiOperation(value = "定时任务,检查单个订单是否发货",notes = "检查某个订单是否在规定时间内发货,若没有发货则取消订单")
    @PostMapping(value = "/checkIsSend", consumes = {"application/json"})
    public JsonResult checkIsSend(@Valid @RequestBody CheckIsSendRequestDto checkIsSendRequestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            int status = productOrderService.selectById(checkIsSendRequestDto.getGoodsOrderId()).getStatus();
            if (status!= OrderStatusEnum.ONWAY.getCode()){
                CancelOrderRequestDto cancelOrderRequestDto=new CancelOrderRequestDto();
                cancelOrderRequestDto.setId(checkIsSendRequestDto.getGoodsOrderId());
                boolean res= productOrderFuseAction.cancel(cancelOrderRequestDto);
                if (!res){
                    return JsonResult.fail("检查单个订单是否发货失败!");
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("检查单个订单是否发货异常");
        }
        return JsonResult.success();
    }
    @ApiOperation(value = "定时任务，检查是否付款",notes = "7天未付款则取消订单")
    @PostMapping(value = "/checkIsPay",consumes = {"application/json"})
    JsonResult checkIsPay(@Valid @RequestBody CheckIsPayRequestDto request,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            int status= productOrderService.selectById(request.getOrderId()).getStatus();
            if (status==OrderStatusEnum.UNPAID.getCode()){
                CancelOrderRequestDto cancelOrderRequestDto=new CancelOrderRequestDto();
                cancelOrderRequestDto.setId(request.getOrderId());
                boolean res= productOrderFuseAction.cancel(cancelOrderRequestDto);
                if (!res){
                    return JsonResult.fail("7天未付款则取消订单失败！");
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("7天未付款则取消订单异常");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "定时任务，使用冻结将钱转给商家",notes = "使用冻结将钱转给商家，订单状态待评论 （外卖型配送）")
    @PostMapping(value = "/changeOrderStatusAndPay")
    JsonResult changeOrderStatusAndPay(@RequestBody CommentOrderIdRepuestDto repuestDto){
        if (repuestDto.getOrderId()==null) {
            return JsonResult.fail( "订单id不能为空");
        }
        try{
            boolean res= productOrderFuseAction.changeOrderStatusAndPay(repuestDto.getOrderId());
            return getResult(res,"使用冻结将钱转给商家失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("使用冻结将钱转给商家异常");
        }
    }

    @ApiOperation(value = "测试定时任务", notes = "测试定时任务，以后写的定时任务就先在这里进行测试，测试完之后再加到对应的业务处理。")
    @PostMapping("/testQuartz")
    public JsonResult testQuartz(@RequestBody TestQuartzRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        //boolean res = businessQuartzService.checkIsSendFinish(requestDto.getOrderId(),0);
//        boolean res=businessQuartzService.checkIsSend(requestDto.getOrderId(),0);
//        if(res){
//            logger.info("start success");
//        }else{
//            logger.error("start failure");
//        }
        //TODO 定时任务
        return JsonResult.success();
    }

    @ApiOperation(value = "获取上个月订单交易总额", notes = "根据注册商家的用户userId,获取上个月订单交易总额")
    @PostMapping("/getlastMonthOdersAmount")
    public JsonResult getlastMonthOdersAmount(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetlastMonthOdersAmountResponseVo res = productOrderAction.getlastMonthOdersAmount(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取上个月订单交易总额失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取上个月订单交易总额异常");
        }
    }

    @ApiOperation(value = "获取商家每个月订单交易总额", notes = "根据注册商家的用户userId,获取商家每个月订单交易总额")
    @PostMapping("/getEveryMonthOdersAmount")
    public JsonResult getEveryMonthOdersAmount(@RequestBody GetEveryMonthOrderAmountRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors())
        {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            GetEveryMonthOrderAmountResponseDto res = productOrderAction.getEveryMonthOrderAmounts(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家每个月订单交易总额失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家每个月订单交易总额失败异常！");
        }
    }

    @ApiOperation(value = "商家拒收并投诉接口",notes = "type如果是订单的话，typeId是必填项")
    @PostMapping("/sellerRefuseAndArbitration")
    public JsonResult sellerRefuseAndArbitration(@Valid @RequestBody ArbitrationAddComplaintRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            //由于想在跨域导致效力不高，时常出现跨域失败返回不了值但是又可以成功跨域的情况
            String arbitrationId=arbitrationProxy.addComplaint(request);
//            System.out.println(arbitrationId+"####################");
            if(StringUtils.isBlank(arbitrationId)){
                return JsonResult.fail("商家投诉失败！");
            }
            ProductOrder productOrder = productOrderService.selectById(request.getTypeId());//订单号
            productOrder.setArbitrationId(arbitrationId);
            productOrderService.updateById(productOrder);
            if(!productOrderFuseAction.sellerRefuse(request)){
                return JsonResult.fail("更改订单状态失败!!");
            }
            return JsonResult.success("商家拒收投诉成功");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家拒收投诉异常");
        }
    }

    @ApiOperation(value = "用户申诉接口",notes = "用户申诉接口")
    @PostMapping("/userAppealArbitration")
    public JsonResult userAppealArbitration(@Valid @RequestBody UserAppealArbitrationRequestDto requestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            ProductOrder productOrder = productOrderService.selectById(requestDto.getOrderId());
            ArbitrationAppealRequestDto requestDto1=new ArbitrationAppealRequestDto();
            requestDto1.setId(productOrder.getArbitrationId());
            BeanUtils.copyProperties(requestDto,requestDto1);
            boolean bRet=arbitrationProxy.appealArbitration(requestDto1);
//                if(!bRet){
//                    return JsonResult.fail("用户申诉失败!");
//                }
            if(productReturnFuseAction.userAppealArbitration(requestDto)){
                return JsonResult.success("用户申诉操作成功!");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户申诉操作失败！");
        }
        return JsonResult.fail("用户申诉操作异常");
    }

    @ApiOperation(value = "用来后台获取订单仲裁操作和结果")
    @PostMapping("/autoRumSettleOrderArbitrationResult")
    public JsonResult autoRumSettleOrderArbitrationResult(@Valid @RequestBody CheckOrderArbitrationRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            String arbitrationDescription= productOrderFuseAction.autoRumSettleOrderArbitrationResult(request);
            if (arbitrationDescription != null){
                return JsonResult.success().addResult("arbitrationDescription",arbitrationDescription);
            }
            return JsonResult.fail("获取订单仲裁操作和结果失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取订单仲裁操作和结果异常！");
        }
    }

    @ApiOperation(value = "外部服务，为定时任务修改订单和退货的状态")
    @PostMapping("/changeGoodsOrderStatusAndReturnStatus")
    public JsonResult changeGoodsOrderStatusAndReturnStatus(CheckOrderArbitrationRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            boolean bRet= productOrderAction.changeGoodsOrderStatusAndReturnStatus(request);
            return getResult(bRet,"修改订单和退货的状态失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改订单和退货的状态异常!");
        }
    }

    @ApiOperation(value = "修改订单的状态")
    @PostMapping("/changeGoodsOrderStatus")
    public JsonResult changeGoodsOrderStatus(@Valid @RequestBody ChangeGoodsOrderStatusRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            boolean bRet= productOrderAction.changeGoodsOrderStatus(request);
            return getResult(bRet,"修改订单的状态");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改订单的状态异常！");
        }
    }

    @ApiOperation(value = "获取注册所有商家的订单")
    @PostMapping("/getAllGoodsOderByUserId")
    public JsonResult getAllGoodsOderByUserId(@Valid @RequestBody GetGoodsOrderListRequestDto request, HttpServletRequest requestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetProductOrderResponseVo> list = productOrderFuseAction.getAllGoodsOderByUserId(request);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取注册所有商家的订单失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("获取注册所有商家的订单异常！");
        }
    }

    @ApiOperation(value = "提供检查用户是否有订单还没完成的")
    @PostMapping("/checkUpHaveCompleteGoodsOrder")
    public JsonResult checkUpHaveCompleteGoodsOrder(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            boolean res = productOrderFuseAction.checkUpHaveCompleteGoodsOrder(userId);
            return getResult(res,"提供检查用户是否有订单还没完成的失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("提供检查用户是否有订单还没完成的异常！");
        }

    }


    @ApiOperation(value = "获取hash值")
    @PostMapping("/getHash")
    public JsonResult getHash(@RequestHeader(name="token") String token){
        if(token==null) {
            return JsonResult.fail("token不能为空");
        }
        try {
            String res= hashCheckoutAction.getHashByToken(token);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取hash值失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取hash值异常！");
        }
    }


    @ApiOperation(value = "获取主管人员userId")
    @PostMapping("/getManageUserIdByOrderId")
    public JsonResult getManageUserIdByOrderId(String orderId){
        if(StringUtils.isEmpty(orderId))
        {
            return JsonResult.fail("数据不能为空");
        }
        try {
            String res = productOrderFuseAction.getManageUserIdByOrderId(orderId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取主管人员userId失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取主管人员userId异常！");
        }
    }

    @ApiOperation(value = "扣取商家红包提成",notes = "扣取商家红包提成，供网值活动等用户支付订单时调用")
    @PostMapping("/directRedPackMoney")
    public JsonResult directRedPackMoney(String orderId){
        if(StringUtils.isEmpty(orderId))
        {
            return JsonResult.fail("数据不能为空");
        }
        try {
            boolean res = productOrderFuseAction.directRedPackMoney(orderId);
            return getResult(res,"扣取商家红包提成失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("扣取商家红包提成异常！");
        }
    }

    @ApiOperation(value = "申请延迟收货",notes = "当商家同意延迟收货时，物流完成时间需加7天")
    @PostMapping("/putOffGoodsOrder")
    public JsonResult putOffGoodsOrder(@Valid @RequestBody ApplyPutOffRequestDto request,BindingResult bindingResult,HttpServletRequest requestDto) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            ProductOrderPutOff productOrderPutOff = productOrderPutOffFuseAction.applyPutOff(request);
            if (productOrderPutOff != null){
                return JsonResult.success().addResult("productOrderPutOff",productOrderPutOff);
            }
            return JsonResult.fail("申请延迟收货失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("申请延迟收货异常！");
        }
    }

    @ApiOperation(value = "商家是否同意延迟收货",notes = "商家是否同意延迟收货，物流完成时间需加7天")
    @PostMapping("/ifCanagreePutOff")
    public JsonResult ifCanagreePutOff(@Valid @RequestBody AgreePutOffRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderPutOffFuseAction.agreePutOff(request);
            return getResult(res,"商家拒接延迟收货！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家是否同意延迟收货操作异常！");
        }
    }

    @ApiOperation(value = "商家拒接退货")
    @PostMapping("/notAgreeGoodsOrderReturn")
    public JsonResult notAgreeGoodsOrderReturn(String orderId){
        if (orderId == null){
            return JsonResult.fail("订单id不能为空");
        }
        try {
            boolean res = productOrderFuseAction.notAgreeGoodsOrderReturn(orderId);
            return getResult(res,"商家拒接退货失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家拒接退货异常！");
        }
    }


    @ApiOperation(value = "定时任务",notes = "获取物流详情")
    @PostMapping("/getOrderLogistcsDetails")
    public JsonResult getOrderLogistcsDetails(){
        try {
            productOrderAction.getOrderLogistcsDetails();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家已发货用户待收货订单， 判定从付款的时间点到当前节点，是否超过十五天未确认收货，如果超过十五天时间，则自动确认用户收货，并打款给商家")
    @PostMapping(value = "/changeLogisticsStatusAndPay")
    public JsonResult changeLogisticsStatusAndPay(){
        try{
            productOrderFuseAction.changeLogisticsStatusAndPay();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家已付款待商家发货订单列表， 判定从付款的时间点到当前节点，是否超过三天未发货，如果超过三天时间，则退款给用户")
    @PostMapping(value = "/checkIsSend()")
    public JsonResult checkIsSend(){
        try{
            productOrderFuseAction.checkIsSend();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家同意延期收货的订单，判定从付款的时间点到当前节点， 是否超过十五天＋七天 未确认说话，如果超过二十三天时间，则自动确认用户收货，并打款给商家")
    @PostMapping(value = "/changePutOffStatusAndPay()")
    public JsonResult changePutOffStatusAndPay(){
        try{
            productOrderFuseAction.changePutOffStatusAndPay();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家的待退款订单，判定从用户申请退款的时间点到当前节点，是否超过三天未同意退款，则自动确认商家退款给用户，并退款给用户")
    @PostMapping(value = "/changeResturnStatusAndPay()")
    public JsonResult changeResturnStatusAndPay(){
        try{
            productOrderFuseAction.changeResturnStatusAndPay();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家的同意退款订单，判定从商家退款的时间点到当前节点，是否超过十五天未用户未确认收到退款，则自动确认退款交易完成")
    @PostMapping(value = "/changeResturnStatusAndOrderStatus()")
    public JsonResult changeResturnStatusAndOrderStatus(){
        try{
            productOrderAction.changeResturnStatusAndOrderStatus();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家的待延期订单，判定从用户申请延期的时间点到当前节点，是否超过三天未同意延期，则自动确认商家同意延期，收货时间+7天")
    @PostMapping(value = "/changePutOffAndOrderStatus()")
    public JsonResult changePutOffAndOrderStatus(){
        try{
            productOrderAction.changePutOffAndOrderStatus();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "定时任务",notes = "每1个小时获取所有商家的待评论订单，判定从是否超过三天未评论，则自动确认订单已完成")
    @PostMapping(value = "/changeOrderStatusToCompleted()")
    public JsonResult changeOrderStatusToCompleted(){
        try{
            productOrderAction.changeOrderStatusToCompleted();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail();
        }
    }

    @ApiOperation(value = "根据userId查询购物车商品列表")
    @PostMapping(value = "/getShoppingTrolleyByUserId")
    public JsonResult getShoppingTrolleyByUserId(String userId){
        if(userId == null){
            return JsonResult.fail("UserId不能为空");
        }
        try{
            Map<String, List<ShoppingTrolleyRequestVo>> shoppingTrolley = productOrderAction.getShoppingTrolleyByUserId(userId);
            return JsonResult.success().addResult("shoppingTrolley",shoppingTrolley);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("购物车异常！");
        }
    }

    @ApiOperation(value = "删除购物车订单")
    @PostMapping(value = "/deleteOrder")
    public JsonResult deleteOrder(@Valid @RequestBody UpdateAndDeleteOrderReqestDto reqest,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderAction.delete(reqest.getOrderId());
            return getResult(res, "删除购物车订单失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除购物车订单异常！");
        }
    }

    @ApiOperation(value = "编辑购物车订单")
    @PostMapping(value = "/updateOrder")
    public JsonResult updateOrder(@Valid @RequestBody UpdateAndDeleteOrderReqestDto reqest,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderAction.update(reqest.getOrderId(),reqest.getQuanty());
            return getResult(res, "编辑购物车订单失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑购物车订单异常！");
        }
    }

    @ApiOperation(value = "支付前改变订单接口")
    @PostMapping(value = "/changeOrder")
    public JsonResult changeOrder(@Valid @RequestBody ChangeGoodsOrderStatusAndStatusRequestDto request,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderAction.changeProductOrderStatusAndUpdate(request);
            return getResult(res, "编辑购物车订单失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑购物车订单异常！");
        }
    }

    @ApiOperation(value = "改变订单状态并推送信息给商家")
    @PostMapping(value = "/changeActivityOrderStatusAndSendMessage")
    public JsonResult changeActivityOrderStatusAndSendMessage(@Valid @RequestBody ChangeActivityOrderStatusAndSendMessageRequestDto request,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productOrderFuseAction.changeActivityOrderStatusAndSendMessage(request);
            return getResult(res, "操作失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作异常！");
        }
    }
}