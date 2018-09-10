package com.netx.api.controller.shoppingmall.marketing;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.marketingcenter.MerchantAddCustomeragentFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantRegisterAction;
import com.netx.shopping.vo.AddRelationshipRequestDto;
import com.netx.shopping.vo.ApprovalRelationshipRequestDto;
import com.netx.shopping.vo.QueryStateRequestDto;
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
import java.util.List;

/**
 * Created By 黎子安
 * Description: 营销模块控制层
 * Date: 2017-12-29
 */
@Api(description = " 营销模块相关接口")
@RestController
@RequestMapping("/api/business/marketing/")
public class MarketingController extends BaseController{

    @Autowired
    MerchantAction merchantAction;

    @Autowired
    MerchantRegisterAction merchantRegisterAction;

    @Autowired
    MerchantAddCustomeragentFuseAction addCustomeragentFuseAction;

    private Logger logger = LoggerFactory.getLogger(MarketingController.class);

    @ApiOperation(value = "添加客服代理", notes = "填写添加的客服代理的客服代码")
    @PostMapping("addRelationship")
    public JsonResult addRelationship(@Valid @RequestBody AddRelationshipRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
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
            switch (addCustomeragentFuseAction.addCustomeragentByCode(requestDto.getSellerId(), requestDto.getCode(), requestDto.getReason(),userId)) {
                case 4:
                    return JsonResult.fail("商家id不存在！");
                case 3:
                    return JsonResult.fail("此商家已是其他商家的客服代理");
                case 2:
                    return JsonResult.fail("系统错误，请重试");
                case 1:
                    return JsonResult.success("添加成功");
                case 0:
                    return JsonResult.fail( "所添加的客服代理不存在，请重新填写");
                default:
                    return JsonResult.fail("添加请求推送失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加客服代理失败！");
        }
    }

    @ApiOperation(value = "是否同意添加请求", notes = "根据双方的商家id")
    @PostMapping("approvalAddRelationship")
    public JsonResult approvalAddRelationship(@Valid @RequestBody ApprovalRelationshipRequestDto requestDto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
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
            switch (addCustomeragentFuseAction.isAgrentCustomeragentRequest(userId,requestDto.getpId(),requestDto.getSellerId(),requestDto.getState())){
                case 0:
                    return JsonResult.fail("你没有这个请求");
                case 2:
                    return JsonResult.fail("这个父商家不存在！");
                case 3:
                    return JsonResult.fail("这个商家不存在！");
                case 4:
                    return JsonResult.fail("这个商家未管理费，请叫他先交了管理费再拉他为你的客服代理！");
            }
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("同意添加请求操作失败！");
        }
    }

    @ApiOperation(value = "查询处理状态")
    @PostMapping("/queryState")
    public JsonResult queryState(@Valid @RequestBody QueryStateRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Integer res = addCustomeragentFuseAction.queryState(requestDto.getParentMerchant(), requestDto.getMerchantId());
            if (res < 3){
                return JsonResult.success().addResult("res", res);
            }
            return JsonResult.fail("查询处理状态失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询处理状态异常！");
        }
    }

    @ApiOperation(value = "定时清空商家每月拓展的商家数")
    @PostMapping("updateMonthNum")
    public JsonResult updateMonthNum(){
        try {
            merchantRegisterAction.operateMonthThing();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("定时清空商家每月拓展的商家数失败！");
        }
    }

    @ApiOperation(value = "定时清空商家每日拓展的商家数")
    @PostMapping("updateDayNum")
    public JsonResult updateDayNum(){
        try {
            boolean res = merchantAction.updateDayNum();
            return getResult(res,"定时清空商家每日拓展的商家数失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("定时清空商家每日拓展的商家数失败！");
        }
    }

    @ApiOperation(value = "根据用户id获取商家列表")
    @PostMapping("getSellerAgent")
    public JsonResult getSellerAgent(@Valid @RequestBody GetSellerAgentRequestDto dto){
        try {
            List<GetSellerAgentResponsetDto> res = merchantRegisterAction.getSellerAgent(dto);
            if (res != null){
                return JsonResult.success().addResult("res", res);
            }
            return JsonResult.fail("查询商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家列表失败！");
        }
    }

    @ApiOperation(value = "根据商家id获取二级商家列表")
    @PostMapping("getCustomerServiceAgent")
    public JsonResult getCustomerServiceAgent(@RequestParam("merchantId") String merchantId){
        try {
            List<GetCustomerServiceAgentResponsetDto> res = merchantRegisterAction.getCustomerServiceAgent(merchantId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取二级商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取二级商家列表失败！");
        }
    }

    @ApiOperation(value = "根据二级商家id获取三级商家列表")
    @PostMapping("getSellerList")
    public JsonResult getSellerList(@RequestParam("merchantId") String merchantId){
        try {
            List<GetSellerResponseDto> res= merchantRegisterAction.getMerchantList(merchantId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取三级商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取三级商家列表失败！");
        }
    }
}