package com.netx.api.controller.ucenter.friend;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.common.FriendsPageRequestDto;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.common.vo.common.QueryFriendListRequestDto;
import com.netx.common.vo.friends.FriendsListResponseVo;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.friend.FriendsAction;
import com.netx.utils.json.ApiCode;
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
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-8-27
 */
@Api(description = "好友列表")
@RestController
@RequestMapping("/api/friends")
public class FriendsController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(FriendsController.class);
    @Autowired
    FriendsAction friendsAction;
    @Autowired
    MessagePushAction messagePushAction;

    @ApiOperation(value = "获取用户好友列表")
    @PostMapping("/list")
    public JsonResult getFriendList(@Valid @RequestBody FriendsPageRequestDto page, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo = getGeoFromRequest(request);
            if (userGeo.getUserId() == null) {
                return JsonResult.fail("请重新登录后再操作");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            Map<String,Object> friendsList=friendsAction.getFriendsListByMasterId(userGeo.getUserId(), page.getCurrent(), page.getSize(), page.isFriendVerify(), page.getCredit(),userGeo.getLon(), userGeo.getLat());
            if (friendsList.get("list")==null&&(int)friendsList.get("total")==1){
                return JsonResult.fail("暂无认证完整的好友！");
            }
                return JsonResult.successJsonResult(friendsList);
        } catch (Exception e) {
            logger.error("获取好友失败：" + e.getMessage(), e);
            return JsonResult.fail("获取好友失败");
        }
    }

    @ApiOperation(value = "判断是否好友")
    @PostMapping("/check")
    public JsonResult check(@RequestParam("userId") String userId, HttpServletRequest request) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail("用户id不能为空");
        }
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
            if (StringUtils.isBlank(fromUserId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.success().addResult("status", friendsAction.isFriend(fromUserId, userId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("验证好友关系异常");
        }
    }

    @ApiOperation(value = "获取过滤用户好友列表")
    @GetMapping("/listFilter")
    public JsonResult getFriendListByFilter(@RequestBody QueryFriendListRequestDto requestDto, HttpServletRequest request) {
        if (StringUtils.isBlank(requestDto.getUserId())) {
            try {
                requestDto.setUserId(getUserId(request));
                if (StringUtils.isBlank(requestDto.getUserId())) {
                    return JsonResult.fail("用户id不能为空");
                }
            } catch (Exception e) {
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            List<FriendsListResponseVo> friendsListResponses = friendsAction.getFriendsListByMasterIdAndFilter(requestDto.getUserId(), requestDto.getFitlerName(), requestDto.getCurrent(), requestDto.getSize());
            return JsonResult.success().addResult("list", friendsListResponses);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取好友失败");
        }
    }

    @ApiOperation(value = "删除好友", notes = "按好友的用户id删除")
    @PostMapping("delete")
    public JsonResult delete(@RequestParam("userId") String userId, HttpServletRequest request) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail("被删除的用户id不能空");
        }
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
            if (StringUtils.isBlank(fromUserId)) {
                return JsonResult.fail("请先登录再操作");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            if (friendsAction.isFriend(fromUserId, userId)) {
                return friendsAction.delFriends(fromUserId, userId) ? JsonResult.success("删除成功") : JsonResult.fail("删除失败");
            }
            return JsonResult.fail("你们不是好友关系");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除好友异常");
        }
    }
}
