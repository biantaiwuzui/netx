package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.ucenter.biz.user.UserOauthAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserOauth;
import com.netx.ucenter.vo.request.UserOauthRequest;
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

import javax.validation.Valid;

@Api(description = "第三方模块")
@RestController
@RequestMapping("/api/userOauth/")
public class UserOauthController extends BaseController{
    @Autowired
    UserOauthAction userOauthService;
    private Logger logger = LoggerFactory.getLogger(UserOauthController.class);

    @ApiOperation(value = "绑定或添加", notes = "操作第三方账号")
    @PostMapping(value = "add")
    public JsonResult add(@Valid @RequestBody UserOauthRequest request , BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if(!checkOtherUser(request.getUserId(),request.getOpenId(),request.getType())){
                switch (userOauthService.updateOauth(request.getUserId(),request.getOpenId(),request.getType())){
                    case 0:
                        return JsonResult.fail("绑定失败！");
                    case 1:
                        return JsonResult.success("绑定成功！");
                    case 2:
                        return JsonResult.success("改绑成功！");
                    default:
                        return JsonResult.fail("改绑失败！");
                }
            }
            return JsonResult.fail("操作失败！此账号已经被绑定");
        }catch (Exception e){
            logger.error("操作第三方账号异常："+e.getMessage(),e);
            return JsonResult.fail("操作第三方账号异常");
        }
    }

    @ApiOperation(value = "验证", notes = "验证第三方账号")
    @PostMapping(value = "check")
    public JsonResult check(@Valid @RequestBody UserOauthRequest request , BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(checkOtherUser(request.getUserId(),request.getOpenId(),request.getType()),"验证成功！","验证失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("验证第三方账号异常");
        }
    }

    private Boolean checkOtherUser(String id , String openId,Integer type) throws Exception{
        UserOauth userOauth = userOauthService.checkOtherUser(openId,type);
        return userOauth!=null && StringUtils.isNotBlank(userOauth.getUserId()) && userOauth.getUserId().equals(id);
    }}
