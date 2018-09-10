package com.netx.api.controller.ucenter.user;

import com.netx.common.user.dto.user.OnlineUserRequestDto;
import com.netx.common.user.dto.user.ScreenUserRequestDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "查询用户集模块")
@RestController
@RequestMapping("/api/userOnline/")
public class UserOnlineController {
    @Autowired
    UserAction userService;//用户信息服务
    private Logger logger = LoggerFactory.getLogger(UserOnlineController.class);

    @ApiOperation(value = "获取在线用户(不含访问者)",notes = "如果userId为空，则查询全部,time为时间（min）")
    @PostMapping(value = "selectOnlineUser")
    public JsonResult selectOnlineUser(@RequestBody OnlineUserRequestDto request) {
        try {
            List<UserSynopsisData> list = userService.getOnlineUser(null,getOnlineTime(request.getTime()));
            if(!StringUtils.isEmpty(request.getUserId())){
                for (int i=0;i<list.size();i++) {
                    if(list.get(i).getId().equals(request.getUserId())){
                        list.remove(i);
                        break;
                    }
                }
            }
            return JsonResult.success().addResult("list",list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    private long getOnlineTime(Long time){
        if(time==null){
            time=1l;
        }else if(time==-1){
            return time;
        }
        return System.currentTimeMillis()-time*60*1000l;
    }

    @ApiOperation(value = "获取在线用户ids",notes = "如果userId为空，则查询全部,time为时间（min）")
    @PostMapping(value = "selectOnlineUserIds")
    public JsonResult selectOnlineUserIds(@RequestBody OnlineUserRequestDto request) {
        try {
            List<String> ids = userService.getOnlineUserIds(null,getOnlineTime(request.getTime()));
            if(!StringUtils.isEmpty(request.getUserId())){
                ids.remove(request.getUserId());
            }
            return JsonResult.success().addResult("list",ids);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }

    }

    @ApiOperation(value = "在线用户进行筛选",notes = "不限,time传-1")
    @PostMapping(value = "screenOnlineUser")
    public JsonResult screenOnlineUser(@RequestBody ScreenUserRequestDto requestDto) {
        try {
            return JsonResult.success().addResult("list",userService.getOnlineUser(requestDto.getUserIds(), getOnlineTime(requestDto.getTime())));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "在线用户ids进行筛选",notes = "不限,time传-1")
    @PostMapping(value = "screenOnlineUserIds")
    public JsonResult screenOnlineUserIds(@RequestBody ScreenUserRequestDto requestDto) {
        try {
            return JsonResult.success().addResult("list",userService.getOnlineUserIds(requestDto.getUserIds(), getOnlineTime(requestDto.getTime())));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }
}
