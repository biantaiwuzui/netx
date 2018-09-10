package com.netx.api.config;

import com.netx.api.security.TokenHelper;
import com.netx.api.security.URLInterceptor;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.utils.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private UserAction userAction;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    @Bean
    public HandlerInterceptor getMyInterceptor(){
        RedisCache redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return new URLInterceptor (redisCache,tokenHelper,userAction);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(new String[]{"/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"});
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/user/add")
                .allowedOrigins("http://wzubi.com/reg")
                .allowedMethods("POST");


    }



}
