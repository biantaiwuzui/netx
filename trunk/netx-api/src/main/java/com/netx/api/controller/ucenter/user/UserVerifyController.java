package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.user.IdNumberUserRequestDto;
import com.netx.common.user.dto.userManagement.UserVerifyBeanResponse;
import com.netx.common.user.dto.userVerify.*;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.enums.VerifyTypeEnum;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserVerifyAction;
import com.netx.ucenter.vo.request.WzMobileCodeRequest;
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

@Api(description = "用户认证模块")
@RestController
@RequestMapping("/api/UserVerify/")
public class UserVerifyController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(UserVerifyController.class);

    @Autowired
    private UserVerifyAction userVerifyAction;
    @Autowired
    private UserAction userAction;

    @ResponseBody
    @ApiOperation(value = "根据用户id查询用户认证内容",notes = "type：true查询用户待认证信息，false查询用户已认证信息")
    @GetMapping("selectUserVerify")
    public JsonResult selectUserVerify(String userId,Boolean type,HttpServletRequest request) {
        if(type==null){
            return JsonResult.fail("查询认证类型不能为空");
        }
        try {
            userId = getUserId(userId,request);
            if(userId==null){
                return JsonResult.fail("用户id不能为空");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try {
            UserVerifyBeanResponse response = userVerifyAction.getUserVerifyBeanResponse(userId,type);
            return JsonResult.success().addResult("result",response);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("postUserIdCardVerify")
    @ApiOperation("发送身份认证请求")
    public JsonResult postUserIdCardVerify(@Valid @RequestBody PostUserIdCardVerifyRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (!dto.getIdCardNumber().matches(RegularExpressionEnum.ID_NUMBER.getValue())) {
            return JsonResult.fail( "身份证号格式有误");
        }
        if(StringUtils.isBlank(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isBlank(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            switch (userVerifyAction.postUserIdCardVerify(dto)){
                case 0:
                    return JsonResult.fail("认证请求失败,可能的原因如下：" +
                            "1、认证已经通过 " +
                            "2、请求的认证尚在审核中");
                case 1:
                    return JsonResult.success();
                default:
                    return JsonResult.fail("该身份证已经被认证");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送认证请求出现异常");
        }
    }

    @ResponseBody
    @PostMapping("postUserVideoVerify")
    @ApiOperation("发送视频认证请求")
    public JsonResult postUserVideoVerify(@Valid @RequestBody PostUserVideoVerifyRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isBlank(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isBlank(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userVerifyAction.postUserVideoVerify(dto),"发送认证请求失败,可能认证已经通过，或者上次的认证请求还在审核中");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送认证请求出现异常");
        }
    }

    @ResponseBody
    @PostMapping("postUserCarVerify")
    @ApiOperation("发送车辆认证请求")
    public JsonResult postUserCarVerify(@Valid @RequestBody PostUserCarVerifyRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isBlank(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isBlank(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            switch (userVerifyAction.postUserCarVerify(dto)){
                case 0:
                    return JsonResult.fail("发送认证请求失败,可能认证已经通过，或者上次的认证请求还在审核中");
                case 1:
                    return JsonResult.success();
                default:
                    return JsonResult.fail("发送认证请求失败,该车牌已经被申请认证或已经被认证");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送认证请求出现异常");
        }
    }

    @ResponseBody
    @PostMapping("postUserHouseVerify")
    @ApiOperation("发送房产认证请求")
    public JsonResult postUserHouseVerify(@Valid @RequestBody PostUserHouseRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isBlank(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isBlank(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userVerifyAction.postUserHouseVerify(dto),"发送认证请求失败,可能认证已经通过，或者上次的认证请求还在审核中");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送认证请求出现异常");
        }
    }

    @ResponseBody
    @PostMapping("postUserDegreeVerify")
    @ApiOperation("发送学历认证请求")
    public JsonResult postUserDegreeVerify(@Valid @RequestBody PostUserDegreeRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isBlank(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isBlank(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userVerifyAction.postUserDegreeVerify(dto),"发送认证请求失败,可能认证已经通过，或者上次的认证请求还在审核中");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送认证请求出现异常");
        }
    }

    @ResponseBody
    @PostMapping("operateUserMobileVerify")
    @ApiOperation(value = "绑定手机操作，验证验证码（请求短信验证码接口在手机验证码模块）",
            notes = "返回值：-1:该号码已被注册；0:手机验证失败； 1:验证成功； 2:验证码过期； 3:验证码已经被验证过，请重新获取验证码； 其他:你暂无验证码。另外，注意：这里的userId不能为空!!!")
    public JsonResult operateUserMobileVerify(@Valid @RequestBody WzMobileCodeRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail("用户id不能为空");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            int statusCode = userVerifyAction.operateUserMobileVerify(dto,userId);
            return JsonResult.success().addResult("result",statusCode);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("绑定异常");
        }
    }

    @ResponseBody
    @PostMapping("matchUserVerify")
    @ApiOperation(value = "认证用户身份信息", notes = "若身份证号为空，则说明不需要认证身份证。若成功认证：返回userId")
    public JsonResult matchUserVerify(@RequestBody @Valid IdNumberUserRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isNotBlank(dto.getIdNumber())) {//如果有数据，则校验，没有数据，说明不需要进行身份证认证。
            if (!dto.getIdNumber().matches(RegularExpressionEnum.ID_NUMBER.getValue())) {
                return JsonResult.fail("身份证号格式有误");
            }
        }
        try{
            return JsonResult.successJsonResult(userVerifyAction.matchUserVerify(dto.getRealName(), dto.getMobile(), dto.getUserNumber(), dto.getIdNumber()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("认证异常："+e.getMessage());
        }
    }

    @PostMapping("verifyUserSetting")
    @ApiOperation(value = "认证用户的设置", notes = "礼物：wzType=1，邀请：wzType=2")
    public JsonResult verifyUserSetting(String fromUserId, String toUserId, Integer wzType) {
        try {
            return super.getResult(userAction.verifyUserSetting(fromUserId, toUserId, wzType),"对方做了相应的设置，你没有权限进行此操作");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @GetMapping("getVerify")
    @ApiOperation(value = "查看认证信息")
    public JsonResult getVerify(VerifyTypeEnum typeEnum,HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail( "授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(typeEnum==null){
            return JsonResult.fail("查询类型不能为空");
        }
        try {
            return JsonResult.success().addResult("result",userVerifyAction.getVerifyResponse(userId,typeEnum));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }
}
