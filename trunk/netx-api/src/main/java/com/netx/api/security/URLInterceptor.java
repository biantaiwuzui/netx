package com.netx.api.security;

import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

public class URLInterceptor implements HandlerInterceptor  {

    private TokenHelper tokenHelper;

    private UserAction userAction;

    private RedisCache redisCache;

    private Logger logger = LoggerFactory.getLogger("URLInterceptor");

    public URLInterceptor(RedisCache redisCache, TokenHelper tokenHelper, UserAction userAction) {
        logger.info("拦截构造");
        this.tokenHelper = tokenHelper;
        this.userAction = userAction;
        this.redisCache = redisCache;
    }

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle run!");
        try {
            String token = tokenHelper.getToken(httpServletRequest);
            if (token != null) {
                String userId = tokenHelper.getUsernameFromToken(token);
                if (userId != null) {
                    logger.info("redis获取");
                    RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,userId);
                    User user = (User) redisCache.get(redisKeyName.getUserKey());
                    logger.error("----------从redis用户取数据---------------");
                    if (user == null) {
                        logger.info("数据库获取");
                        user = userAction.getUserService().selectById(userId);
                    }
                    if (user != null) {
                        if (user.getNickname()==null||user.getRealName()==null||user.getUserNumber()==null||user.getCredit()==null){
                            logger.info("用户数据丢失，数据库获取");
                            user = userAction.getUserService().selectById(userId);
                        }
                        logger.info(userId+"位置更新");
                        Double lon = parseDouble(httpServletRequest.getParameter("lon"));
                        Double lat = parseDouble(httpServletRequest.getParameter("lat"));
                        user.setLastActiveAt(new Date());//用于更新用户最后操作时间
                        if (checkLocation(lon, 180) && checkLocation(lat, 90)) {
                            user.setLat(new BigDecimal(lat));
                            user.setLon(new BigDecimal(lon));
                        }
                        redisCache.put(redisKeyName.getUserKey(), user);
                        logger.error("------------从redis放用户数据---------------"+user.toString());
                        userAction.getUserService().updateById(user);
                        logger.info("位置同步完毕");
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        logger.info("执行完毕");
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("postHandle run!");
    }
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("afterCompletion run!");
    }

    private Boolean checkLocation(Double location,double value){
        return location!=0 && location >= -value && location <= value;
    }

    private Double parseDouble(String value) {
        Double result = 0.0;
        if(!StringUtils.isEmpty(value)){
            try {
                result = new Double(value);
            } catch (Exception e) {
//            logger.error(e.getMessage(), e);
                logger.warn("值转换异常");
            }
        }
        return result;
    }
}
