package com.netx.api.controller.worth;

import java.util.List;
import java.util.Map;
import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.wz.dto.meeting.*;
import com.netx.fuse.biz.worth.MeetingFuseAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.utils.json.ApiCode;
import com.netx.worth.biz.common.IndexAction;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.common.CommonPayCallbackDto;
import com.netx.common.wz.dto.common.CommonSearchDto;
import com.netx.common.wz.vo.meeting.MeetingListVo;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.meeting.MeetingAction;
import com.netx.worth.enums.PublishStatus;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;
import com.netx.worth.util.PageWrapper;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;

@Api(description = "活动模块")
@RequestMapping("/wz/meeting")
@RestController
public class MeetingController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(MeetingController.class);
    @Autowired
    private MeetingAction meetingAction;
    @Autowired
    private MeetingFuseAction meetingFuseAction;
    @Autowired
    private IndexAction indexAction;
    @Autowired
    private ScoreAction scoreAction;

    @ApiOperation(value = "活动详情")
    @GetMapping(value = "/detail")
    public JsonResult detail(String meetingId, HttpServletRequest request) {
        if (StringUtils.isEmpty(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String,Object> map = meetingFuseAction.selectById(meetingId,userGeo.getLon(),userGeo.getLat(),userGeo.getUserId());
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看活动详情失败："+e.getMessage());
        }

    }


    @ApiOperation(value = "我附近的聚会")
    @PostMapping(value = "/nearHasManyList", consumes = {"application/json"})
    public JsonResult nearHasManyList(@Validated @RequestBody CommonSearchDto commonSearchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (commonSearchDto.getLength() == null) return JsonResult.fail("length距离不能为空");
        Page<Meeting> page = new Page<>(commonSearchDto.getCurrentPage(), commonSearchDto.getSize());
        Map<String, List> map = null;
        try {
            map = meetingFuseAction.nearHasManyList(commonSearchDto.getUserId(), commonSearchDto.getLon(), commonSearchDto.getLat(), commonSearchDto.getLength(), page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我附近的聚会失败");
        }
    }


    @ApiOperation(value = "我附近的活动")
    @PostMapping(value = "/nearHasOneList", consumes = {"application/json"})
    public JsonResult nearHasOneList(@Validated @RequestBody CommonSearchDto commonSearchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (commonSearchDto.getLength() == null) return JsonResult.fail("length距离不能为空");
        Page<Meeting> page = new Page<>(commonSearchDto.getCurrentPage(), commonSearchDto.getSize());
        Map<String, List> map = null;
        try {
            map = meetingAction.nearHasOneList(commonSearchDto.getUserId(), commonSearchDto.getLon(), commonSearchDto.getLat(), commonSearchDto.getLength(), page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询我附近的活动失败");
        }
    }


    @ApiOperation(value = "我报名的活动", response = MeetingListVo.class)
    @PostMapping(value = "/regList", consumes = {"application/json"})
    public JsonResult receiveList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Page<MeetingRegister> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = meetingFuseAction.getUserRegMeetings(commonPageDto.getUserId(),userGeo.getLat(),userGeo.getLon(), page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我报名的活动失败");
        }
    }


    @ApiOperation(value = "我发起的活动", response = MeetingListVo.class)
    @PostMapping(value = "/sendList", consumes = {"application/json"})
    public JsonResult sendList(@Validated @RequestBody CommonPageDto commonPageDto,HttpServletRequest request,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Page<MeetingSend> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = meetingFuseAction.getUserSendMeeting(commonPageDto.getUserId(),userGeo.getLat(),userGeo.getLon(),page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我发起的活动失败");
        }
    }


    @ApiOperation(value = "我发起的活动(按状态排序)", response = MeetingListVo.class)
    @PostMapping(value = "/userSendList", consumes = {"application/json"})
    public JsonResult userSendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page<MeetingSend> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = meetingAction.getUserSendList(commonPageDto.getUserId(), page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我发起的活动(按状态排序)失败");
        }
    }


    @ApiOperation(value = "查看活动人员，sendList：发布者列表。regList：报名者列表", response = MeetingListVo.class)
    @PostMapping(value = "/meetingPerson", consumes = {"application/json"})
    public JsonResult meetingPerson(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Page<MeetingSend> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = meetingFuseAction.meetingPerson(commonPageDto.getId(),page,userGeo.getLon(),userGeo.getLat(),userGeo.getUserId());
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看活动人员失败");
        }
    }


    @ApiOperation(value = "发布活动")
    @PostMapping(value = "/send", consumes = { "application/json" })
    public JsonResult send(@Validated @RequestBody SendMeetingDto sendMeetingDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            sendMeetingDto.setUserId(super.getUserId(request));
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            PublishStatus publishStatus = meetingFuseAction.send(sendMeetingDto);
            switch (publishStatus) {
                case HASUNCOMPLETE:
                    return JsonResult.fail(publishStatus.getDescription());
                case SAME:
                    return JsonResult.fail(publishStatus.getDescription());
                case FAIL:
                    return JsonResult.fail(publishStatus.getDescription());
                case SUCCESS:
                    scoreAction.addScore(sendMeetingDto.getUserId(), StatScoreEnum.SS_PUBLISH_WORTH);
                    return JsonResult.success(sendMeetingDto.getId());
            }
        } catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现了一个预期之外的错误(￣▽￣)");
        }
        return JsonResult.success();
    }


    @ApiOperation(value = "编辑活动")
    @PostMapping(value = "/edit", consumes = {"application/json"})
    public JsonResult edit(@Validated @RequestBody SendMeetingDto sendMeetingDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            PublishStatus publishStatus = meetingFuseAction.edit(sendMeetingDto,userGeo);
            switch (publishStatus){
                case SAME: return JsonResult.fail(publishStatus.getDescription());
                case SUCCESS: return JsonResult.success().addResult("id",sendMeetingDto.getId());
                default: return JsonResult.fail("编辑活动失败");
            }
        } catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑活动失败");
        }
    }


    @ApiOperation(value = "需我处理的联合发起列表（同意、拒绝）")
    @PostMapping(value = "/unionSendList", consumes = {"application/json"})
    public JsonResult unionSendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Page<MeetingSend> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = meetingFuseAction.unionSendList(userGeo.getUserId(),userGeo.getLat(),userGeo.getLon(), page);
            return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询需我处理的联合发起列表失败");
        }
    }


    @ApiOperation(value = "同意联合发起")
    @PostMapping(value = "/unionAccept")
    public JsonResult unionAccept(HttpServletRequest request, String meetingId) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = meetingAction.unionAccept(userGeo.getUserId(), meetingId);
            return JsonResult.success().addResult("status",success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("程序跑歪了，未处理成功(✿◡‿◡)");
        }
    }


    @ApiOperation(value = "拒绝联合发起")
    @PostMapping(value = "/unionRefuse")
    public JsonResult unionRefuse(HttpServletRequest request, String meetingId) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = meetingAction.unionRefuse(userGeo.getUserId(), meetingId);
            return JsonResult.success().addResult("status",success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("哎呀，没处理成功，手抖手抖");
        }
    }


    @ApiOperation(value = "活动报名，需支付完毕后再调用此接口，但首先要看该活动的状态是否为：0")
    @PostMapping(value = "/register", consumes = {"application/json"})
    public JsonResult register(@Validated @RequestBody RegMeetingDto regMeetingDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = meetingFuseAction.register(regMeetingDto,userGeo.getUserId());
            if(success){
                //添加最新报名活动的记录
                indexAction.addHistory(WorthTypeEnum.MEETING_TYPE,userGeo.getUserId(),regMeetingDto.getMeetingId());
                return JsonResult.success();
            }
            return JsonResult.fail("未知的错误！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }


//    @ApiOperation(value = "补充活动信息（地址、消费））")
//    @PostMapping(value = "/modify", consumes = {"application/json"})
//    public JsonResult modify(@Validated @RequestBody ModifyMeetingDto modifyMeetingDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        try {
//            boolean success = meetingFuseAction.modify(modifyMeetingDto);
//            return getResult(success, "补充活动信息失败");
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return JsonResult.fail("补充活动信息失败");
//        }
//    }


    @ApiOperation(value = "补足成功时的回调方法。注意：1.如果无需补差额，报名费本身就够的情况下不要调用。2.如果要补差额，那么必须是补足支付成功后才能调用此方法！！！")
    @PostMapping(value = "/payBalanceCallback")
    public JsonResult payBalanceCallback(String id, String userId) {
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(id)) {
            return JsonResult.fail("活动ID不能为空");
        }
        try {
            boolean success = meetingAction.payBalanceCallback(id, userId);
            return getResult(success, "成功补足差额!","已成功补足差额!,不可再补足!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("补足成功时的回调方法调用失败!!");
        }
    }


    @ApiOperation(value = "发起人取消活动")
    @PostMapping(value = "/cancelSend")
    public JsonResult cancelSend(String meetingId, HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        try {
            boolean success = meetingFuseAction.cancelSend(meetingId, userGeo.getUserId());
            return getResult(success, "发起人取消活动失败");
        } catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发起人取消活动失败");
        }
    }


    @ApiOperation(value = "报名者退出活动")
    @PostMapping(value = "/cancelReg")
    public JsonResult cancelReg(String meetingId, HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        try {
            boolean success = meetingFuseAction.exitMeeting(meetingId, userGeo.getUserId());
            return getResult(success, "参与人取消活动失败");
        }catch (RuntimeException r){
            logger.error(r.getMessage(), r);
            return JsonResult.fail(r.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("参与人取消活动失败");
        }
    }


    @ApiOperation(value = "确认活动细节")
    @PostMapping(value = "/confirmDetail")
    public JsonResult confirmDetail(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        try {
            boolean confirm = meetingAction.confirmDetail(meetingId);
            return getResult(confirm,"确认活动细节失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("确认活动细节失败");
        }
    }


    @ApiOperation(value = "发起人同意开始活动")
    @PostMapping(value = "/sendAccept")
    public JsonResult sendAccept(String meetingId, HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        try {
            Map<String,Object> map =null;
            map = meetingAction.sendAccept(meetingId, userGeo.getUserId(), userGeo.getLat(), userGeo.getLon());
            if (map != null) {
                return JsonResult.successJsonResult(map);
            } else {
                return JsonResult.fail("发起人同意开始活动，分发验证码失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "/getCode")
    public JsonResult getCode(String meetingId,HttpServletRequest request) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.successJsonResult(meetingAction.getCode(meetingId, userGeo.getUserId(),userGeo.getLat(),userGeo.getLon()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "确认出席，准备校验验证码")
    @PostMapping(value = "/registerStart")
    public JsonResult registerStart(String meetingId,HttpServletRequest request) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("活动ID不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(userGeo.getUserId())) {
            return JsonResult.fail("用户ID不能为空");
        }
        try {
            boolean success = meetingAction.registerStart(meetingId, userGeo.getUserId());
            return getResult(success, "确认出席，准备校验验证码的请求失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("确认出席，准备校验验证码的请求失败");
        }
    }


    @ApiOperation(value = "校验验证码")
    @PostMapping(value = "/verificationCode")
    public JsonResult verificationCode(@RequestParam("meetingId") String meetingId,@RequestParam("code") Integer code,@RequestParam("sendUserId") String sendUserId,HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(userGeo.getUserId())) {
            return JsonResult.fail("请输入后再操作");
        }
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            boolean success = meetingFuseAction.checkCode(meetingId,userGeo.getUserId(),sendUserId,code,userGeo.getLat(),userGeo.getLon());
            return getResult(success,"校验成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "提醒商家提供服务")
    @PostMapping(value = "/remindMerchants")
    public JsonResult remindMerchants(String meetingId,HttpServletRequest request){
        if(meetingId == null){
            return JsonResult.fail("活动Id不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            boolean success = false;
            success = meetingFuseAction.getRemindMerchants(meetingId,userGeo.getUserId());
            return getResult(success,"提醒成功","找不到该活动的商家id");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "邀请好友参加活动")
    @PostMapping(value = "/inviteFriends")
    public JsonResult inviteFriends(String meetingId,String friends,HttpServletRequest request){
        if(friends == null){
            return JsonResult.fail("活动Id不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            boolean success = false;
            success = meetingFuseAction.getInviteFriends(meetingId,userGeo.getUserId(),friends);
            return getResult(success,"邀请成功");
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation("支付后回调")
    @PostMapping(value = {"/payCallback"}, consumes = {"application/json"})
    public JsonResult payCallback(@Validated @RequestBody CommonPayCallbackDto commonPayCallbackDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = this.meetingFuseAction.payCallback(commonPayCallbackDto,userGeo.getUserId());
            return getResult(success, "支付失败");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return JsonResult.fail("支付失败");
        }
    }


    @ApiOperation(value = "检查这个用户是否有未完成的聚会")
    @PostMapping(value = "/checkHasUnComplete", consumes = {"application/json"})
    public JsonResult checkHasUnComplete(HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(userGeo.getUserId())) {
            return JsonResult.fail("请登陆后再查看");
        }
        try {
            boolean hasUnComplete = meetingAction.checkHasUnComplete(userGeo.getUserId());
            return JsonResult.success().addResult("hasUncomplete", hasUnComplete);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("检查这个用户是否有未完成的聚会失败");
        }
    }


    @ApiOperation(value = "定时任务：确定入选人，regStopAt触发")
    @PostMapping(value = "/checkRegisterSuccess")
    public JsonResult checkRegisterSuccess(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.SureSelected(meetingId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：确定入选人失败");
        }
    }


    @ApiOperation(value = "定时任务：检查发布人是否同意开始活动，startAt触发")
    @PostMapping(value = "/checkPublishStart")
    public JsonResult checkPublishStart(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.checkPublishStart(meetingId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：检查发布人是否同意开始活动失败");
        }
    }


    @ApiOperation(value = "定时任务：验证码、距离是否通过,end_at触发")
    @PostMapping(value = "/checkSuccess")
    public JsonResult checkSuccess(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingAction.checkSuccess(meetingId);
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("启动定时任务：验证是否通过失败");
        }
    }


    @ApiOperation(value = "定时任务：是否评价")
    @PostMapping(value = "/checkEvaluate")
    public JsonResult checkEvaluate(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.checkEvaluate((meetingId));
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "定时任务：检查确定入选人条件")
    @PostMapping(value = "/checkMeetingRegisterCount")
    public JsonResult checkMeetingRegisterCount(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.checkSureSelected(meetingId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：检查启动入选人条件失败");
        }
    }


    @ApiOperation(value = "检查某一时间段内是否可以发布活动")
    @PostMapping(value = "/CheckIfSendMeeting")
    public JsonResult CheckIfSendMeeting(Long registerAt, Long startedAt,Long endAt,String id,HttpServletRequest request){
        UserGeo userGeo = null;
        try{
            userGeo = getGeoFromRequest(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if(startedAt == null){
            return JsonResult.fail("活动开始时间不能为空");
        }
        if(endAt == null){
            return JsonResult.fail("活动结束时间不能为空");
        }
        try{
            Object success = meetingAction.checkHasUnComplete(userGeo.getUserId(),registerAt,startedAt,endAt,id);
            return JsonResult.success().addResult("isSend",success);
        }catch (Exception e){
            logger.error((e.getMessage()));
            return JsonResult.fail(e.getMessage());
        }
    }


    @ApiOperation(value = "定时任务：检查报名费用是否足够(推送), regStopAt触发")
    @PostMapping(value = "/checkAmountEnough")
    public JsonResult checkAmountEnough(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.checkAmountEnough(meetingId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("检查报名费用是否足够的程序异常");
        }
        return JsonResult.success();
    }


    @ApiOperation(value = "定时任务：检查开始前半小时是否确认细节")
    @PostMapping(value = "/checkConfirmDetail")
    public JsonResult checkConfirmDetail(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.checkConfirmDetail(meetingId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("检查是否确认细节异常");
        }
        return JsonResult.success();
    }


    @ApiOperation(value = "定时任务：没有确认细节导致活动关闭，报名者的费用全部退回，并推送提醒所有用户")
    @PostMapping(value = "/checkNoConfirmDetail")
    public JsonResult checkNoConfirmDetail(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            meetingFuseAction.checkNoConfirmDetail(meetingId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("你有活动未确认细节结果处理异常");
        }
        return JsonResult.success();
    }


    @ApiOperation(value = "定时任务：活动结束时间自动结算")
    @PostMapping(value = "/autoMeetingSettle")
    public JsonResult autoMeetingSettle(String meetingId) {
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        try {
            boolean success = meetingFuseAction.autoMeetingSettle(meetingId);
            if (success) {
                return JsonResult.success();
            }
            return getResult(success,"你有活动未结算成功，请及时客服反馈");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail("你有活动未结算成功，请及时客服反馈");
        }
    }


    @ApiOperation(value = "显示校验人员列表")
    @GetMapping(value = "/showCheckList")
    public JsonResult showCheckList(String meetingId){
        if (!StringUtils.hasText(meetingId)) {
            return JsonResult.fail("ID不能为空");
        }
        Map<String,Object> map = null;
        try{
            map = meetingFuseAction.showSendUserList(meetingId);
            return JsonResult.successJsonResult(map);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return JsonResult.fail("显示校验列表失败");
    }


    @ApiOperation(value = "查看需要我处理的活动信息")
    @GetMapping(value = "/SeeMeetingNews")
    public JsonResult SeeMeetingNews(String meetingId,HttpServletRequest request){
        UserGeo userGeo = null;
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            Boolean success= meetingAction.SeeMeetingNews(meetingId,userGeo.getUserId());
            return JsonResult.success().addResult("Status",success);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("联合处理状态获取异常");
        }
    }


    /**
     * @since ChenQian
     * @param meetingSearchDto
     * @param bindingResult
    //     * @param requestDto
     * @return
     */
    @ApiOperation(value = "活动列表", notes = "通用的活动列表接口，根据不同的条件返回")
    @PostMapping(value = "/list")
    public JsonResult list(@Validated @RequestBody MeetingSearchDto meetingSearchDto, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            meetingSearchDto.setLat(getLat(requestDto));
            meetingSearchDto.setLon(getLon(requestDto));
            List<MeetingListDto> list = meetingFuseAction.list(meetingSearchDto);
            if(list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取活动列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取活动列表异常！");
        }
    }


    @ApiOperation(value = "强制启动检查出席校验")
    @PostMapping(value = "/checkRegisterIfAttend")
    public JsonResult checkRegisterIfAttend(String meetingId){
        try{
            meetingFuseAction.checkRegisterIfAttend(meetingId);
        } catch (Exception e) {
            logger.error(e.getMessage()+"确实有问题",e);
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.fail("没报错");
    }

//    @ApiOperation(value = "批量删除Job")
//    @PostMapping(value = "/batchDeleteJob")
//    public JsonResult batchDeleteJob(String meetingId){
//        return getResult(meetingFuseAction.batchDeleteJob(meetingId),"有毒");
//    }
//

    @ApiOperation(value="确认入选人，推送消息")
    @PostMapping(value = "/sendMessageTo")
    public JsonResult sendMessageTo(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageTo(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="确认细节推送发起人（线下）")
    @PostMapping(value = "/confirmDetailsSendMessage")
    public JsonResult confirmDetailsSendMessage(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.confirmDetailsMessageToSenderUnderline(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("细节线下推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="推送：发起人可以开始活动（线上）")
    @PostMapping(value = "/sendMessageToSenderOnline")
    public JsonResult sendMessageToSenderOnline(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageToSenderOnline(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="活动开始前分发验证码后推送（线上）")
    @PostMapping(value = "/sendMessageBeforeActivityUnderline")
    public JsonResult sendMessageBeforeActivityUnderline(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageBeforeActivityUnderline(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    //活动开始后 推送给商家（线下）


    @ApiOperation(value="活动开始后 推送（线上）")
    @PostMapping(value = "/sendMessageBeforeActivityOnline")
    public JsonResult sendMessageBeforeActivityOnline(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageBeforeActivityOnline(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="活动结束推送发起人和入选者")
    @PostMapping(value = "/sendMessageAfterActivity")
    public JsonResult sendMessageAfterActivity(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageAfterActivity(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="活动失败，推送发起人和入选者")
    @PostMapping(value = "/sendMessageAfterFailActivity")
    public JsonResult sendMessageAfterFailActivity(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageAfterFailActivity(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="活动取消，推送发起人和入选者")
    @PostMapping(value = "/sendMessageCancelActivity")
    public JsonResult sendMessageCancelActivity(String meetingId){
        if(!StringUtils.hasText(meetingId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            meetingFuseAction.sendMessageCancelActivity(meetingId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }
}

