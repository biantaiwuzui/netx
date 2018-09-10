package com.netx.common.redis.service.impl;

import com.netx.common.redis.model.TokenModel;
import com.netx.common.redis.service.RedisService;
import com.netx.common.redis.service.TokenRedisService;
import com.netx.common.redis.util.RedisUtils;
import com.netx.common.user.model.RedisUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Token的Redis操作类
 * @author 黎子安
 * @date 2017/10/23
 */
@Service
public class TokenRedisServiceImpl implements TokenRedisService {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String createToken(RedisUser redisUser) throws Exception{
        if(redisUser == null){
            return null;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisUtils.addOrUpdateKey(token,redisUser);
        return token;
        //return updateRedisUser(token,redisUser)?token:null;
    }

    private Map<String,Object> getRedisUserMap(String token,RedisUser redisUser) throws Exception{
        Map temp = redisUtils.hmget(token);
        Field[] declaredFields = redisUser.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.get(redisUser) != null) {
                temp.put(field.getName(), field.get(redisUser));
            }
        }
        return temp;
    }

    private RedisUser getRedisUesrObject(String token,RedisUser redisUser) throws Exception{
        RedisUser temp = getRedisUser(token);
        if(temp==null)temp = new RedisUser();
        if(redisUser.getId()!=null)temp.setId(redisUser.getId());
        if(redisUser.getUserNumber()!=null)temp.setUserNumber(redisUser.getUserNumber());
        if(redisUser.getLastActiveAt()!=null)temp.setLastActiveAt(redisUser.getLastActiveAt());
        if(redisUser.getNickName()!=null)temp.setNickName(redisUser.getNickName());
        if(redisUser.getExpiredAt()!=null)temp.setExpiredAt(redisUser.getExpiredAt());
        if(redisUser.getMobile()!=null)temp.setMobile(redisUser.getMobile());
        if(redisUser.getUserRole()!=null)temp.setUserRole(redisUser.getUserRole());
        if(redisUser.getSex()!=null)temp.setSex(redisUser.getSex());
        return temp;
    }


    @Override
    public void updateRedisUser(String token,RedisUser redisUser) throws Exception{
        RedisUser temp = getRedisUesrObject(token,redisUser);
        redisUtils.addOrUpdateKey(token,temp);
    }

    @Override
    public RedisUser getRedisUser(String token) throws Exception{
        return (RedisUser) redisUtils.getValueJsonRedisUser(token);
    }

    @Override
    public List<RedisUser> getRedisUser(List<String> tokens) throws Exception{
        List<RedisUser> redisUsers = new ArrayList<>();
        RedisUser redisUser;
        for (String token:tokens){
            redisUser = getRedisUser(token);
            if(redisUser==null){
                redisUser = new RedisUser();
            }
            redisUsers.add(redisUser);
        }
        return redisUsers;
    }

    @Override
    public boolean checkToken(TokenModel tokenModel) throws Exception{
        if (tokenModel==null) {
            return false;
        }
        RedisUser redisUser = (RedisUser) redisUtils.getValueJsonRedisUser(tokenModel.getToken());
        if(redisUser==null || redisUser.getId().isEmpty() || !redisUser.getId().equals(tokenModel.getUserId())){
            return false;
        }
        return true;
    }
    @Override
    public void deleteToken(String token) throws Exception {
        redisUtils.deleteString(token);
    }
}
