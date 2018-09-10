package com.netx.boss.web.controller;

import com.netx.boss.components.UserTokenState;
import com.netx.boss.web.security.JwtAuthenticationRequest;
import com.netx.boss.web.security.TokenHelper;
import com.netx.ucenter.biz.user.UserAdminAction;
import com.netx.ucenter.model.user.UserAdmin;
import com.netx.ucenter.vo.request.*;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * Created by CloudZou on 3/7/2018.
 */
@Api(description = "Bossd登录")
@RestController
@RequestMapping( value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE )
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private UserAdminAction userAdminAction;

    @ApiOperation(value = "boss系统登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException, IOException {
        try {
            UserAdmin userAdmin = userAdminAction.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            if(userAdmin == null){
                return JsonResult.fail("账号或密码错误！");
            }
            String jws = tokenHelper.generateToken( authenticationRequest.getUsername(), device);
            int expiresIn = tokenHelper.getExpiredIn(device);
            // Return the token
            return JsonResult.success().addResult("token", new UserTokenState(jws, expiresIn));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("登录异常");
        }
    }

    @ApiOperation(value = "添加用户管理员用户", notes = "添加用户管理员用户")
    @PostMapping(value = "/addUserAdmin")
    public JsonResult add(@Valid @RequestBody AddUserAdminRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            UserAdmin userAdmin = userAdminAction.addUserAdmin(requestDto);
            if(userAdmin == null){
                return JsonResult.fail("添加失败！");
            }else if (StringUtils.isBlank(userAdmin.getUserName())){
                return JsonResult.fail("该用户名已存在！");
            }else if (StringUtils.isBlank(userAdmin.getCreateUserName())){
                return JsonResult.fail("超级管理员才能添加管理员！");
            }
            return JsonResult.success().addResult("res", userAdmin);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加异常！");
        }
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping(value = "/updatePassword")
    public JsonResult updatePassword(@Valid @RequestBody UpdatePasswordRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            UserAdmin userAdmin = userAdminAction.updatePassword(requestDto);
            if(userAdmin == null){
                return JsonResult.fail("修改密码失败！");
            }else if (StringUtils.isBlank(userAdmin.getUserName())){
                return JsonResult.fail("用户不存在！");
            }else if (StringUtils.isBlank(userAdmin.getPassword())){
                return JsonResult.fail("密码错误！");
            }
            return JsonResult.success().addResult("res", userAdmin);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加异常！");
        }
    }

    @ApiOperation(value = "重置管理员密码", notes = "重置管理员密码")
    @PostMapping(value = "/resetPassword")
    public JsonResult resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String res = userAdminAction.resetPassword(requestDto);
            if(StringUtils.isBlank(res)){
                return JsonResult.success();
            }
            return JsonResult.fail(res);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("重置密码异常！");
        }
    }

    @ApiOperation(value = "管理员列表", notes = "管理员列表")
    @PostMapping(value = "/queryUserAdmins")
    public JsonResult queryUserAdmins(@Valid @RequestBody QueryUserAdminListRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String, Object> map = userAdminAction.queryUserAdminList(requestDto);
            return JsonResult.success().addResult("map", map);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询管理员列表异常！");
        }
    }

    @ApiOperation(value = "禁用/解除禁用管理员", notes = "管理员列表")
    @PostMapping(value = "/delete")
    public JsonResult delete(@Valid @RequestBody DeleteUserAdminRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String res = userAdminAction.deleteById(requestDto.getId(), requestDto.getUserName(), requestDto.getReason());
            if(StringUtils.isBlank(res)){
                return JsonResult.success();
            }
            return JsonResult.fail(res);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作异常！");
        }
    }

    @ApiOperation(value = "刷新")
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal
    ) {

        String authToken = tokenHelper.getToken( request );

        Device device = DeviceUtils.getCurrentDevice(request);

        if (authToken != null && principal != null) {

            // TODO check user password last update
            String refreshedToken = tokenHelper.refreshToken(authToken, device);
            int expiresIn = tokenHelper.getExpiredIn(device);

            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }

}
