package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.ReceivablesOrderAddRequestDto;
import com.netx.common.vo.common.ReceivablesOrderFindRequestDto;
import com.netx.ucenter.model.common.CommonReceivablesOrder;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.json.JsonResult;
import com.netx.utils.money.Money;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Create by allen on 17-9-15
 */
@Api(description = "收款相关")
@RestController
@RequestMapping("/api/receivablesOrder")
public class ReceivablesOrderController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(ReceivablesOrderController.class);
    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @ApiOperation("收款方添加收款订单")
    @PostMapping("/addReceivablesOrder")
    public JsonResult addReceivablesOrder(@RequestBody @Valid ReceivablesOrderAddRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonReceivablesOrder wzCommonReceivablesOrder=new CommonReceivablesOrder();
            wzCommonReceivablesOrder.setAmount(new Money(request.getAmount()).getCent());
            wzCommonReceivablesOrder.setToUserId(request.getToUserId());
            wzCommonReceivablesOrder.setDeleted(0);
            boolean insert = commonServiceProvider.getReceivablesOrderService().insert(wzCommonReceivablesOrder);
            if(insert){
                return JsonResult.success().addResult("result",wzCommonReceivablesOrder.getId());
            }else{
                return JsonResult.fail("操作失败");
            }
        } catch (Exception e) {
            logger.error("修改提现账户异常："+e.getMessage(), e);
            return JsonResult.fail("修改提现账户异常");
        }
    }

    @ApiOperation("查询收款订单")
    @PostMapping("/findReceivablesOrder")
    public JsonResult findReceivablesOrder(@RequestBody @Valid ReceivablesOrderFindRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonReceivablesOrder wzCommonReceivablesOrder = commonServiceProvider.getReceivablesOrderService().selectById(request.getId());
            if(wzCommonReceivablesOrder==null){
                return JsonResult.fail("收款订单不存在");
            }
            return JsonResult.success().addResult("result",wzCommonReceivablesOrder);
        } catch (Exception e) {
            logger.error("查询收款订单异常："+e.getMessage(), e);
            return JsonResult.fail("查询收款订单异常");
        }
    }
}
