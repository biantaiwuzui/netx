package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.vo.message.GroupMembersDto;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.common.PageAndStateRequestDto;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.vo.request.JMssageHistoryRequestDto;
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

/**
 * Create by wongloong on 17-8-23
 */
@Api(description = "极光推送api")
@RestController
@RequestMapping("/api/jpush")
public class PushMessageController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(PictureController.class);
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private UserAction userAction;

    @ApiOperation(value = "发送消息到所有用户")
    @PostMapping("/sendAll")
    public void sendAll(@Valid @RequestBody String msg) {
        try {
            messagePushAction.pushMessageForAll(msg);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @ApiOperation(value = "发送消息到用户id")
    @PostMapping("/sendByAlias")
    public void sendByAlias(@RequestBody JpushDto jpushDto){
        try {
            messagePushAction.sendMessageAlias(jpushDto);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

    }

    @ApiOperation(value = "更新群组成员")
    @PostMapping("/updateGroupMembers")
    public JsonResult updateGroupMembers(@RequestBody GroupMembersDto groupMembersDto) {
        try {
            messagePushAction.pushIdToGroup(groupMembersDto.getGroupId(),groupMembersDto.getUserId());
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更新群成员异常");
        }
    }

    @ApiOperation(value = "定时发送推送信息")
    @PostMapping("/sendMessage")
    public JsonResult sendMessage() {
        try {
            messagePushAction.sendMessage();
            return JsonResult.success();
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送异常");
        }
    }

    @ApiOperation(value = "极光注册")
    @PostMapping("/addUser")
    public JsonResult addUser(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId, request);
            if(userId==null||userId.isEmpty()){
                return JsonResult.fail("用户不能为空");
            }
            String password = messagePushAction.addUser(userId);
            if(password==null){
                return JsonResult.fail("注册极光失败");
            }
            User user = userAction.getUserService().selectById(userId);
            user.setRegJMessage(true);
            user.setJmessagePassword(password);
            userAction.getUserService().updateById(user);
            return JsonResult.success("注册极光成功").addResult("password",password);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注册异常");
        }
    }

    @ApiOperation(value = "获取动态信息",notes = "获取消息记录")
    @PostMapping("/getPushMessage")
    public JsonResult getPushMessage(@Valid @RequestBody PageAndStateRequestDto dto,BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try{
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isBlank(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
//            return JsonResult.successJsonResult(wzCommonImHistoryAction.getHistoryMessage(dto.getNetxType(),userGeo.getLon(),userGeo.getLat(),userGeo.getUserId(),dto.getTypeEnum(),dto.getCurrent(),dto.getSize()));
            return JsonResult.successJsonResult(wzCommonImHistoryAction.getHistoryMessage(dto.getNetxType(),userGeo.getLon(),userGeo.getLat(),userGeo.getUserId(),dto.getTypeEnums(),dto.getCurrent(),dto.getSize()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取动态异常");
        }
    }

    @ApiOperation(value = "把消息改为已读",notes = "消息状态")
    @PostMapping("/updateRead")
    public JsonResult getPushMessage(String id, HttpServletRequest request) {

        String userId = null;
        try{
            userId = getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            wzCommonImHistoryAction.updateRead(id,userId);
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取动态异常");
        }
    }
}
