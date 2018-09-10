package com.netx.boss.web.controller.ucenter;

import com.netx.ucenter.biz.user.UserAdminAction;
import com.netx.ucenter.biz.user.UserSuggestAction;
import com.netx.ucenter.vo.request.AddScoreInBossRequestDto;
import com.netx.ucenter.vo.request.QuerySuggestRequestDto;
import com.netx.ucenter.vo.response.AddSuggestPassDto;
import com.netx.ucenter.vo.response.ExamineSuggestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "用户建议")
@RestController
public class SuggestionsController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserAdminAction userAdminAction;
    @Autowired
    UserSuggestAction userSuggestAction;

    @ApiOperation(value = "获取用户建议列表")
    @PostMapping("/getUserSuggestList")
    public JsonResult getUsersuggestList(@Valid @RequestBody QuerySuggestRequestDto suggestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if(userSuggestAction.userSuggest(suggestDto).get("list")==null){
                return JsonResult.fail("暂时没有该用户的数据");
            }
            if(userSuggestAction.userSuggest(suggestDto)==null){
                return JsonResult.fail("暂时没有符合条件的用户信息");
            }
            else{
                return JsonResult.successJsonResult(userSuggestAction.userSuggest(suggestDto));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("获取用户建议列表异常！");
        }
    }

    @ApiOperation(value = "审批用户建议")
    @PostMapping("/suggest")
    public JsonResult suggest(@Valid @RequestBody ExamineSuggestDto examineSuggestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.success(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean success = userSuggestAction.suggest(examineSuggestDto, userAdminAction.queryAdmin(examineSuggestDto.getAuditUserName()).getId());
            if (success) {
                return JsonResult.success();
            }
            return JsonResult.fail("修改状态失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改状态异常！");
        }
    }

    @ApiOperation(value = "后台添加用户建议")
    @PostMapping("/addSuggestPass")
    public JsonResult addsuggestPass(@Valid @RequestBody AddSuggestPassDto addSuggestPassDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.success(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean success = userSuggestAction.addSuggestPass(addSuggestPassDto, userAdminAction.queryAdmin(addSuggestPassDto.getAuditUserName()).getId());
            if (success) {
                return JsonResult.success();
            }
            return JsonResult.fail("没有该用户!请重新输入网号或手机号码");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("后台添加建议异常！");
        }
    }

    @ApiOperation(value = "后台直接加分")
    @PostMapping("/addScoreInBoss")
    public JsonResult addScoreInBoss(@Valid @RequestBody AddScoreInBossRequestDto addScoreInBossRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.success(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean success = userSuggestAction.addScoreInBoss(addScoreInBossRequestDto);
            if (success) {
                return JsonResult.success();
            }
            return JsonResult.fail("没有该用户!请重新输入网号或手机号码");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("后台添加建议异常！");
        }
    }

    @ApiOperation(value = "后台加分密码判断")
    @PostMapping("/passwordCheck")
    public JsonResult passwordCheck(@RequestBody AddScoreInBossRequestDto addScoreInBossRequestDto){
        try{
            Boolean success=userSuggestAction.checkPassword(addScoreInBossRequestDto);
            if (success){
                return JsonResult.success();
            }
            return JsonResult.fail("密码错误！！！！！！");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("密码判断异常！");
        }
    }
}