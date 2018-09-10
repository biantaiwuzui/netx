package com.netx.api.controller.worth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.invitation.InvitationConfirmDto;
import com.netx.common.wz.dto.invitation.InvitationSendDto;
import com.netx.common.wz.dto.invitation.InvitationSuggestDto;
import com.netx.common.wz.vo.invitation.InvitationListVo;
import com.netx.fuse.biz.worth.InvitationFuseAction;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.common.InvitationAction;
import com.netx.worth.model.Invitation;
import com.netx.worth.util.PageWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@Api(description = "邀请模块")
@RequestMapping("/wz/invitation")
@RestController
public class InvitationController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(InvitationController.class);
    @Autowired
    private InvitationAction invitationAction;
    @Autowired
    private InvitationFuseAction invitationFuseAction;

    @ApiOperation(value = "我收到的邀请", response = InvitationListVo.class)
    @PostMapping(value = "/receiveList", consumes = {"application/json"})
    public JsonResult receiveList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        //List<Invitation> list = null;
        Map map = null;
        Page<Invitation> page = new Page(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        try {
            map = invitationFuseAction.getUserReceiveInvitation(commonPageDto.getUserId(), page);
            return JsonResult.success().addResult("map",PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我收到的邀请失败");
        }
    }

    @ApiOperation(value = "我发出的邀请", response = InvitationListVo.class)
    @PostMapping(value = "/sendList", consumes = {"application/json"})
    public JsonResult sendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        //List<Invitation> list = null;
        Map map = null;
        Page<Invitation> page = new Page(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        try {
            map = invitationFuseAction.getUserSendInvitation(commonPageDto.getUserId(), page);
            return JsonResult.success().addResult("map",PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我发出的邀请失败");
        }
    }

    @ApiOperation(value = "我发出的邀请数量")
    @PostMapping(value = "/sendListCount")
    public JsonResult sendListCount(HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        Integer count = 0;
        try {
            count = invitationAction.getSendCountByFromUserId(userId);
            return JsonResult.success().addResult("count", count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我发出的邀请数量失败");
        }
    }

    @ApiOperation(value = "接受邀请")
    @PostMapping(value = "/accept")
    public JsonResult accept(String invitationId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(invitationId)) {
            return JsonResult.fail("邀请ID不能为空");
        }
        boolean success = false;
        try {
            success = invitationAction.accept(userId, invitationId);
            if(success){
                Boolean code = invitationAction.updateCode(invitationId);
                return JsonResult.success().addResult("success", code);
            }
            return JsonResult.fail("接受邀请失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("接受邀请失败");
        }
    }

    @ApiOperation(value = "拒绝邀请，注意必须先调用其它模块的退款方法，退款成功方可拒绝。")
    @PostMapping(value = "/refuse")
    public JsonResult refuse(String invitationId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(invitationId)) {
            return JsonResult.fail("邀请ID不能为空");
        }
        boolean success = false;
        try {
            success = invitationAction.refuse(userId, invitationId);
            return getResult(success, "拒绝邀请失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("拒绝邀请失败");
        }
    }

    @ApiOperation(value = "发出邀请，注意：需要满足两个条件", notes = "必须满足以下两个条件才能调用该接口：1.先调用认证用户设置接口判断是否有权限邀请 2.支付成功后")
    @PostMapping(value = "/send", consumes = {"application/json"})
    public JsonResult send(@Validated @RequestBody InvitationSendDto invitationSendDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (!invitationSendDto.getOnline()) {
            if (!StringUtils.hasText(invitationSendDto.getAddress())) {
                return JsonResult.fail("用户地址不能为空！");
            }
        }

        boolean success = false;
        try {
            success = invitationFuseAction.send(invitationSendDto);
            return getResult(success, "发出邀请失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发出邀请失败");
        }
    }

    @ApiOperation(value = "查看邀请")
    @PostMapping(value = "/detail")
    public JsonResult detail(String invitationId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(invitationId)) {
            return JsonResult.fail("邀请ID不能为空");
        }
        try {
            Map<String, Object> map = invitationFuseAction.detail(userId, invitationId);
            if (map != null) {
                return JsonResult.success().addResult("map",map);
            } else {
                return JsonResult.fail("查看邀请失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看邀请失败");
        }
    }

    @ApiOperation(value = "邀请建议")
    @PostMapping(value = "/suggest", consumes = {"application/json"})
    public JsonResult suggest(@Validated @RequestBody InvitationSuggestDto invitationSuggestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = invitationFuseAction.suggest(invitationSuggestDto);
            return getResult(success, "邀请建议失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("邀请建议失败");
        }
    }


    @ApiOperation(value = "邀请方最终确认")
    @PostMapping(value = "/confirm", consumes = {"application/json"})
    public JsonResult confirm(@Validated @RequestBody InvitationConfirmDto invitationConfirmDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Invitation invitation = invitationAction.selectById(invitationConfirmDto.getInvitationId());
            if (!invitation.getOnline()) {
                if (!StringUtils.hasText(invitationConfirmDto.getAddress())) {
                    return JsonResult.fail("用户地址不能为空！");
                }
            }
            boolean success = invitationAction.confirm(invitationConfirmDto);
            return getResult(success, "邀请方最终确认的请求失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("邀请方最终确认的请求失败");
        }
    }

    @ApiOperation(value = "邀请人输入验证码")
    @PostMapping(value = "/verificationCode")
    public JsonResult verificationCode(String id, Integer code, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(id)) {
            return JsonResult.fail("邀请ID不能为空");
        }
        if (code == null) {
            return JsonResult.fail("验证码不能为空");
        }
        try {
            Invitation invitation = invitationAction.selectById(id);
            if (invitation.getTimes() >= 3) {
                return JsonResult.fail("重试次数过多");
            }
            if (!invitation.getCode().equals(code)) {
                invitationAction.updateVerificationCode(userId, id, invitation.getTimes() + 1);
                return JsonResult.fail("验证码错误");
            } else {
                boolean success = invitationAction.startInvitation(userId, id);
                return getResult(success, "邀请人输入验证码的请求失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("邀请人输入验证码的请求失败");
        }
    }

    @ApiOperation(value = "验证距离")
    @PostMapping(value = "/check", consumes = {"application/json"})
    public JsonResult check(@Validated @RequestBody CommonCheckDto commonCheckDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = invitationAction.check(commonCheckDto);
            return getResult(success, "验证距离失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("验证距离失败");
        }
    }
    
    @ApiOperation(value = "检查这个用户是否有未完成的邀请")
    @PostMapping(value = "/checkHasUnComplete", consumes = {"application/json"})
    public JsonResult checkHasUnComplete(String userId) {
    	if (!StringUtils.hasText(userId)) {
    		return JsonResult.fail("用户ID不能为空");
    	}
    	try {
    		boolean hasUnComplete = invitationAction.checkHasUnComplete(userId);
    		return JsonResult.success().addResult("hasUnComplete",hasUnComplete);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		return JsonResult.fail("检查这个用户是否有未完成的邀请失败");
    	}
    }

    @ApiOperation(value = "定时任务：验证码、距离是否通过,startAt+30m触发")
    @PostMapping(value = "/checkSuccess")
    public JsonResult checkSuccess(String invitationId) {
        if (!StringUtils.hasText(invitationId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            invitationAction.checkSuccess(invitationId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：验证码、距离是否通过失败");
        }
    }

    @ApiOperation(value = "定时任务：是否评价, endAt+24h触发")
    @PostMapping(value = "/checkEvaluate")
    public JsonResult checkEvaluate(String invitationId) {
        if (!StringUtils.hasText(invitationId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
        	invitationFuseAction.checkEvaluate(invitationId);
        	return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：是否评价失败");
        }
    }


    @ApiOperation(value = "我收到的邀请总数")
    @PostMapping(value = "/receiveCount")
    public JsonResult receiveCount(HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        int count = 0;
        try {
            count = invitationAction.getReceiveCount(userId);
            return JsonResult.success().addResult("count", count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询我收到的邀请总数失败");
        }
    }



}
