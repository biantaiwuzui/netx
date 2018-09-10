package com.netx.api.component;

import com.netx.common.common.enums.RedisKey;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by CloudZou on 3/7/2018.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    UserAction userAction;

    private RedisCache redisCache;
    //连接redis
    @Autowired
    public CustomUserDetailsService(RedisInfoHolder redisInfoHolder){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //Redis查询
        RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,id);
        User user = (User) redisCache.get(redisKeyName.getUserKey());
        logger.info("Redis查询结果："+user);
        if(user==null){
            //数据库查询
            user = userAction.getUserService().selectById(id);
            logger.info("数据库查询结果："+user);
            if(user!=null){
                //将数据库的存回redis
                logger.info("将数据库查询结果更新到redis");
                redisCache.put(redisKeyName.getUserKey(),user);
            }
        }
        if (user!=null) {
            TokenUser tokenUser = new TokenUser();
            tokenUser.setUsername(user.getId());
            tokenUser.setPassword(user.getPassword());
            return tokenUser;
        } else {
            throw new BadCredentialsException("用户名或者密码错误");
        }
    }


}
