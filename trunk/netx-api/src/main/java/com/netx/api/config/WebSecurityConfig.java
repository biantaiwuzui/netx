package com.netx.api.config;

import com.netx.api.component.CustomUserDetailsService;
import com.netx.api.security.ContentFilter;
import com.netx.api.security.RestAuthenticationEntryPoint;
import com.netx.api.security.TokenAuthenticationFilter;
import com.netx.api.security.TokenHelper;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( jwtUserDetailsService )
                .passwordEncoder( passwordEncoder() );

    }

    /**
     * 跨域请求支持
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("*").allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
            }
        };
    }

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    RedisInfoHolder redisInfoHolder;//redis配置

    @Autowired
    UserAction userAction;//用户服务action

    @Autowired
    CommonServiceProvider commonServiceProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RedisCache redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        http
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                .exceptionHandling().authenticationEntryPoint( restAuthenticationEntryPoint ).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService,redisCache), BasicAuthenticationFilter.class)
                .addFilterBefore(new ContentFilter(commonServiceProvider),BasicAuthenticationFilter.class);
        http.csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/auth/login",
                "/api/user/queryOauth",
                "/api/user/fristOauthLogin",
                "/api/user/oauthLogin",
                "/api/user/add",
                "/api/user/login",
                "/api/user/otherLogin",
                "/api/user/updatePassWord",
                "/api/user/selectUsersByNumberOrMobile",
                "/api/user/selectUsersByUserNumber",
                "/api/user/selectUsers",
                "/api/user/checkLogin",
                "/api/router/otherNum",
                "/api/router/selectUcenterArticleNumByUser",
                "/api/router/selectCurrencyUserDataList",
                "/api/router/selectCurrencyUserDataMapList",
                "/api/router/selectNearUserListByGeom",
                "/api/router/selectNearUserMapByGeom",
                "/api/router/selectNearbyWish",
                "/api/router/selectNearbyLogin",
                "/api/router/selectNearbyGeom",
                "/api/userWatch/selectByUser",
                "/api/userBlackList/getBlackList",
                "/api/MobileCode/sendCode",
                "/api/MobileCode/checkMobileCode",
                "/api/userOnline/selectOnlineUser",
                "/api/userOnline/selectOnlineUserIds",
                "/api/picture/upload",
                "/api/article/selectArticleCountByUserId",
                "/api/article/selectArticleListByDistanceAndTime",
                "/api/article/selectArticleListInTop",
                "/api/userInformation/selectUserProfileByUserId",
                "/api/userManagement/queryUserInSystemBlacklist",
                "/api/WangMing/selectWangMingList",
                "/api/credit/homePage",
                "/api/credit/list",
                "/api/business/seller/list",
                "/api/business/seller/businessHomePage",
                "/api/business/seller/sellerListByDealAmount",
                "/api/business/goods/list",
                "/api/MobileCode/sendCode",
                "/api/MobileCode/checkMobileCode",
                "/api/jpush/*",
                "/api/evaluate/myEvaluateList",
                "/api/UserVerify/matchUserVerify",
                "/api/UserVerify/verifyUserSetting",
                "/api/tags/query",
                "/api/tags/queryPrivate",
                "/api/tags/queryTypeCate",
                "/callback/ali",
                "/callback/wechat",
                "/api/nearly/getUserByType",
                "/api/stat/*",
                "/api/userStat/*",
                "/api/app/check",
                "/api/router/test",
                "/wz/skill//pushTimeout/{type}/{paramId}"
        );
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/api/user/selectUser/*",
				"/api/UserIntegration/selectUserEducation",
                "/api/UserIntegration/selectUserInterest",
                "/api/UserIntegration/selectUserProfession",
                "/api/user/selectUserByIds",
                "/api/user/selectUserMapByIds",
                "/api/UserVerify/selectUserVerify",
                "/api/UserVerify/verifyUserSetting",
                "/api/userInformation/selectUserBaseInfoByUserId",
                "/api/userInformation/selectUserSetting",
                "/api/userInformation/selectProfile",
                "/api/UserIntegration/selectIntegration",
                "/api/friends/listFilter",
                "/api/tags/query",
                "/api/tags/queryPrivate",
                "/api/tags/queryTypeCate",
                "/api/article/selectArticleOtherInfo",
                "/api/userManagement/queryUserInSystemBlacklist",
                "/callback/wechat/**",
                "/api/user/getToken"
        );
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
        web.ignoring().antMatchers(
                HttpMethod.OPTIONS,
                "/auth/login",
                "/api/user/queryOauth",
                "/api/user/fristOauthLogin",
                "/api/user/oauthLogin",
                "/api/user/add",
                "/api/user/login",
                "/api/user/otherLogin",
                "/api/user/updatePassWord",
                "/api/user/selectUsersByNumberOrMobile",
                "/api/user/selectUsersByUserNumber",
                "/api/user/selectUsers",
                "/api/user/checkLogin",
                "/api/router/otherNum",
                "/api/router/selectUcenterArticleNumByUser",
                "/api/router/selectCurrencyUserDataList",
                "/api/router/selectCurrencyUserDataMapList",
                "/api/router/selectNearUserListByGeom",
                "/api/router/selectNearUserMapByGeom",
                "/api/router/selectNearbyWish",
                "/api/router/selectNearbyLogin",
                "/api/router/selectNearbyGeom",
                "/api/userWatch/selectByUser",
                "/api/userBlackList/getBlackList",
                "/api/MobileCode/sendCode",
                "/api/MobileCode/checkMobileCode",
                "/api/userOnline/selectOnlineUser",
                "/api/userOnline/selectOnlineUserIds",
                "/api/picture/upload",
                "/api/article/selectArticleCountByUserId",
                "/api/article/selectArticleListByDistanceAndTime",
                "/api/article/selectArticleListInTop",
                "/api/userInformation/selectUserProfileByUserId",
                "/api/userManagement/queryUserInSystemBlacklist",
                "/api/WangMing/selectWangMingList",
                "/api/credit/homePage",
                "/api/credit/list",
                "/api/business/seller/list",
                "/api/business/seller/businessHomePage",
                "/api/business/seller/sellerListByDealAmount",
                "/api/business/goods/list",
                "/api/MobileCode/sendCode",
                "/api/MobileCode/checkMobileCode",
                "/api/jpush/*",
                "/api/evaluate/myEvaluateList",
                "/api/UserVerify/matchUserVerify",
                "/api/UserVerify/verifyUserSetting",
                "/api/tags/query",
                "/api/tags/queryPrivate",
                "/api/tags/queryTypeCate",
                "/callback/wechat",
                "/callback/ali",
                "/api/nearly/getUserByType",
                "/api/stat/*",
                "/api/userStat/*",
                "/api/app/check"
        );
//        web.ignoring().antMatchers(
//                HttpMethod.GET,
//                "/",
//                "/webjars/**",
//                "/*.html",
//                "/favicon.ico",
//                "/**/*.html",
//                "/**/*.css",
//                "/**/*.js"
//        );
    }
}