package com.netx.api.controller.shoppingmall.ordercenter;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.business.CancelOrderRequestDto;
import com.netx.shopping.model.ordercenter.constants.PayTypeEnum;
import com.netx.shopping.vo.*;
import com.netx.fuse.biz.shoppingmall.ordercenter.MerchantOrderFuseAction;
import com.netx.shopping.biz.ordercenter.HashCheckoutAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

@Api(description = "新 · 网商订单接口")
@RestController
@RequestMapping("/api/business/merchantOrder")
public class MerchantOrderController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(MerchantOrderController.class);

    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;

    @Autowired
    private MerchantOrderFuseAction merchantOrderFuseAction;

    @Autowired
    private HashCheckoutAction hashCheckoutAction;

    @Autowired
    private UserAction userAction;

    @ApiOperation(value = "获取hash值")
    @PostMapping("/getHash")
    public JsonResult getHash(HttpServletRequest request){
        String userId = null;
        String authToken = tokenHelper.getToken(request);
        if(authToken!=null){
            userId = tokenHelper.getUsernameFromToken(authToken);
        }
        if(StringUtils.isBlank(userId)){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            String res= hashCheckoutAction.getHashByToken(userId,authToken);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取hash值失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取hash值异常！");
        }
    }

    @ApiOperation(value = "立即购买", notes = "订单")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody AddOrderRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            User user = userAction.queryUser(userId);
            String orderId = merchantOrderInfoAction.add(userId,user.getUserNumber(),user.getNickname(),dto);
            if(orderId==null){
                return JsonResult.fail("立即购买失败");
            }
            return JsonResult.success().addResult("orderId",orderId);
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail("立即购买异常!");
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("立即购买失败");
        }
    }

    @ApiOperation(value = "修改订单信息", notes = "修改订单信息")
    @PostMapping("/update")
    public JsonResult update(@Valid @RequestBody OrderAdressRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = merchantOrderInfoAction.updateAddress(dto);
            return getResult(res, "修改订单地址失败！");
        }catch (RuntimeException e){
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改订单地址异常!");
        }
    }

    @ApiOperation(value = "添加订单备注【下单】", notes = "修改订单信息：<br>" +
            "[订单id:备注],例子：[\"订单id1\":\"备注\",\"订单id2\":\"备注\"]")
    @PostMapping("/addRemark")
    public JsonResult addRemark(@RequestBody List<AddRemarkBeanDto> list, HttpServletRequest request){
        if(list==null){
            return JsonResult.fail("备注列表不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean res = merchantOrderInfoAction.updateRemark(list,userId);
            return getResult(res, "添加订单备注失败！");
        }catch (RuntimeException e){
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加订单备注异常!");
        }
    }

    @ApiOperation(value = "订单支付", notes = "立即支付，如果选择零钱支付零钱不够，提示充值后再支付；如果选择网币支付不够，提示申购网币后再支付，" +
            "不申购则选用网币和零钱支付。")
    @PostMapping("/payOrder")
    public JsonResult payOrder(@Valid @RequestBody MerchantPayOrderRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            checkCreditPay(dto.getPayType(),dto.getCreditPay());
        }catch (RuntimeException e){
            return JsonResult.fail(e.getMessage());
        }
        String userId;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return getResult(merchantOrderFuseAction.newPayOrder(userId,dto),"订单支付失败！");
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("订单支付失败");
        }
    }

    /**
     * 判断含有网信支付的支付方式
     * @param payTypeEnum
     * @param creditPrice
     */
    private void checkCreditPay(PayTypeEnum payTypeEnum, List<CreditPayDto> creditPrice){
        switch (payTypeEnum){
            case PT_NONE:
                return;
            case PT_ALI:
                return;
            case PT_WECHAT:
                return;
        }
        if(creditPrice==null || creditPrice.size()<0){
            throw new RuntimeException("网信金额不能为空");
        }
    }

    @ApiOperation(value = "订单列表", notes = "下单的查询")
    @PostMapping("/queryOrderList")
    public JsonResult queryOrderList(@RequestParam("ids") String ids, HttpServletRequest request){
        if(StringUtils.isBlank(ids)){
            return JsonResult.fail("订单id集不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.success().addResult("list",merchantOrderInfoAction.getOrderList(userId,ids.split(",")));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取订单列表异常！");
        }
    }

    @ApiOperation(value = "订单列表(Map)", notes = "下单的查询")
    @PostMapping("/queryOrderMap")
    public JsonResult queryOrderMap(@RequestParam("ids") String ids, HttpServletRequest request){
        if(StringUtils.isBlank(ids)){
            return JsonResult.fail("订单id集不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.successJsonResult(merchantOrderInfoAction.getOrderMap(userId,ids.split(",")));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取订单列表异常！");
        }
    }

    @ApiOperation(value = "订单详情", notes = "订单详情",response = MerchantOrderDetailResponseDto.class)
    @GetMapping("/get")
    public JsonResult get(@RequestParam("id") String id, HttpServletRequest request){
        if(StringUtils.isBlank(id)){
            return JsonResult.fail("订单id不能为空");
        }
        String userId;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            User user = userAction.queryUser(userId);
            return JsonResult.success().addResult("detail", merchantOrderFuseAction.queryDetail(id,user.getUserNumber(),user.getLat().doubleValue(),user.getLon().doubleValue()));
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("订单详情异常");
        }
    }

    @ApiOperation(value = "订单详情(网能模块使用)", notes = "订单详情",response = WorthOrderDetailResponseDto.class)
    @PostMapping("/getActivityOrder")
    public JsonResult getActivityOrder(@Valid @RequestBody WorthOrderDetailRequestDto worthOrderDetailRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        BigDecimal lon = new BigDecimal(getLon(request));
        BigDecimal lat = new BigDecimal(getLat(request));
        try {
            return JsonResult.success().addResult("detail", merchantOrderFuseAction.queryWorthOrderDetail(lon, lat, worthOrderDetailRequestDto.getTypeId(), worthOrderDetailRequestDto.getOrderTypeEnum()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("订单详情异常");
        }
    }

    @ApiOperation(value = "订单列表【根据不同的条件返回】",response = OrderListResponseDto.class,
            notes = "OQ_ALL：全部<br>OQ_ORDER：待付款<br>OQ_CONFIRMED：待发货<br>OQ_COMMENT：待评价<br>OQ_SHIPPING：待收货/消费")
    @PostMapping("/userList")
    public JsonResult userList(@Valid @RequestBody UserOrderRequestDto userOrderRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            List<OrderListResponseDto> list;
            /* 获取我发出的订单列表 **/
            if(userOrderRequestDto.getIsQueryBuy()){
                list = merchantOrderFuseAction.addUserInfo(merchantOrderInfoAction.getOrderList(userGeo, userOrderRequestDto.getOrderQueryEnum(), userOrderRequestDto.getCurrent(), userOrderRequestDto.getSize()),true);
            }else{
                /* 获取我收到的订单列表 **/
                User user = userAction.queryUser(userGeo.getUserId());
                list = merchantOrderFuseAction.addUserInfo(merchantOrderInfoAction.getOrderListByUserNumber(userGeo,user.getUserNumber(),userOrderRequestDto.getOrderQueryEnum(), userOrderRequestDto.getCurrent(), userOrderRequestDto.getSize()),false);
            }
            return JsonResult.success().addResult("list", list);
        }catch (RuntimeException e){
            logger.error(e.getMessage()+"查询异常",e);
            return JsonResult.fail(e.getMessage());
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @PostMapping("/cancel")
    public JsonResult cancel(@Valid @RequestBody CancelOrderRequestDto request, HttpServletRequest requestDto, BindingResult bindingResult){
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
            boolean res = merchantOrderFuseAction.cancel(request);
            if (res) {
                return JsonResult.success();
            }
            return JsonResult.fail("取消订单失败！");
        }catch (RuntimeException e){
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("取消订单异常！");
        }

    }

    @ApiOperation(value = "订单发货", notes = "订单发货")
    @PostMapping("/send")
    public JsonResult send(@Valid @RequestBody MerchantOrderSendRequestDto merchantOrderSendRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return getResult(merchantOrderFuseAction.send(userId,merchantOrderSendRequestDto),"订单发货失败");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("订单发货异常");
        }
    }

    @ApiOperation(value = "用户申请退货【未完成】", notes = "用户申请退货")
    @PostMapping("/applyReturn")
    public JsonResult applyReturn(@RequestParam("id") String id, HttpServletRequest request){
        if(StringUtils.isBlank(id)){
            return JsonResult.fail("订单id不能为空");
        }

        return JsonResult.success();
    }

    @ApiOperation(value = "商家同意退货【未完成】", notes = "商家同意退货")
    @PostMapping("/agreeReturn")
    public JsonResult agreeReturn(HttpServletRequest request){
        return JsonResult.success();
    }

    @ApiOperation(value = "获取退货详情【未完成】", notes = "获取退货详情")
    @PostMapping("/getProductReturn")
    public JsonResult getProductReturn(HttpServletRequest request){
        return JsonResult.success();
    }

    @ApiOperation(value = "用户确认或撤销退货【未完成】", notes = "用户确认或撤销退货")
    @PostMapping("/confirmReturn")
    public JsonResult confirmReturn(HttpServletRequest request){
        return JsonResult.success();
    }

/*    @ApiOperation(value = "商家退货并退款【未完成】", notes = "商家退货并退款")
    @PostMapping("/successReturn")
    public JsonResult successReturn(HttpServletRequest request){
        return JsonResult.success();
    }*/

    @ApiOperation(value = "用户确认收货", notes = "用户确认收货")
    @PostMapping("/receive")
    public JsonResult receive(String orderId,HttpServletRequest request){
        if(StringUtils.isBlank(orderId)){
            return JsonResult.fail("订单id不能为空");
        }
        String userId;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            merchantOrderFuseAction.receive(orderId,userId);
            return JsonResult.success();
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("确认收货异常");
        }

    }

    @ApiOperation(value = "用户提醒发货【非外卖】", notes = "点击“提醒发货”后，给当前商家的业务主管发送信息")
    @PostMapping("/remind")
    public JsonResult remind(@RequestParam("id") String id, HttpServletRequest request){
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            boolean res= merchantOrderFuseAction.remind(id,userId);
            if (res){
                return JsonResult.success("提醒成功,商家已收到你的提醒");
            }else {
                return JsonResult.success("你已提醒,每天可提醒一次");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("提醒发货异常："+e.getMessage());
        }
    }

    @ApiOperation(value = "用户催单提醒【外卖】", notes = "点击“用户催单提醒”后，给当前商家的业务主管发送信息")
    @PostMapping("/reminder")
    public JsonResult reminder(@RequestParam("id") String id,HttpServletRequest request){
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            boolean res= merchantOrderFuseAction.reminder(id,userId);
            if (res){
                return JsonResult.success("催单成功,商家已收到你的催单");
            }else {
                return JsonResult.success("你已催单,每10分钟可以催单一次");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("催单异常："+e.getMessage());
        }
    }

/*

    @ApiOperation(value = "商家拒收并投诉接口【未完成】", notes = "type如果是订单的话，typeId是必填项")
    @PostMapping("/sellerRefuseAndArbitration")
    public JsonResult sellerRefuseAndArbitration(HttpServletRequest request){
        return JsonResult.success();
    }
*/

 /*   @ApiOperation(value = "用户申诉接口【未完成】", notes = "用户申诉接口")
    @PostMapping("/userAppealArbitration")
    public JsonResult userAppealArbitration(HttpServletRequest request){
        return JsonResult.success();
    }*/

    @ApiOperation(value = "修改订单的状态",notes = "订单状态：<br>"
            + "1：待付款-OS_ORDER<br>"
            + "2：待发货-OS_CONFIRMED<br>"
            + "3：物流中-OS_SHIPPING<br>"
            + "4：退货中-OS_RETURN<br>"
            + "5：投诉中-OS_COMPLATING<br>"
            + "6：待评论-OS_COMMENT<br>"
            + "7：已完成-OS_FINISH<br>"
            + "8：已取消-OS_CANCELED<br>"
            + "9: 待生成-OS_WATTING<br>"
            + "10: 已付款-OS_FINSH_PAY <br>"
            + "11: 已服务-OS_SERVICE")
    @PostMapping("/changeGoodsOrderStatus")
    public JsonResult changeGoodsOrderStatus(@Valid @RequestBody ChangeOrderStatusRequestDto changeOrderStatusRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return getResult(merchantOrderInfoAction.changeGoodsOrderStatus(changeOrderStatusRequestDto),"修改订单状态失败");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改订单状态异常");
        }
    }

    @ApiOperation(value = "扣取商家红包提成",notes = "扣取商家红包提成，供网值活动等用户支付订单时调用")
    @PostMapping("/directRedPackMoney")
    public JsonResult directRedPackMoney(@RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(orderId)) {
            return JsonResult.fail("订单id不能为空");
        }
        try {
            boolean res = merchantOrderFuseAction.directRedPackMoney(orderId);
            return getResult(res,"扣取商家红包提成失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("扣取商家红包提成异常！");
        }
    }

    @ApiOperation(value = "申请延迟收货【未完成】",notes = "当商家同意延迟收货时，物流完成时间需加7天")
    @PostMapping("/putOffGoodsOrder")
    public JsonResult putOffGoodsOrder(HttpServletRequest request){
        return JsonResult.success();
    }

    @ApiOperation(value = "商家是否同意延迟收货【未完成】",notes = "商家是否同意延迟收货，物流完成时间需加7天")
    @PostMapping("/ifCanagreePutOff")
    public JsonResult ifCanagreePutOff(HttpServletRequest request){
        return JsonResult.success();
    }

    @ApiOperation(value = "商家拒接退货【未完成】")
    @PostMapping("/notAgreeGoodsOrderReturn")
    public JsonResult notAgreeGoodsOrderReturn(HttpServletRequest request){
        return JsonResult.success();
    }
    
//    @ApiOperation(value = "都别动，这是测试评论订单用的")
//    @PostMapping("/testShedule")
//    public JsonResult testShedule(){
//        merchantOrderInfoAction.changeOrderStatusToCompleted();
//        return JsonResult.success();
//    }

    @ApiOperation(value = "刷新物流信息")
    @PostMapping("/updateOrderInfoShippingDetails")
    public JsonResult updateOrderInfoShippingDetails() throws Exception {
        try {
            merchantOrderInfoAction.updateOrderInfoShippingDetails();
            return JsonResult.success();
        }catch (Exception e){
            throw new Exception(e.getMessage(),e);
        }
    }

}

