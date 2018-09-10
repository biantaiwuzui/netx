package com.netx.boss.components;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserAdminAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserAdmin;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by CloudZou on 3/7/2018.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    UserAdminAction userAdminAction;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAdmin user = userAdminAction.queryAdmin(username);
        if (user!=null) {
            TokenUser tokenUser = new TokenUser();
            tokenUser.setUsername(user.getUserName());
            tokenUser.setPassword(user.getPassword());
            return tokenUser;
        } else {
            throw new BadCredentialsException("用户名或者密码错误");
        }
    }


}
