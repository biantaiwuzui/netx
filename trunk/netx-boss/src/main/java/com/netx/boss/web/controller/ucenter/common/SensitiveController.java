package com.netx.boss.web.controller.ucenter.common;


import com.netx.boss.web.controller.BaseController;
import com.netx.common.vo.common.*;
import com.netx.shopping.biz.ordercenter.HashCheckoutAction;
import com.netx.ucenter.biz.common.SensitiveAction;
import com.netx.ucenter.biz.common.SensitiveSuggestAction;
import com.netx.ucenter.model.common.CommonSensitive;
import com.netx.ucenter.model.common.CommonSensitiveSuggest;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/sensitive")
@Api(description = "过滤词管理")
public class SensitiveController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(SensitiveController.class);

    @Autowired
    private SensitiveAction sensitiveAction;
    @Autowired
    private SensitiveSuggestAction sensitiveSuggestAction;
    @Autowired
    HashCheckoutAction hashCheckoutAction;

    @PostMapping("/addSuggest")
    @ApiOperation(value = "添加过滤词建议", notes = "添加过滤词建议")
    public JsonResult addSensitiveSuggest(@Valid @RequestBody SensitiveAddRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = hashCheckoutAction.hashCheckout(requestDto.getSuggestUserId(), requestDto.getHash());
            if (!res) {
                return JsonResult.fail("机器无效提交订单");
            }
            String repeat = sensitiveSuggestAction.repeatSensitiveSuggest(requestDto);
            if(StringUtils.isNotBlank(repeat)){
                return JsonResult.fail("已有类似过滤词提交");
            }
            CommonSensitiveSuggest commonSensitiveSuggest = sensitiveSuggestAction.addSensitiveSuggest(requestDto);
            if(commonSensitiveSuggest != null){
                return JsonResult.success().addResult("res", commonSensitiveSuggest);
            }
            return JsonResult.fail("添加过滤词建议失败");
        } catch (Exception e) {
            logger.error("添加过滤词建议异常："+e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("添加过滤词建议异常");
        }
    }

    @ApiOperation(value = "分页查询已通过/未通过/全部审核列表", notes = "分页查询已通过/未通过/全部审核列表")
    @PostMapping("/auditList")
    public JsonResult auditList(@Valid @RequestBody SensitiveAuditListRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<QuerySensitiveSuggestListResponseDto> list = sensitiveSuggestAction.querySensitiveSuggestList(request);
            return JsonResult.success().addResult("list", list);
        } catch (Exception e) {
            logger.error("查询未审核列表异常："+e.getMessage(), e);
            return JsonResult.fail("查询未审核列表异常");
        }
    }

    @ApiOperation(value = "分页查询过滤词列表", notes = "分页查询过滤词列表")
    @PostMapping("/sensitiveList")
    public JsonResult auditList(@Valid @RequestBody PageRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<CommonSensitive> res = sensitiveAction.querySensitiveList(request.getCurrent(), request.getSize());
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error("查询敏感词列表异常："+e.getMessage(), e);
            return JsonResult.fail("查询敏感词列表异常");
        }
    }

    @ApiOperation(value = "超级管理员审核过滤词", notes = "超级管理员审核过滤词")
    @PostMapping("/audit")
    public JsonResult audit(@Valid @RequestBody SensitiveSuggestAuditRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean res = sensitiveSuggestAction.audit(request);
            if(res){
                return JsonResult.success();
            }
            return JsonResult.fail("审核关键词失败");
        } catch (Exception e) {
            logger.error("审核关键词异常："+e.getMessage(), e);
            return JsonResult.fail("审核关键词异常");
        }
    }

    @ApiOperation(value = "检验是否含有敏感词")
    @PostMapping("/filtering")
    public JsonResult filtering(@RequestBody Map<String,String> map){
        if(map==null || map.isEmpty()){
            return JsonResult.fail("检验敏感词不能为空！");
        }
        return JsonResult.success().addResult("result", sensitiveAction.checkValue(map));
    }

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
}
