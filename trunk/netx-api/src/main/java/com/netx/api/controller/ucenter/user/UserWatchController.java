package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.ucenter.biz.user.UserWatchAction;
import com.netx.ucenter.vo.request.UserTypeRequest;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户关注模块 控制器类
 * </p>
 *
 * @author 黎子安
 * @since 2017-9-10
 */
@Api(description = "用户关注模块")
@RestController
@RequestMapping("/api/userWatch/")
public class UserWatchController extends BaseController {

    @Autowired
    UserWatchAction userWatchService;

    private Logger logger = LoggerFactory.getLogger(UserWatchController.class);

    private boolean checkListEmpty(List list){
        return list!=null && list.size()!=0;
    }

    @ApiOperation(value = "查询关注用户列表", notes = "按用户id：查询我关注的,输入type:user;查询关注我的,输入type:toUser;")
    @PostMapping("selectByUser")
    public JsonResult selectByUser(@Valid @RequestBody UserTypeRequest dto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        if(StringUtils.isEmpty(dto.getUserId())){
            try{
                userGeo = getGeoFromRequest(request);
                if(userGeo.getUserId()==null){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }else{
            userGeo.setUserId(dto.getUserId());
            userGeo.setLat(getLat(request));
            userGeo.setLon(getLon(request));
        }
        try{
            List<UserSynopsisData> listUser=userWatchService.selectUserByType(userGeo.getUserId(),dto.getType(),userGeo.getLon(),userGeo.getLat());
            return checkListEmpty(listUser)?JsonResult.success().addResult("list",listUser):JsonResult.success("没有关注信息");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ApiOperation(value = "查询未关注用户列表", notes = "按用户id查询我没关注的")
    @GetMapping("selectNoWatchByUser")
    public JsonResult selectNoWatchByUser(String userId,HttpServletRequest request) {
        if(StringUtils.isEmpty(userId)){
            try {
                userId = getUserId(request);
                if(StringUtils.isEmpty(userId)){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            List<UserSynopsisData> listUser=userWatchService.selectNoWatchUserByUserId(userId);
            return checkListEmpty(listUser)?JsonResult.success().addResult("list",listUser):JsonResult.success("没有关注信息");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ApiOperation(value = "关注", notes = "添加发起用户关注")
    @PostMapping(value = "add")
    public JsonResult add(@RequestParam("userId") String userId, HttpServletRequest request) {
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
            if(fromUserId==null){
                return JsonResult.fail("用户id不能为空");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(userId)){
            return JsonResult.fail("被关注者不能为空");
        }
        if(fromUserId.equals(userId)){
            return JsonResult.fail("不能关注自己");
        }
        try {
            String result = userWatchService.addUserWatch(fromUserId,userId);
            return new JsonResult(result.contains("关注成功"),result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("关注异常");
        }

    }
    //======================== 黎子安 end ========================

}
