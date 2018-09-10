package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.dto.userManagement.*;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.ucenter.biz.user.SystemBlacklistAction;
import com.netx.ucenter.biz.user.UserVerifyAction;
import com.netx.ucenter.vo.request.QueryUserVerifyListRequestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "用户管理模块")
@RequestMapping("/api/userManagement/")
public class UserManagementController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    private UserVerifyAction userVerifyService;
    @Autowired
    private SystemBlacklistAction systemBlacklistService;

    @ResponseBody
    @ApiOperation("查询用户认证列表")
    @PostMapping("selectUserVerifyList")
    public JsonResult selectUserVerifyList(@Valid @RequestBody QueryUserVerifyListRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map list = userVerifyService.selectUserVerifyList(request);
            return JsonResult.success().addResult("list",list.get("list"));
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询出现异常");
        }
    }

    @ResponseBody
    @ApiOperation("根据用户id查询用户请求认证内容")
    @GetMapping("selectUserVerify")
    public JsonResult selectUserVerify(String userId) {
        if(!StringUtils.hasText(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try {
            SelectUserVerifyResponse response =userVerifyService.selectUserVerify(userId);
            return JsonResult.success().addResult("result",response);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ResponseBody
    @ApiOperation(value = "根据认证id和用户id审核用户认证内容操作",
            notes = "支持批量通过认证操作且认证用户必须是同一对象，多个认证id以逗号隔开，若某认证里的用户id与传递的用户id不符合，会出现异常")
    @PostMapping("operateUserVerify")
    public JsonResult operateUserVerify(@Valid @RequestBody OperateUserVerifyRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(request.getStatus() == VerifyStatusEnum.REJECTIVE_AUTHENTICATION.getValue()){
            if(request.getReason() == null) {
                return JsonResult.fail("拒绝原因不能为空");
            }
        }
        try {
            return super.getResult(userVerifyService.operateUserVerify(request),"审核操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("认证操作异常");
        }
    }

    @ResponseBody
    @ApiOperation(value = "根据网号模糊并分页查询系统黑名单/白名单里的用户", notes="当网号为空串时，代表查询所有黑名单或白名单用户")
    @PostMapping(value = "queryUserInSystemBlacklist")
    public JsonResult queryUserInSystemBlacklist(@Valid @RequestBody SelectUserInSystemBlacklistRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            Map list = systemBlacklistService.selectUserInSystemBlacklist(request);
            return JsonResult.success().addResult("list",list.get("userList"));
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询出现异常!");
        }
    }
    @ResponseBody
    @ApiOperation( value = "根据用户id和操作类型对用户进行拉黑或释放处理", notes = "operateType:操作类型（0：释放，1：拉黑）")
    @PostMapping(value = "operateSystemBlacklist")
    public JsonResult operateSystemBlacklist(String userId, String createUserId, String reason, Integer operateType){
        if(!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户id不能为空");
        }
        if(!StringUtils.hasText(createUserId)){
            return JsonResult.fail("操作用户id不能为空");
        }
        if(!StringUtils.hasText(reason)) {
            return JsonResult.fail("操作原因不能为空");
        }
        if(operateType != 0 && operateType != 1) {
            return JsonResult.fail("操作类型不能为空");
        }
        try {
            return super.getResult(systemBlacklistService.operateSystemBlacklist(userId, createUserId, reason, operateType),"拉黑或释放操作失败，可能已被拉黑或释放");
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }
}
