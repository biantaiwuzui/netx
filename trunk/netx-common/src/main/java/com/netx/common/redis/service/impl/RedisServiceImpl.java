package com.netx.common.redis.service.impl;

import com.alibaba.fastjson.JSON;
import com.netx.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis操作实现类
 * @author 黎子安
 * @date 2017/10/21
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Override
    public Object getValueJsonObject(String key,Object object){
        String value=redisTemplate.opsForValue().get(key).toString();
        if (value.isEmpty())return null;
        try {
            return JSON.parseObject(value,object.getClass());
        }catch (Exception e){
            logger.error("getValueJsonObject error:"+e.getMessage(),e);
            return null;
        }
    }

    @Override
    public boolean insertValue(String key,String value,long time){
        try {
            if(time==0l){
                redisTemplate.opsForValue().set(key,value);
            }else{
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.HOURS);
            }
            return true;
        }catch (Exception e){
            logger.error("insertValue error:"+e.getMessage(),e);
            return false;
        }
    }

    @Override
    public boolean reFreshValue(String key,long time){
        try {
            if(time==0l){
                return false;
            }else{
                redisTemplate.boundValueOps(key).expire(time, TimeUnit.HOURS);
            }
            return true;
        }catch (Exception e){
            logger.error("reFreshValue error:"+e.getMessage(),e);
            return false;
        }
    }

    @Override
    public boolean deleteValue(String key){
        try {
            redisTemplate.delete(key);
            return true;
        }catch (Exception e){
            logger.error("delectValue error:"+e.getMessage(),e);
            return false;
        }
    }
}
