package com.netx.api.controller.ucenter.friend;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.DisposeAddFriendMessageRequestDto;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.ucenter.biz.friend.UserFriendAction;
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

/**
 * Create by wongloong on 17-8-25
 */
@RestController
@RequestMapping("/api/userFriend/")
@Api(description = "用户好友模块", hidden = true)
public class UserFriendController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UserFriendController.class);
    @Autowired
    private UserFriendAction userFriendAction;

    @PostMapping("add")
    @ApiOperation(value = "发送添加好友请求")
    public JsonResult sendAddFriendMessage(@RequestParam("userId") String userId, HttpServletRequest request) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.fail("好友id不能为空");
        }
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
            if (fromUserId == null) {
                return JsonResult.fail("用户id不能为空");
            }
            if (fromUserId.equals(userId)) {
                return JsonResult.fail("不能添加自己为好友");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            String result = userFriendAction.saveAddFriendMessage(fromUserId, userId);
            return result == null ? JsonResult.success("发送请求成功！") : JsonResult.fail(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发送好友请求异常");
        }
    }

    @PostMapping("list")
    @ApiOperation(value = "获取用户的添加好友请求")
    public JsonResult queryList(@Valid @RequestBody PageRequestDto page, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (userId == null) {
                return JsonResult.fail("请重新登录后再操作");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            return successResult(userFriendAction.selectPageMap(userId, page.getCurrent(), page.getSize()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取添加好友请求异常");
        }
    }

    @PostMapping("queryFriends")
    @ApiOperation(value = "获取用户的好友列表")
    public JsonResult queryFriends(@Valid @RequestBody PageRequestDto page, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (userId == null) {
                return JsonResult.fail("请重新登录后再操作");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            return successResult(userFriendAction.selectFriendPageMap(userId, page.getCurrent(), page.getSize()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取好友列表异常");
        }
    }

    @PostMapping("get")
    @ApiOperation(value = "查看好友请求的详细数据,并设置为已读")
    public JsonResult queryOne(HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            userFriendAction.getById(userId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看好友请求异常");
        }
    }

    @PostMapping("getStatus")
    @ApiOperation(value = "获取未读的好友请求数量")
    public JsonResult getStatus(HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if (StringUtils.isBlank(userId)) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.success().addResult("count", userFriendAction.getReadCount(userId, false));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取未读的好友请求数量异常");
        }
    }

    @PostMapping("dispose")
    @ApiOperation("处理好友请求,是否同意,0不同意,1同意")
    public JsonResult disposeMessage(@Valid @RequestBody DisposeAddFriendMessageRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if (userId == null) {
                return JsonResult.fail("授权过期，请重新登录", ApiCode.NO_AUTHORIZATION);
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage(), ApiCode.NO_AUTHORIZATION);
        }
        try {
            String result = userFriendAction.disposeMessage(dto, userId);
            return getResult(result == null, "添加好友成功，并已自动关注对方！", result);
        } catch (Exception e) {
            logger.error("处理好友请求错误:" + e.getMessage(), e);
            return JsonResult.fail("处理好友请求错误");
        }
    }

}
