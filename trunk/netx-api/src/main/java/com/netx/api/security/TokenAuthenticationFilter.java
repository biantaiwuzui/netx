package com.netx.api.security;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CloudZou on 3/7/2018.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    private TokenHelper tokenHelper;

    private UserDetailsService userDetailsService;

    private RedisCache redisCache;

    private RedisKeyName redisKeyName;

    public TokenAuthenticationFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService, RedisCache redisCache) {
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
        this.redisCache = redisCache;
        redisKeyName = new RedisKeyName("userToken", RedisTypeEnum.HSET_TYPE,null);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String username;
        //从request响应头获取token
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            // get username from token 从token提取用户名
            username = tokenHelper.getUsernameFromToken(authToken);
            if (username != null) {
                //从redis获取旧token
                //验证token唯一
                String oldToken = (String) redisCache.hGet(redisKeyName.getUserKey(),username);
                if(oldToken!=null && oldToken.equals(authToken)){
                    // get user
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (tokenHelper.validateToken(authToken, userDetails)) {
                        // create authentication
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        if (request.getMethod().equals("OPTIONS"))
            response.setStatus(HttpServletResponse.SC_OK);
        else
            chain.doFilter(request, response);
    }

}