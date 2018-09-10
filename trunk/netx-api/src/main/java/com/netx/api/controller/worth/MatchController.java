package com.netx.api.controller.worth;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.matchEvent.*;
import com.netx.fuse.biz.worth.MatchFuseAction;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.match.MatchApplyAction;
import com.netx.worth.biz.match.MatchAttendAction;
import com.netx.worth.biz.match.MatchCreateAction;
import com.netx.worth.model.*;
import com.netx.worth.service.MatchEventService;
import com.netx.worth.vo.MatchEventSimpleVo;
import com.netx.worth.vo.MatchIndexVo;
import com.netx.worth.vo.MatchNoticeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;


/**
 * 比赛模块
 * Created by Yawn on 2018/8/4 0004.
 */

@Api(description = "比赛模块")
@RequestMapping("/wz/match")
@RestController
public class MatchController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(InvitationController.class);

    /**
     * 依赖注入
     */
    @Autowired
    private MatchCreateAction matchCreateAction;
    @Autowired
    private MatchApplyAction matchApplyAction;
    @Autowired
    private MatchAttendAction matchAttendAction;
    @Autowired
    private MatchFuseAction matchFuseAction;


    @ApiOperation(value = "添加或更新赛事")
    @PostMapping(value = "/publish", consumes = {"application/json"})
    public JsonResult publichMatch(@Validated @RequestBody SendMatchEventDTO matchEventDTO, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Double lon = getLon(request);
        Double lat = getLat(request);
        String matchId = null;
        try {
            matchEventDTO.setLat(lat);
            matchEventDTO.setLon(lon);
            matchId = matchCreateAction.addOrUpdateMatch(matchEventDTO);
            if (StringUtils.isBlank(matchId)) {
                return JsonResult.fail("发布赛事失败");
            }
            return JsonResult.success().addResult("matchId", matchId);
        } catch (RuntimeException e) {
//            if(e.getMessage().length()>15) {
//                return JsonResult.fail("发布赛事失败");
//            }
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发布赛事失败");
        }
    }

    @ApiOperation(value = "获取发布赛事的填写信息")
    @PostMapping("/getMatchEvent")
    public JsonResult getMatchEventByMatchId(@Validated @RequestBody MatchCheckDTO matchCheckDTO) {
        Map<String, Object> map = null;
        if (StringUtils.isBlank(matchCheckDTO.getMatchId())) {
            return JsonResult.fail("matchId不能为空");
        }
        try {
            map = matchCreateAction.getMatchEvent(matchCheckDTO);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("系统发生错误，请稍后重试。");
        }
        if (map == null || map.size() == 0) {
            return JsonResult.fail("没有该赛事的相关信息。");
        }
        return JsonResult.success().addResult("match", map);
    }


    @ApiOperation(value = "获取赛事全部内容")
    @PostMapping("/getAllMatch")
    public JsonResult getAllMatchByMatchId(@Validated @RequestBody MatchCheckDTO matchCheckDTO) {
        Map<String, Object> map = null;
        if (StringUtils.isBlank(matchCheckDTO.getMatchId())) {
            return JsonResult.fail("matchId不能为空");
        }
        try {
            map = matchCreateAction.getAllMatch(matchCheckDTO);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("系统发生错误，请稍后重试。");
        }
        if (map == null) {
            return JsonResult.fail("没有该赛事的相关信息。");
        }
        return JsonResult.success().addResult("match", map);
    }


    @ApiOperation(value = "删除赛事相关的所有内容")
    @PostMapping(value = "/deleteMatch")
    public JsonResult deleteMatch(String matchId, HttpServletRequest request) {
        boolean success = false;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        MatchDeleteDto matchDeleteDto = new MatchDeleteDto();
        matchDeleteDto.setMatchId(matchId);
        matchDeleteDto.setUserId(userId);
        try {
            success = matchCreateAction.deleteAllMatch(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail("删除失败");
        }
        if (success)
            return JsonResult.success("删除成功");
        return JsonResult.fail("删除失败");
    }

    /**
     * 获取赛事的基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取赛事的基础信息")
    @GetMapping(value = "/baseMatch")
    public JsonResult getBaseMatchEvent(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("matchId不能为空");
        }
        try {
            Map map = matchCreateAction.getBaseMatchEvent(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail("获取赛事失败，请稍后重试。");
        } catch (Exception e) {
            return JsonResult.fail("获取失败");
        }
    }


    /**
     * 获取所有接收邀请的单位
     *
     * @return
     */
    @ApiOperation(value = "获取所有接收邀请的单位")
    @GetMapping(value = "/getAcceptReview")
    public JsonResult getReviewListAcceptBYMatchId(String matchId) {
        Map<String, List> map;
        try {
            map = matchApplyAction.getReviewListAcceptBYMatchId(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取所有接收邀请的单位失败");
        }
    }

    /**
     * 发布赛事审核
     *
     * @return
     */
    @ApiOperation(value = "发布赛事审核", notes = "会给组织方发送通知")
    @GetMapping(value = "/commitMatch")
    public JsonResult commitMatchEvent(String matchId, HttpServletRequest httpServletRequest) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("matchId不能为空");
        }
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        boolean success;
        try {
            success = matchFuseAction.commitMatchEvent(matchId, userId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("不能重复发布审核");
        }
        if (success)
            return JsonResult.success("发布审核成功");
        return JsonResult.fail("发布审核失败");
    }

    @ApiOperation("获取所有赛区的报名信息")
    @GetMapping("/getAllApplyInfo")
    public JsonResult getAllApplyInfo(String matchId) {
        Map<String, List> map;
        try {
            map = matchApplyAction.getMatchAllZoneApplyInfo(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取所有赛区的报名信息失败");
        }
    }

    @ApiOperation("获取所有赛区的报名信息")
    @GetMapping("/getFirstApplyInfo")
    public JsonResult getMatchFirstZoneApplyInfo(String matchId) {
        Map<String, List> map;
        try {
            map = matchApplyAction.getMatchFirstZoneApplyInfo(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取所有赛区的报名信息失败");
        }
    }

    @ApiOperation(value = "获取所有已经发布了的赛事")
    @GetMapping(value = "/listMatchEvents")
    public JsonResult listMatchEvent() {
        Map<String, Object> map;
        try {
            map = matchCreateAction.listMatchEvent();
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("还没有已经通过审核的比赛");
        }
    }


    @ApiOperation(value = "获取所有已经发布了的赛事")
    @PostMapping(value = "/listMatchEvents")
    public JsonResult listMatchEvent(CommonPageDto dto) {
        try {
            Page<MatchEvent> page = new Page<>(dto.getCurrentPage(), dto.getSize());
            List<MatchEvent> list = matchCreateAction.listMatchEvent(page);
            return JsonResult.success().addResult("list", list);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("还没有已经通过审核的比赛");
        }
    }

    @ApiOperation(value = "获取所有已经发布了的赛事")
    @GetMapping(value = "/listTwoMatchEvent")
    public JsonResult listTwoMatchEvent() {
        Map<String, Object> map;
        try {
            map = matchCreateAction.listTwoMatchEvent();
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("还没有已经通过审核的比赛");
        }
    }

    @ApiOperation(value = "添加或更新举办单位")
    @PostMapping(value = "/addOrganizer", consumes = {"application/json"})
    public JsonResult addOrganizer(@Validated @RequestBody List<MatchReviewDTO> dtos, String matchId, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(httpServletRequest);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        boolean success;
        try {
            success = matchFuseAction.addOrganizer(dtos, matchId);
            return JsonResult.success().addResult("status", success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加主办单位失败");
        }
    }


    @ApiOperation(value = "删除举办单位")
    @PostMapping(value = "/deleteOrganizer", consumes = {"application/json"})
    public JsonResult deleteOrganizer(@Validated @RequestBody MatchDeleteDto matchDeleteDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        boolean success;
        try {
            success = matchCreateAction.deleteOrganizer(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除举办单位失败，请稍后再试。");
        }
        return JsonResult.success("删除成功").addResult("status", success);
    }


    @ApiOperation(value = "查询举办单位")
    @GetMapping("/listOrganizers")
    public JsonResult listOrganizer(String matchId, HttpServletRequest request) {
        Map<String, Object> map;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            map = matchCreateAction.listOrganizer(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询错误");
        }
        if (map == null) {
            return JsonResult.fail("没有单位");
        }
        return JsonResult.success().addResult("list", map);
    }


    @ApiOperation(value = "添加或更新组成人员", notes = "审核未通过前调用该接口")
    @PostMapping(value = "/addMember", consumes = {"application/json"})
    public JsonResult addMember(@Validated @RequestBody List<MemberDTO> memberDTOList, String matchId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = matchCreateAction.addOrUpdateMember(memberDTOList, matchId);
            if (!success) {
                return JsonResult.fail("添加组成人员失败");
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加组成成员失败，请稍后再试。");
        }
        return JsonResult.success("添加成员成功");
    }


    @ApiOperation(value = "查看添加的工作人员")
    @GetMapping(value = "/getAllWorkPeople")
    public JsonResult getWorkPeopleAllListByMatchId(@RequestParam("matchId") String matchId) {
        try {
            boolean success = false;
            if (matchId == null) {
                return JsonResult.fail("matchId不能为空");
            }
            Map<String, List> map = matchCreateAction.getWorkPeopleAllListByMatchId(matchId);
            return JsonResult.success().addResult("status", success).addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询错误");
        }
    }


    @ApiOperation(value = "添加或更新组成人员", notes = "审核通过后邀请成员用，后台会直接推送消息")
    @PostMapping(value = "/addMemberAfterReview", consumes = {"application/json"})
    public JsonResult addMemberAfterReview(@Validated @RequestBody List<MemberDTO> memberDTOList, String matchId, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(httpServletRequest);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加失败啦，请刷新试试 ");
        }
        try {
            boolean success = matchFuseAction.inviteMemberAfterReview(memberDTOList, userId, matchId);
            if (!success) {
                return JsonResult.fail("邀请人员失败，请刷新试试");
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加失败");
        }
        return JsonResult.success("添加成功，请告知他完成注册信息");
    }


    @ApiOperation(value = "查看在场工作人员，嘉宾")
    @GetMapping(value = "/listMember")
    public JsonResult listMember(String matchId) {
        try {
            boolean success = false;
            Map<String, List> map = matchCreateAction.getMatchMemberListByMatchId(matchId);
            if (map != null && map.size() > 0) {
                success = true;
                return JsonResult.success().addResult("status", success).addResult("data", map);
            }
            return JsonResult.fail("找不到在场工作人员");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
    }

    @ApiOperation(value = "删除组成人员")
    @PostMapping(value = "/deleteMember", consumes = {"application/json"})
    public JsonResult deleteMember(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        boolean success = false;
        try {
            success = matchCreateAction.deleteMatchMember(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除失败，请稍后重试。");
        }
        if (success) {
            return JsonResult.success("删除成功").addResult("status", success);
        }
        return JsonResult.fail("删除失败，请稍后重试。");
    }

    @ApiOperation(value = "添加或更新比赛要求")
    @PostMapping(value = "/addRequirement", consumes = {"application/json"})
    public JsonResult addRequirement(@Validated @RequestBody List<MatchRequirementDTO> matchRequirementDTOList, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = false;
            success = matchCreateAction.addRequirement(matchRequirementDTOList);
            return JsonResult.success().addResult("status", success);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加比赛要求失败");
        }
    }

    @ApiOperation(value = "查看比赛要求")
    @GetMapping(value = "/listRequirement")
    public JsonResult listRequirement(String groupId) {
        Map<String, Object> map;
        try {
            map = matchCreateAction.listMatchRequirement(groupId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail("查看失败");
        }
    }


    @ApiOperation(value = "删除比赛要求")
    @PostMapping(value = "/deleteRequirement")
    public JsonResult deleteRequirement(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request, BindingResult bindingResult) {
        boolean success;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            if (matchCreateAction.deleteRequirement(matchDeleteDto)) {
                return JsonResult.success().addResult("status", 200);
            }
            return JsonResult.fail("删除要求失败,请稍后重试。");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除要求失败,请稍后重试。");
        }
    }

    @ApiOperation(value = "添加或更新奖项")
    @PostMapping(value = "/addAward", consumes = {"application/json"})
    public JsonResult addMatchAward(@Validated @RequestBody List<MatchAwardDTO> matchAwardDTOList, String matchId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (matchId == null) {
            return JsonResult.fail("比赛ID为空");
        }
        try {
            if (matchCreateAction.addOrUpdateMatchAward(matchAwardDTOList))
                return JsonResult.success().addResult("status", true);
            return JsonResult.fail("添加奖项失败");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加奖项失败");
        }
    }

    @ApiOperation(value = "删除奖项")
    @PostMapping(value = "/deleteAward", consumes = {"application/json"})
    public JsonResult deleteMatchAward(@Validated @RequestBody MatchDeleteDto matchDeleteDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            if (!matchCreateAction.deleteMatchAwardByMatchId(matchDeleteDto))
                return JsonResult.fail("删除失败");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除失败");
        }
        return JsonResult.success("删除成功");
    }

    @ApiOperation(value = "查看比赛奖项名单")
    @GetMapping(value = "/listAward")
    public JsonResult listMatchAward(String matchId) {
        if (StringUtils.isBlank(matchId))
            return JsonResult.fail("matchID不能为空");
        Map<String, Object> map;
        try {
            map = matchCreateAction.listMatchAward(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
    }


    @ApiOperation(value = "添加或更新场次")
    @PostMapping(value = "/addVenue")
    public JsonResult addOrUpdateMatchVenue(@Validated @RequestBody List<MatchVenueDTO> dtos) {
        if (dtos == null || dtos.size() == 0)
            return JsonResult.fail("会场信息为空");
        try {
            if (matchCreateAction.addOrUpdateMatchVenue(dtos))
                return JsonResult.success("添加成功");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加错误");
        }
        return JsonResult.fail("添加失败");
    }


    @ApiOperation(value = "查看场次信息")
    @GetMapping(value = "/listVenue")
    public JsonResult listMatchVenue(String matchZoneId) {
        if (StringUtils.isBlank(matchZoneId))
            return JsonResult.fail("请填入赛区ID");
        Map<String, List> map;
        try {
            map = matchCreateAction.getMatchVenueListByMatchZoneId(new String[]{matchZoneId});
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看错误");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "删除场次")
    @PostMapping(value = "/deleteVenue", consumes = {"application/json"})
    public JsonResult deleteMatchVenue(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request) {
        boolean success = false;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchCreateAction.deleteVenue(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除会场失败");
        }
        if (success) {
            return JsonResult.success("删除完成");
        }
        return JsonResult.fail("删除会场失败");
    }


    @ApiOperation(value = "发布或更新门票档次")
    @PostMapping(value = "/addTicket", consumes = {"application/json"})
    public JsonResult addMatchTicket(@Validated @RequestBody MatchTicketAndDefaultTimeDTO matchApplyDefaultTimeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String timeId = null;
        try {
            if (matchApplyDefaultTimeDto.getMatchApplyDefaultTimeDto() != null) {
                timeId = matchCreateAction.addDefaultTime(matchApplyDefaultTimeDto.getMatchApplyDefaultTimeDto(), matchApplyDefaultTimeDto.getMatchTicketDTOList());
                if (StringUtils.isBlank(timeId)) {
                    return JsonResult.fail("默认时间发布失败");
                }
                return JsonResult.success().addResult("data", timeId);
            }else {
                matchCreateAction.addOrUpdateMatchTicket(matchApplyDefaultTimeDto.getMatchTicketDTOList(),null,null);
                return JsonResult.success();
            }

        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发布门票失败");
        }
    }

    @ApiOperation(value = "获得默认门票档次设置")
    @GetMapping(value = "/listDefulutTicket")
    public JsonResult listDefulutTicket(String matchId) {
        try {
            Map<String, Object> map = matchCreateAction.getDefaultTicketByMatchId(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("找不到票价信息");
        }
    }

    @ApiOperation(value = "档次查询")
    @GetMapping(value = "/listTicket")
    public JsonResult listTicket(String zoneId) {
        try {
            boolean success = false;
            Map<String, Object> map = matchCreateAction.getMatchTicketListByMatchId(zoneId);
            if (map != null && map.size() > 0) {
                success = true;
                return JsonResult.success().addResult("status", success).addResult("data", map);
            }
            return JsonResult.fail("找不到票价信息");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("找不到票价信息");
        }
    }


    @ApiOperation(value = "删除档次")
    @PostMapping(value = "/deleteTicket")
    public JsonResult deleteMatchTicket(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request) {
        boolean success = false;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchCreateAction.deleteTicketId(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            JsonResult.fail("删除失败");
        }
        if (success)
            return JsonResult.success("删除成功");
        else
            return JsonResult.fail("删除失败");
    }


    /*****************************************分割线****************赛事逻辑模块***************************************************************/


    @ApiOperation(value = "删除报名用户")
    @PostMapping(value = "/deleteParticipant", consumes = {"application/json"})
    public JsonResult deleteParticipant(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request) {
        boolean success;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchApplyAction.deleteParticipantById(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除失败");
        }
        if (success)
            return JsonResult.success("批量删除成功");
        return JsonResult.success("删除失败");
    }


    @ApiOperation(value = "查看比赛报名用户")
    @PostMapping(value = "/listParticipant", consumes = {"application/json"})
    public JsonResult listParticipant(@Validated @RequestBody CommonPageDto matchPageDto, String matchId) {
        if (matchId == null)
            return JsonResult.fail("请传入比赛ID");
        Map<String, Object> map;
        Page<MatchParticipant> page = new Page<>(matchPageDto.getCurrentPage(), matchPageDto.getSize());
        try {
            map = matchApplyAction.listMatchParticipant(matchId, page);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }

    @ApiOperation(value = "根据赛区查看比赛报名用户", notes = "工作人员审核时调用，查看现场状态时调用")
    @PostMapping(value = "/listMatchParticipantByZone", consumes = {"application/json"})
    public JsonResult listMatchParticipantByZone(@Validated @RequestBody CommonPageDto pageDto, String matchId, String zoneId, BindingResult bindingResult) {
        if (StringUtils.isBlank(matchId))
            return JsonResult.fail("请选定比赛");
        if (StringUtils.isBlank(zoneId))
            return JsonResult.fail("请选择赛区");
        Map<String, Object> map;
        try {
            Page<MatchParticipant> page = new Page<>(pageDto.getCurrentPage(), pageDto.getSize());
            map = matchApplyAction.listMatchParticipantByZone(matchId, zoneId, page);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "根据赛组查看比赛报名用户", notes = "工作人员审核，查看现场状态")
    @PostMapping(value = "/listMatchParticipantByGroup", consumes = {"application/json"})
    public JsonResult listMatchParticipantByGroupe(@Validated @RequestBody CommonPageDto pageDto, String zoneId, String groupId, BindingResult bindingResult) {
        if (StringUtils.isBlank(zoneId))
            return JsonResult.fail("请选定赛区");
        if (StringUtils.isBlank(groupId))
            return JsonResult.fail("请选择赛组");
        Map map;
        try {
            map = matchApplyAction.listMatchParticipantByGroupe(zoneId, groupId, (pageDto.getCurrentPage() - 1) * pageDto.getSize(), pageDto.getSize());
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.successJsonResult(map);
    }

    @ApiOperation(value = "根据赛组查看比赛报名用户", notes = "工作人员审核，查看现场状态")
    @PostMapping(value = "/listMatchParticipantByGroupAndStatus", consumes = {"application/json"})
    public JsonResult listMatchParticipantByGroupAndStatus(@Validated @RequestBody MatchParticipantStatusDto matchParticipantStatusDto, BindingResult bindingResult) {
        if (StringUtils.isBlank(matchParticipantStatusDto.getZoneId()))
            return JsonResult.fail("请选定赛区");
        if (StringUtils.isBlank(matchParticipantStatusDto.getGroupId()))
            return JsonResult.fail("请选择赛组");
        Map map;
        try {
            map = matchApplyAction.listMatchParticipantByGroupAndStatus(matchParticipantStatusDto.getZoneId(), matchParticipantStatusDto.getGroupId() , matchParticipantStatusDto.getStatus());
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.successJsonResult(map);
    }

    @ApiOperation(value = "根据参赛者查看参赛报名要求信息")
    @GetMapping(value = "/getRequirementData")
    public JsonResult getRequirementData(String participantId, HttpServletRequest httpServletRequest) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getRequirementData(participantId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "查看比赛已经支付报名费的用户")
    @GetMapping(value = "/listPaidMatchParticipant")
    public JsonResult listPaidMatchParticipant(String matchId) {
        if (matchId == null)
            return JsonResult.fail("请传入比赛ID");
        Map<String, Object> map;
        try {
            map = matchApplyAction.listPaidMatchParticipant(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "参赛人员通过审核", notes = "通过审核之后要进入下一个赛程")
    @PostMapping(value = "/matchParticipantPassReview", consumes = {"application/json"})
    public JsonResult passMatchParticipant(@Validated @RequestBody List<String> ids, BindingResult bindingResult, String progressId) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (StringUtils.isBlank(progressId)) {
            return JsonResult.fail("请设定下一个的赛程");
        }
        boolean success;
        try {
            success = matchApplyAction.passReview(ids, progressId);
            if (success)
                return JsonResult.success("审核成功");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("审核失败");
        }
        return JsonResult.fail("审核失败");
    }


    @ApiOperation(value = "参赛人员通过审核", notes = "通过审核之后要进入下一个赛程")
    @PostMapping(value = "/affirmFinalists", consumes = {"application/json"})
    public JsonResult passMatchParticipant(@Validated @RequestBody List<MatchUpDateDto> matchUpDateDtoList, String matchId) {
        boolean success;
        try {
            success = matchApplyAction.affirmFinalists(matchUpDateDtoList, matchId);
            if (success)
                return JsonResult.success("审核成功");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作失败");
        }
        return JsonResult.fail("审核失败");
    }

//    @ApiOperation(value = "团队添加或更新参赛资料")
//    @PostMapping(value = "/addParticipantData", consumes = {"application/json"})
//    public JsonResult addParticipantData(@Validated @RequestBody List<MatchChildInfoDTO> dtos, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        boolean success;
//        try {
//            success = matchApplyAction.addChild(dtos);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return JsonResult.fail(e.getMessage());
//        }
//        if (success)
//            return JsonResult.success("添加成功");
//        return JsonResult.fail("添加失败");
//    }


    @ApiOperation(value = "获取团队和孩子信息", notes = "isTeam = true或者is_guardian=true的才可以查看")
    @GetMapping(value = "/listTeamMessage")
    public JsonResult getChildList(String participantId, HttpServletRequest httpServletRequest) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getChildList(participantId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }

    @ApiOperation(value = "删除一个队员信息")
    @PostMapping(value = "/deletePaticipantData")
    public JsonResult listPaticipantData(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request) {
        boolean success;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchApplyAction.deleteChild(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除错误");
        }
        if (success)
            return JsonResult.success("删除成功");
        return JsonResult.fail("删除失败");
    }


    @ApiOperation(value = "外来用户注册网址，激活工作人员邀请")
    @GetMapping(value = "/activateMember")
    public JsonResult activeMember(HttpServletRequest httpServletRequest, String c) {
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchFuseAction.activateMember(userId, c);
        return JsonResult.success("加入赛事成功");
    }


    @ApiOperation(value = "获取门票入场码")
    @GetMapping(value = "getMatchCode")
    public JsonResult getMatchCode(HttpServletRequest httpServletRequest, String ticketId) {
        if (StringUtils.isEmpty(ticketId)) {
            return JsonResult.fail("操作错误");
        }
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Map<String, Object> map;
        try {
            /*
            matchCode=====Audience的ID
             */
            map = matchAttendAction.getAttendCode(ticketId, userId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.success().addResult("code", map);
    }


    @ApiOperation(value = "所有人员出席接口", notes = "工作人员扫码调用")
    @GetMapping(value = "/allAttend")
    public JsonResult allAttend(HttpServletRequest httpServletRequest, String code) {
        try {
            getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Map<String, Object>map;
        try {
            map = matchAttendAction.allAttend(code);
            return JsonResult.success("出席成功").addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }


    @ApiOperation(value = "查看报名情况", notes = "用户获取出席出席码，查看自己审核状态")
    @GetMapping(value = "/listParticipantsByUser")
    public JsonResult listParticipantsByUser(HttpServletRequest httpServletRequest, String matchId) {
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Map<String, Object> map;
        try {
            /*
            activeCode=====parvicipant的ID
             */
            map = matchApplyAction.listParticipantByUser(matchId, userId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.successJsonResult(map);
    }


    @ApiOperation(value = "比赛通过审核，会给所有工作人员，嘉宾发送通知")
    @PostMapping(value = "/passReview")
    public JsonResult passReview(@Validated @RequestBody  MatchPassReviewDto matchPassReviewDto, boolean isPass, HttpServletRequest httpServletRequest) {
        String userId = null;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = false;
            success = matchFuseAction.issueMatch(matchPassReviewDto,userId);
            return JsonResult.success().addResult("status", success);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }


    @ApiOperation(value = "查看在场观众", notes = "根据比赛查看观众")
    @GetMapping(value = "/listAudience")
    public JsonResult listAudience(String matchId) {
        try {
            boolean success = true;
            Map<String, Object> map = matchAttendAction.getMatchAudience(matchId);
            return JsonResult.success().addResult("status", success).addResult("data", map);
        }catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        }  catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
    }


    @ApiOperation(value = "大赛详情，根据用户类型显示不同的页面")
    @GetMapping(value = "/matchdesc")
    public JsonResult getMatchdesc(String matchId, HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String, Object> map = matchAttendAction.getMatchMemberKind(matchId, userId);
            boolean success = true;
            return JsonResult.success().addResult("status", success).addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看用户类型失败");
        }
    }

    @ApiModelProperty(value = "查看人员类型和比赛状态")
    @GetMapping(value = "/getStatusAndKind")
    public JsonResult getMemberKind(String matchId, HttpServletRequest request) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("matchId不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String, Object> map = matchAttendAction.getMemberKind(userId, matchId);
            boolean success = true;
            return JsonResult.success().addResult("status", success).addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看用户类型失败");
        }
    }


    @ApiOperation(value = "查看会场工作人员")
    @GetMapping(value = "/listWorkPeople")
    public JsonResult listWorkPeople(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("请指定比赛");
        }
        Map<String, Object> map;
        try {
            map = matchAttendAction.getWorkPeopleAllListByMatchId(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "获取已经接受邀请的工作人员")
    @GetMapping(value = "/listAcceptWorkPeople")
    public JsonResult listAcceptWorkPeople(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("请指定比赛");
        }
        Map<String, Object> map;
        try {
            map = matchAttendAction.getWorkAcceptPeopleAcceptListByMatchId(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "获取到场的工作人员")
    @GetMapping(value = "/listSpottWorkPeople")
    public JsonResult getWorkAcceptPeopleIsSpotANDIsAccepttListByMatchId(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("请指定比赛");
        }
        Map<String, Object> map;
        try {
            map = matchAttendAction.getWorkAcceptPeopleIsSpotANDIsAccepttListByMatchId(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            return JsonResult.fail("查看失败");
        }
        return JsonResult.success().addResult("data", map);
    }

    @ApiOperation(value = "更新出场顺序")
    @PostMapping(value = "/updateAppearanceOrder", consumes = {"application/json"})
    public JsonResult updateAppearanceOrder(@Validated @RequestBody List<MatchAppearanceDTO> matchAppearanceDTOList, @NotBlank(message = "matchId不能为空") String matchId, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = matchAttendAction.updateMatchAppearanceOrder(matchAppearanceDTOList, userId, matchId);
            return JsonResult.success().addResult("status", success);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("更新出场顺序失败，请刷新试试");
        }
    }


    @ApiOperation(value = "根据赛区获得出场顺序")
    @GetMapping(value = "/listAppearanceByVenue")
    public JsonResult getAppearance(String matchId, String zoneId) {
        try {
            Map<String, List> map = matchAttendAction.listMatchAppearance(matchId, zoneId);
            return JsonResult.success().addResult("status", true).addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取场次信息失败");
        }
    }


    @ApiOperation(value = "根据赛组获得出场顺序", notes = "传入赛程ID跟赛组ID")
    @GetMapping(value = "listAppearanceByGroup")
    public JsonResult listAppearanceByGroup(String progressId, String groupId, String zoneId) {
        try {
            Map<String, List> map = matchAttendAction.listMatchAppearanceByGroup(progressId, groupId, zoneId);
            return JsonResult.success().addResult("status", true).addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取场次信息失败");
        }
    }


    @ApiOperation(value = "用户评分")
    @PostMapping(value = "/raste")
    public JsonResult raste(@Validated @RequestBody MatchRatersDTO matchRatersDTO, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = matchAttendAction.addMatchRater(matchRatersDTO, userId);
            return JsonResult.success().addResult("status", success);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }


    @ApiOperation(value = "参赛者的详细得分")
    @GetMapping(value = "/listMatchRaterScoreDetail")
    public JsonResult listMatchRaterScoreDetail(HttpServletRequest httpServletRequest, String participantId, String progressId) {
        Map<String, Object> map;
        try {
            map = matchAttendAction.listMatchRaterScoreDetail(progressId, participantId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看评分失败");
        }
        return JsonResult.success().addResult("data", map);
    }


    @ApiOperation(value = "根据赛组查看所有选手的平均分")
    @GetMapping(value = "/listMatchRaterAvgScore")
    public JsonResult listMatchRaterAvgScore(HttpServletRequest httpServletRequest, String groupId) {
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        Map<String, Object> map;
        try {
            map = matchAttendAction.listScoreByGroupId(groupId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("请刷新试试看");
        }
        return JsonResult.success().addResult("data", map);
    }

    //+++++++++++++++++++++++++++++++++++赛组++++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * 添加赛组
     *
     * @param dto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加赛组(进来就调)")
    @PostMapping(value = "/addMatchGroup", consumes = {"application/json"})
    public JsonResult addMatchGroup(@Validated @RequestBody MatchGroupDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String groupId;
        try {
            groupId = matchCreateAction.addMatchGroup(dto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (StringUtils.isNoneBlank(groupId)) {
            return JsonResult.success("添加赛组成功").addResult("groupId", groupId);
        }
        return JsonResult.fail("添加赛组失败");
    }


    @ApiOperation(value = "根据赛区查看赛组报名情况")
    @GetMapping(value = "/listMatchGroupByZoneId")
    public JsonResult listMatchGroupByZoneId(String zoneId) {
        if (StringUtils.isBlank(zoneId)) {
            return JsonResult.fail("请选择赛区");
        }
        try {
            Map<String, Object> map = matchAttendAction.selectMatchGroupByZoneId(zoneId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }

    @ApiOperation(value = "根据赛区查看赛组报名情况")
    @GetMapping(value = "/listMatchGroupByMatchId")
    public JsonResult listMatchGroupByMatchId(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("请选择赛区");
        }
        try {
            Map<String, Object> map = matchAttendAction.selectMatchGroupByMatchId(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }

    /**
     * 更新赛组
     *
     * @param dtos
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新赛组")
    @PostMapping(value = "/updateMatchGroupList", consumes = {"application/json"})
    public JsonResult updateMatchGroup(@Validated @RequestBody List<MatchGroupDTO> dtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (dtos.size() == 0 || dtos == null) {
            return JsonResult.fail("不能更新空的赛组列表");
        }
        boolean success;
        try {
            success = matchCreateAction.updateMatchGroup(dtos);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("更新赛组成功");
        }
        return JsonResult.fail("更新赛组失败");
    }

    /**
     * 删除赛组
     *
     * @param matchDeleteDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除赛组")
    @PostMapping(value = "/deleteMatchGroup")
    public JsonResult deleteMatchGroup(@Validated @RequestBody MatchDeleteDto matchDeleteDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        boolean success;
        try {
            success = matchCreateAction.deleteMatchGroup(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("刪除赛组失败，请稍后重试。");
        }
        if (success)
            return JsonResult.success("删除成功");
        return JsonResult.fail("删除失败");
    }


    /**
     * 显示赛组列表
     *
     * @param matchId
     * @return
     */
    @ApiOperation(value = "查看比赛所有赛组")
    @GetMapping(value = "/listMatchGroup")
    public JsonResult listMatchGroup(String matchId) {
        Map<String, Object> map;
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("matchId不能为空");
        }
        try {
            map = matchCreateAction.listMatchGroupByMatch(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("还没有赛组");
        }
        return JsonResult.success().addResult("data", map);
    }


    /**
     * 添加赛区
     *
     * @param dto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加赛区(进来的时候调用)")
    @PostMapping(value = "/addMatchZone")
    public JsonResult addMatchZone(@Validated @RequestBody MatchZoneDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String zoneId;
        try {
            zoneId = matchCreateAction.addMatchZone(dto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (StringUtils.isNoneBlank(zoneId)) {
            return JsonResult.success("添加赛区成功").addResult("zoneId", zoneId);
        }
        return JsonResult.fail("添加赛区失败");
    }

    /**
     * 添加默认时间
     *
     * @param matchApplyDefaultTimeDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加默认时间（报名的默认时间）")
    @PostMapping(value = "/addDefaultTime")
    public JsonResult addMatchZone(@Validated @RequestBody MatchApplyDefaultTimeDto matchApplyDefaultTimeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String timeId;
        try {
            timeId = matchCreateAction.addDefaultTime(matchApplyDefaultTimeDto, null);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (StringUtils.isNoneBlank(timeId)) {
            return JsonResult.success("添加默认时间成功").addResult("data", timeId);
        }
        return JsonResult.fail("添加默认时间失败");
    }

    /**
     * 更新默认时间
     *
     * @param matchApplyDefaultTimeDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新默认时间")
    @PostMapping(value = "/updateDefaultTime")
    public JsonResult updateMatchZone(@Validated @RequestBody MatchApplyDefaultTimeDto matchApplyDefaultTimeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String success;
        try {
            success = matchCreateAction.updateDefaultTime(matchApplyDefaultTimeDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (StringUtils.isNoneBlank(success)) {
            return JsonResult.success("更新默认时间成功");
        }
        return JsonResult.fail("更新默认时间失败");
    }

    /**
     * 获取默认报名的时间
     *
     * @param matchId
     * @return
     */
    @ApiOperation(value = "获取默认报名的时间")
    @GetMapping(value = "/getApplyDefaultTime")
    public JsonResult getApplyDefaultTime(String matchId) {
        Map<String, MatchApplyDefaultTime> map;
        try {
            map = matchCreateAction.getMatchDefaultApplyTime(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (map != null) {
            return JsonResult.success("获取成功").addResult("data", map);
        }
        return JsonResult.success();
    }

    /**
     * 获取默认购票的时间
     *
     * @param matchId
     * @return
     */
    @ApiOperation(value = "获取默认购票的时间")
    @GetMapping(value = "/getTickeDefaultTime")
    public JsonResult getTickeDefaultTime(String matchId) {
        Map<String, MatchApplyDefaultTime> map;
        try {
            map = matchCreateAction.getMatchDefaultTicketTime(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (map != null) {
            return JsonResult.success("更新默认时间成功").addResult("data", map);
        }
        return JsonResult.fail("更新默认时间失败");
    }

    /**
     * 更新赛区
     *
     * @param dtos
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新赛区")
    @PostMapping(value = "/updateMatchZoneList", consumes = {"application/json"})
    public JsonResult updateMatchZoneList(@Validated @RequestBody List<MatchZoneDto> dtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (dtos.size() == 0 || dtos == null) {
            return JsonResult.fail("不能更新空的赛组列表");
        }
        boolean success;
        try {
            success = matchCreateAction.updateMatchZoneList(dtos);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("更新赛区成功");
        }
        return JsonResult.fail("更新赛区失败");
    }

    /**
     * 删除赛区
     *
     * @param matchDeleteDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除赛区")
    @PostMapping(value = "/deleteMatchZone")
    public JsonResult deleteMatchZone(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        boolean success;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchCreateAction.deleteMatchZone(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("删除成功");
        }
        return JsonResult.fail("删除失败，请刷新试试");
    }

    /**
     * 查看已有赛区
     *
     * @param matchId 比赛ID
     */
    @ApiOperation(value = "查看已有赛区")
    @GetMapping(value = "/listMatchZone")
    public JsonResult listMatchZone(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请指定一个比赛");
        }
        Map<String, List> datas;
        try {
            datas = matchCreateAction.getMatchZoneListByMatchId(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("请刷新试试看");
        }
        return JsonResult.success().addResult("data", datas);
    }

    /**
     * 添加或者更新赛制
     *
     * @param dtos
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加或更新赛制")
    @PostMapping(value = "/addMatchProgress")
    public JsonResult addMatchProgress(@Validated @RequestBody List<MatchProgressDTO> dtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        boolean success;
        try {
            success = matchCreateAction.addMatchProgress(dtos);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("添加赛组成功");
        }
        return JsonResult.fail("添加赛组失败");
    }

    /**
     * 删除赛制
     *
     * @param matchDeleteDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除赛制")
    @PostMapping(value = "/deleteMatchProgress")
    public JsonResult deleteMatchProgress(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        boolean success;
        String userId = null;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        matchDeleteDto.setUserId(userId);
        try {
            success = matchCreateAction.deleteMatchProgress(matchDeleteDto);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("删除成功");
        }
        return JsonResult.fail("删除失败，请刷新试试");
    }

    /**
     * 查看已有赛制
     *
     * @param matchId
     * @return
     */
    @ApiOperation(value = "查看已有赛制")
    @GetMapping(value = "/listMatchProgress")
    public JsonResult listMatchProgress(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请指定一个比赛");
        }
        Map<String, Object> datas;
        try {
            datas = matchCreateAction.listMatchProgress(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("请刷新试试看");
        }
        return JsonResult.success().addResult("data", datas);
    }

    @ApiOperation(value = "通过比赛id获取所有的场次（初始化赛程的时候调用）")
    @GetMapping(value = "/getAllVenue")
    public JsonResult getAllVenue(String matchId) {
        Map<String, List> map;
        try {
            map = matchCreateAction.getMatchAllVenueListByMatchId(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail("查看失败");
        }
    }

    /**
     * 查看已有赛制
     *
     * @param matchId
     * @return
     */
    @ApiOperation(value = "获得比赛的所有场次信息（设置赛事流程的时候调用）")
    @GetMapping(value = "/getMatchAllVenueListByMatchId")
    public JsonResult getMatchAllVenueListByMatchId(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            return JsonResult.fail("请指定一个比赛");
        }
        Map<String, List> datas;
        try {
            datas = matchCreateAction.getMatchAllVenueListByMatchId(matchId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("请刷新试试看");
        }
        return JsonResult.success().addResult("data", datas);
    }


    /**
     * 添加或者更新赛制流程
     *
     * @param matchFlowDTOList
     * @return
     */
    @ApiOperation(value = "添加或者更新赛制流程")
    @GetMapping(value = "/updateFlow")
    public JsonResult updateFlow(@Validated @RequestBody List<MatchFlowDTO> matchFlowDTOList) {
        boolean success;
        try {
            success = matchCreateAction.updateFlow(matchFlowDTOList);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("请刷新试试看");
        }
        if (success) {
            return JsonResult.success("更新成功");
        }
        return JsonResult.fail("更新失败");
    }

    /**
     * 更新赛区中赛组的时间
     *
     * @param dtos
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新赛区中赛组的时间")
    @PostMapping(value = "/updateMatchGroupAndZone", consumes = {"application/json"})
    public JsonResult updateMatchGroupAndZone(@Validated @RequestBody List<MatchGroupAndZoneDto> dtos, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        boolean success;
        try {
            success = matchCreateAction.updateMatchGroupAndZone(dtos);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        if (success) {
            return JsonResult.success("更新成功");
        }
        return JsonResult.fail("更新失败");
    }


    /**
     * 更新赛区中赛组的时间
     *
     * @param zoneId
     * @return
     */
    @ApiOperation(value = "获取赛区中赛组")
    @GetMapping(value = "/getGroupByZoneId")
    public JsonResult updateMatchGroupAndZone(HttpServletRequest httpServletRequest, String zoneId) {
        try {
            String userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (StringUtils.isBlank(zoneId)) {
            return JsonResult.fail("请选定赛区");
        }
        Map map;
        try {
            map = matchCreateAction.getMatchGroupByZoneId(zoneId);
            map.put("zond_id", zoneId);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.success("获取成功").addResult("data", map);
    }

    @ApiModelProperty(value = "晋级下一轮比赛")
    @PostMapping(value = "/AdvanceMatch")
    public JsonResult passMatch(List<String> ids, String progressId, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = matchApplyAction.passMatch(ids, progressId);
            if (success)
                return JsonResult.success("晋级成功");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("人员晋级失败，请联系开发人员");
        }
        return JsonResult.fail("人员晋级失败，请联系开发人员");
    }

    //++++++++++++++++++++++++++++++++++购票++++++++++++++++++++++++++++++++++++++++++++++++//


    /**
     * 获取支付码
     * @param request
     * @return
     */
//    @ApiOperation(value = "获取支付码")
//    @PostMapping("/get")
//    public JsonResult getHash(HttpServletRequest request){
//        String userId = null;
//        String authToken = tokenHelper.getToken(request);
//        if(authToken!=null){
//            userId = tokenHelper.getUsernameFromToken(authToken);
//        }
//        if(StringUtils.isBlank(userId)){
//            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
//        }
//        try {
//            String res= matchApplyAction.getTicketCodeByToken(userId,authToken);
//            if (res != null){
//                return JsonResult.success().addResult("res",res);
//            }
//            return JsonResult.fail("获取支付码失败！");
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//            return JsonResult.fail("获取支付码异常！");
//        }
//    }


    /**
     * 验证支付是否过期
     *
     * @return
     */
    @ApiOperation(value = "验证支付是否过期")
    @PostMapping(value = "/checkpayid")
    public JsonResult checkUser(String payId, HttpServletRequest request) {
        if (StringUtils.isBlank(payId)) {
            return JsonResult.fail("payId不能爲空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            if (matchApplyAction.CheckTicketCodeout(userId, payId)) {
                return JsonResult.success("支付Id未过期");
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.fail("支付Id过期");
    }

    @ApiOperation(value = "检测是否有未支付的报名信息")
    @GetMapping(value = "/checkIsHaveApply")
    public JsonResult checkIsHaveApply(HttpServletRequest request, String matchId) {
        String userId;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String, Object> map = matchApplyAction.listParticipantByUser(matchId, userId);
            return JsonResult.success().addResult("status", map.get("status"));
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
    }

    /**
     * 准备购买一张票
     *
     * @return
     */
    @ApiOperation(value = "购票点击支付按钮（免费的门票也调用此接口）调用此接口")
    @PostMapping(value = "/buyticket")
    public JsonResult buyTicket(@Validated @RequestBody MatchTicketPlanPayDto matchTicketPlanPayDto, HttpServletRequest request, BindingResult bindingResult) {
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            throw new RuntimeException(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        String token = tokenHelper.getToken(request);
        if (StringUtils.isBlank(token)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            if (matchTicketPlanPayDto.getTicketTypeEnum().getType() == 1) {
                String audienceId = matchApplyAction.updateTicketCountAndTicketAudience(matchTicketPlanPayDto, userId, true);
                if (StringUtils.isNoneBlank(audienceId)) {
                    return JsonResult.success();
                }
            } else if (matchTicketPlanPayDto.getTicketTypeEnum().getType() == 2) {
                Map<String, String> map = matchFuseAction.getMacthAudienceId(userId, token, matchTicketPlanPayDto);
                if (StringUtils.isNoneBlank(map.get("audienceId")) && StringUtils.isNoneBlank(map.get("payId"))) {
                    return  JsonResult.success().addResult("data", map);
                }
            }
            return JsonResult.fail("获取订单失败,请稍后重试");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取订单失败,请稍后重试");
        }
    }

    /**
     * 确认购买一张票
     *
     * @return
     */
    @ApiOperation(value = "确认购票")
    @PostMapping(value = "/affirm/buyTicket")
    public JsonResult affirmBuyTicket(@Validated @RequestBody MatchTicketPayDto matchTicketPayDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        try {
            if (matchFuseAction.updateBuyTicketPayStatus(matchTicketPayDto, userId)) {
                return JsonResult.success("购票成功");
            }
            return JsonResult.fail("购票失败，请稍后再试试。");
        }catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        }  catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("购票失败，请稍后再试试。");
        }
    }

    @ApiOperation(value = "参赛点击支付按钮（免费的赛事也调用此接口）调用此接口", notes = "监护人报名专用，一个孩子多个孩子都用这个接口")
    @PostMapping(value = "/addParticipant", consumes = {"application/json"})
    public JsonResult addParticipant(@Validated @RequestBody MatchParticpantAllDto matchParticpantAllDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        try {
            matchParticpantAllDto.getMatchParticipantDTO().setUserId(userId);
            if (matchParticpantAllDto.getMatchParticipantDTO().getTicketTypeEnum().getType() == 1) {
                if (StringUtils.isNoneBlank(matchApplyAction.addParticipantFree(matchParticpantAllDto, userId))) {
                    return JsonResult.success("报名参赛成功");
                }
            } else if (matchParticpantAllDto.getMatchParticipantDTO().getTicketTypeEnum().getType() == 2) {
                Map<String, String> map = matchApplyAction.addParticipantPay(matchParticpantAllDto, userId);
                if (StringUtils.isNoneBlank(map.get("matchParticipantId"))) {
                    return JsonResult.success("报名参赛成功").addResult("data", map);
                }
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.fail("报名参赛失败");
    }


    @ApiOperation(value = "参赛点击支付按钮（免费的赛事也调用此接口）调用此接口", notes = "用户团队报名专用，不用填头像信息")
    @PostMapping(value = "/addParticipantUserTeam", consumes = {"application/json"})
    public JsonResult addParticipantUserTeam(@Validated @RequestBody MatchParticpantAllDto matchParticpantAllDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        try {
            matchParticpantAllDto.getMatchParticipantDTO().setUserId(userId);
            if (matchParticpantAllDto.getMatchParticipantDTO().getTicketTypeEnum().getType() == 1) {
                if (StringUtils.isNoneBlank(matchFuseAction.addParticipantFreeUserTeam(matchParticpantAllDto, userId))) {
                    return JsonResult.success("报名参赛成功");
                }
            } else if (matchParticpantAllDto.getMatchParticipantDTO().getTicketTypeEnum().getType() == 2) {
                Map<String, String> map = matchApplyAction.addParticipantPayUserTeam(matchParticpantAllDto, userId);
                if (StringUtils.isNoneBlank(map.get("matchParticipantId"))) {
                    return JsonResult.success("报名参赛成功").addResult("data", map);
                }
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作错误");
        }
        return JsonResult.fail("报名参赛失败");
    }

//    @ApiOperation(value = "参赛点击支付按钮（免费的赛事也调用此接口）调用此接口", notes = "监护人个人报名专用")
//    @PostMapping(value = "/addParticipantAlone", consumes = {"application/json"})
//    public JsonResult addParticipantAlone(@Validated @RequestBody MatchParticipantDTO matchParticipantDTO, BindingResult bindingResult, HttpServletRequest request) {
//        if (bindingResult.hasErrors()) {
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        String userId;
//        try {
//            userId = getUserId(request);
//            if (StringUtils.isBlank(userId)) {
//                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(ApiCode.NO_AUTHORIZATION.getMessage());
//        }
//        String token = tokenHelper.getToken(request);
//        if (StringUtils.isBlank(token)) {
//            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
//        }
//        try {
//            matchParticipantDTO.setUserId(userId);
//            if (matchParticipantDTO.getTicketTypeEnum().getType() == 1) {
//                if (StringUtils.isNoneBlank(matchApplyAction.addParticipantFreeAlone(matchParticipantDTO))) {
//                    return JsonResult.success("报名参赛成功");
//                }
//            } else if (matchParticipantDTO.getTicketTypeEnum().getType() == 2) {
//                Map<String, String> map = matchApplyAction.addParticipantPayAlone(matchParticipantDTO, token);
//                if (StringUtils.isNoneBlank(map.get("matchParticipantId"))) {
//                    return JsonResult.success("报名参赛成功").addResult("data", map);
//                }
//            }
//        } catch (RuntimeException e) {
//            return JsonResult.fail(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return JsonResult.fail("报名失败");
//        }
//        return JsonResult.fail("报名参赛失败");
//    }

    @ApiOperation(value = "参赛点击支付按钮（免费的赛事也调用此接口）调用此接口", notes = "用户报名专用，不用传头像，用户名")
    @PostMapping(value = "/addParticipantUser", consumes = {"application/json"})
    public JsonResult addParticipantUser(@Validated @RequestBody MatchParticipantDTO matchParticipantDTO, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        String token = tokenHelper.getToken(request);
        if (StringUtils.isBlank(token)) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            matchParticipantDTO.setUserId(userId);
            if (matchParticipantDTO.getTicketTypeEnum().getType() == 1) {
                if (StringUtils.isNoneBlank(matchApplyAction.addParticipantFreeUser(matchParticipantDTO, userId))) {
                    return JsonResult.success("报名参赛成功");
                }
            } else if (matchParticipantDTO.getTicketTypeEnum().getType() == 2) {
                Map<String, String> map = matchApplyAction.addParticipantPayUser(matchParticipantDTO, token, userId);
                if (StringUtils.isNoneBlank(map.get("matchParticipantId")) && StringUtils.isNoneBlank(map.get("payId"))) {
                    return JsonResult.success("报名参赛成功").addResult("data", map);
                }
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("报名失败");
        }
        return JsonResult.fail("报名参赛失败");
    }


    /**
     * 确认支付参与比赛
     *
     * @return
     */
    @ApiOperation(value = "确认支付参与比赛")
    @PostMapping(value = "/affirm/addParticipant", consumes = {"application/json"})
    public JsonResult affirmAddParticipant(@Validated @RequestBody MatchTicketPayDto matchTicketPayDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        try {
            if (matchFuseAction.updateParticipantPay(matchTicketPayDto, userId)) {
                return JsonResult.success("报名成功");
            }
            return JsonResult.fail("发生错误，报名失败，金额已被冻结，联系管理员解冻。");
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
    }


    //===================================查看新接口=====================================================
    @ApiOperation(value = "获取整个赛事的门票")
    @GetMapping(value = "/getMatchAllTicket")
    public JsonResult getMatchAllTicket(String matchId) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getAllMatchTicketByMatchId(matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取门票信息失败");
        }
    }

    @ApiOperation(value = "通过赛区id筛选门票")
    @GetMapping(value = "/getMatchTicketByZoneId")
    public JsonResult getMatchTicketByZoneId(String zoneId) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getMatchTicketByZoneId(zoneId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取门票信息失败");
        }
    }

    @ApiOperation(value = "通过赛区id和场次id筛选门票")
    @GetMapping(value = "/getMatchTicketByZoneIdAndVenueId")
    public JsonResult getMatchTicketByZoneIdAndVenueId(String zoneId, String venueId) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getMatchTicketByZoneIdAndVenueId(zoneId, venueId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取门票信息失败");
        }
    }

    @ApiOperation(value = "通过赛制id筛选门票")
    @GetMapping(value = "/getMatchTicketByProgressId")
    public JsonResult getMatchTicketByProgressId(String progressId) {
        Map<String, Object> map;
        try {
            map = matchApplyAction.getMatchTicketByProgress(progressId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取门票信息失败");
        }
    }
    @ApiOperation(value = "查看用户创建的比赛")
    @PostMapping(value = "/listSimpleMatchEventList")
    public JsonResult selectSimpleMatchEventList(HttpServletRequest httpServletRequest,@Validated @RequestBody CommonPageDto commonPageDto) {
        if (StringUtils.isBlank(commonPageDto.getUserId())) {
            return JsonResult.fail("请选择用户");
        }
        String myUserId = null;
        Map<String, Object> map;
        Integer pageCount=(commonPageDto.getCurrentPage()-1)*commonPageDto.getSize();
        Integer pageSize=commonPageDto.getSize();
        try {
            myUserId = getUserId(httpServletRequest);
            if (commonPageDto.getUserId().equals(myUserId)) {
                map = matchApplyAction.selectAllMyMatchEvent(myUserId,pageCount,pageSize);
                return JsonResult.success().addResult("data", map);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            map = matchApplyAction.selectSimpleMatchEventList(commonPageDto.getUserId(),pageCount,pageSize);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看比赛信息失败");
        }
    }

    @ApiOperation(value = "查看用户已报名的比赛")
    @GetMapping(value = "/listParticipantMatch")
    public JsonResult selectParticipantMatch(String userId, HttpServletRequest httpServletRequest) {
        String myUserId = null;
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail("请选择用户");
        }
        Map<String, Object> map;
        try {
            myUserId = getUserId(httpServletRequest);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            map = matchApplyAction.selectParticipantMatch(userId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看比赛信息失败");
        }
    }


    @ApiOperation(value = "查看已经通过审核的比赛" )
    @PostMapping(value = "/listApprovedMatchEvent")
    public JsonResult selectApprovedMatchEvent(@RequestBody @Validated CommonPageDto dto, HttpServletRequest httpServletRequest) {
        String myUserId = null;
        try {
            myUserId = getUserId(httpServletRequest);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            List<MatchEventSimpleVo> list = matchApplyAction.selectApprovedMatchEvent(dto);
            return JsonResult.success().addResult("list", list);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看比赛信息失败");
        }
    }
    @ApiOperation(value = "查看用户最近的一个比赛")
    @GetMapping(value = "/selectOneMatchEvent")
    public JsonResult selectOneMatchEvent(String userId,HttpServletRequest request) {
        String myUserId;
        try {
            myUserId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            MatchEventSimpleVo vo = matchApplyAction.selectOneMatchEvent(userId,myUserId);
            return JsonResult.success().addResult("data", vo);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看比赛信息失败");
        }
    }
    @ApiOperation(value = "获取自己购买的门票")
    @GetMapping(value = "/getBuyTicketByUserIdANDMacthId")
    public JsonResult getBuyTicketByUserIdANDMacthId(String matchId, HttpServletRequest request) {
        String userId = null;
        Map<String, List> map;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            map = matchApplyAction.getBuyTicketByUserIdANDMacthId(userId, matchId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取门票信息失败");
        }
    }

    @ApiOperation(value = "查看用户的身高体重网信")
    @GetMapping(value = "/getUserForMatch")
    public JsonResult getUserForMatchVo(HttpServletRequest request) {
        String userId = null;
        Map<String, Object> map;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            map = matchApplyAction.getUserForMatchVo(userId);
            return JsonResult.success().addResult("data", map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取用户信息失败");
        }
    }
    /**
     * 获取团队信息
     */
    @ApiOperation(value = "获得团队成员的信息（监护人下的团队也显示）")
    @PostMapping(value = "/getTeamMembers")
    public JsonResult getTeamMembers(HttpServletRequest request,String participantId
            ,@Validated @RequestBody CommonPageDto matchPageDto) {
        String userId = null;
        Map<String, Object> map;
        try {
            userId = getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            Page<MatchChildInfo> page=new Page<>(matchPageDto.getCurrentPage(),matchPageDto.getSize());
            map = matchApplyAction.getChildDescList(participantId,page);
            return JsonResult.successJsonResult(map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取用户信息失败");
        }
    }

    @ApiOperation(value = "获取自己创建最新的赛事",notes = "传时间，防止缓存")
    @GetMapping(value = "/getNewestMatch")
    public JsonResult getNewestMatchByUserId(String userId,HttpServletRequest request) {
        Map<String, Object> map;
        try {
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            map = matchApplyAction.getNewestMatchByUserId(userId);
            return JsonResult.successJsonResult(map);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);


            return JsonResult.fail("获取用户信息失败");
        }
    }

    @ApiOperation(value = "赛事列表", notes = "通用的赛事列表接口，根据不同的条件返回")
    @PostMapping(value = "/list")
    public JsonResult list(@Validated @RequestBody MatchSearchDTO matchSearchDTO, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            matchSearchDTO.setLat(getLat(requestDto));
            matchSearchDTO.setLon(getLon(requestDto));
            List<MatchIndexVo> list = matchFuseAction.list(matchSearchDTO);
            if(list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "添加比赛公告")
    @PostMapping(value = "/addMatchNotice")
    public JsonResult addMatchNote(@Validated @RequestBody MatchNoticeDTO dto, BindingResult bindingResult ,HttpServletRequest httpServletRequest) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String noticeId = matchApplyAction.insertOrUpdateMatchNotice(dto);
            if (StringUtils.isNoneBlank(noticeId)) {
                return JsonResult.success().addResult("noticeId", noticeId);
            }
            return JsonResult.fail("添加失败");
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            return JsonResult.fail("添加错误");
        }
    }

    @ApiOperation(value = "根据用户查看公告")
    @GetMapping(value = "/getMatchNoticeVosByUserId")
    public JsonResult getMatchNoticeVosByUserId(String userId) {
        try {
            List<MatchNoticeVo> list = matchApplyAction.getMatchNoticeVosByUserId(userId);
            return JsonResult.success().addResult("list", list);
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看错误");
        }
    }

    @ApiOperation(value = "查看比赛的公告")
    @PostMapping(value = "/getMatchNoticeVosByMatchId")
    public JsonResult getMatchNoticeVosByMatchId(@Validated @RequestBody CommonPageDto dto) {
        try {
            return JsonResult.success().addResult("list", matchApplyAction.getMatchNoticeVosByMatchId(dto));
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看错误");
        }
    }


    @ApiOperation(value = "查看比赛的公告")
    @PostMapping(value = "/deleteMatchNotice")
    public JsonResult deleteMatchNotice(@Validated @RequestBody MatchDeleteDto matchDeleteDto, HttpServletRequest httpServletRequest) {
        String userId;
        try {
            userId = getUserId(httpServletRequest);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION.getMessage());
        }
        try {
            boolean flag = matchApplyAction.deleteMatchNotice(matchDeleteDto, userId);
            if (flag) {
                return JsonResult.success("删除成功");
            }
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除错误");
        }
        return JsonResult.fail("删除失败");
    }


}
