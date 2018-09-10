package com.netx.api.controller.ucenter.user;

//import com.worth.ucenter.user.util.aliyun.ServerSTS;
import com.netx.api.component.UserTokenState;
import com.netx.api.controller.BaseController;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.service.GeoService;
import com.netx.common.router.dto.select.SelectRedisResponseDto;
import com.netx.common.user.util.VoPoConverter;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Token操作控制器
 * @author 黎子安
 * @date 2017/10/23
 */
@Api(description = "token模块测试（前端可以模拟调用）")
@RestController
@RequestMapping("/api/token/")
public class TokenController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Value("${netx.oss.sts.key}")
    private String accessKeyId;
    @Value("${netx.oss.sts.secret}")
    private String accessKeySecret;
    @Value("${netx.oss.sts.role-arn}")
    private String roleArn;

    @Autowired
    RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private void create(){
        redisCache = new RedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    @ApiOperation(value = "获取token信息")
    @GetMapping("getToken")
    public JsonResult getToken(HttpServletRequest request) {
        String token = null;
        try {
            token = getUserId(request);
            if(token==null){
                return JsonResult.fail("请重新登录再进行操作");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try {
            create();
            RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,token);
            Object o = redisCache.get(redisKeyName.getUserKey());
            if(o!=null){
                o = VoPoConverter.copyProperties(o,SelectRedisResponseDto.class);
            }
            return JsonResult.success().addResult("result", o);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("token异常");
        }
    }

    @ApiOperation(value = "token刷新")
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal) {
        String authToken = tokenHelper.getToken( request );
        Device device = DeviceUtils.getCurrentDevice(request);
        if (authToken != null && principal != null) {
            // TODO check user password last update
            String refreshedToken = tokenHelper.refreshToken(authToken, device);
            int expiresIn = tokenHelper.getExpiredIn(device);
            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }

    /**
     * 客户端SDK获取阿里云oss token
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取OSS-STS-TOKEN")
    @PostMapping("getOSSToken")
    public JsonResult getOSSToken(String userId) {
        /*String token = ServerSTS.getSTSAssumeRole(accessKeyId,accessKeySecret,roleArn,userId);
        if (token != null) {
            return Result.newSuccess(token);
        }*/
        return JsonResult.fail("请求失败");
    }
}
