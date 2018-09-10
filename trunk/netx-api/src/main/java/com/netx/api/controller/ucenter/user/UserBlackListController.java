package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.ucenter.biz.user.UserBlacklistAction;
import com.netx.ucenter.vo.response.SelectUserCommonResponse;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户黑名单模块 控制器类
 * </p>
 *
 * @author 黎子安
 * @since 2017-9-5
 */
@Api(description = "用户黑名单模块")
@RestController
@RequestMapping("/api/userBlackList/")
public class UserBlackListController extends BaseController{
    //======================== 公共属性 start ========================
    @Autowired
    UserBlacklistAction userBlacklistAction;//黑名单服务

    private Logger logger = LoggerFactory.getLogger(UserBlackListController.class);

    //======================== 公共属性 end ========================

    //======================== 黎子安 start ========================
    @ApiOperation("根据黑名单id释放用户(多条以\",\"隔开)")
    @PostMapping("deleteUserBlackList")
    public JsonResult deleteUserBlackList(@Valid @RequestBody List<String> ids){
        if(ids.isEmpty()){
            return JsonResult.fail("黑名单id不能为空");
        }
        try{
            Map<String,Boolean> map = new HashMap<>();
            for(String i:ids){
                map.put(i, userBlacklistAction.deleteUserBlack(i));
            }
            return JsonResult.success().addResult("list",map);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除操作出现异常");
        }
    }

    @ApiOperation(value = "用户黑名单", notes = "添加用户黑名单")
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
            return JsonResult.fail("被拉黑者不能为空");
        }
        if(fromUserId.equals(userId)){
            return JsonResult.fail("不能拉黑自己");
        }
        try {
            String result = userBlacklistAction.addUserBlack(fromUserId,userId);
            return super.getResult(result==null,result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("拉黑异常");
        }
    }

    @ApiOperation(value = "用户黑名单", notes = "根据userId查询用户黑名单")
    @GetMapping(value = "getBlackList")
    public JsonResult getBlackList(String userId) {
        if(StringUtils.isEmpty(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try {
            List<SelectUserCommonResponse> selectUserBlackResponses = userBlacklistAction.selectUserBlackListByUserId(userId);
            return selectUserBlackResponses.size()==0?JsonResult.success("你未有黑名单"):JsonResult.success().addResult("list",selectUserBlackResponses);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询操作出现异常");
        }
    }
    //======================== 黎子安 end ========================
}