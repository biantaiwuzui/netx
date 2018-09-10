package com.netx.shopping.biz.ordercenter;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.util.RedisUtils;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.sign.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 哈希
 * @author 黎子安
 * @since 2018-05-07
 */
@Service("newHashCheckoutAction")
public class HashCheckoutAction {

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private final String Hash="Hash";

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    /**
     * 生成hash
     * @param userId
     * @param token
     * @return
     */
    public String getHashByToken(String userId,String token){
        //生成hash值
        long Ihash=token.hashCode()+(long)(Math.random()*System.currentTimeMillis());
        String hash= Base64.encode((Ihash+"").getBytes()).replace("[B@", "").replace("=","");
        RedisKeyName redisKeyName = new RedisKeyName(Hash, RedisTypeEnum.STRING_TYPE,userId);
        clientRedis().put(redisKeyName.getOrderKey(),hash,60*60*1000);
        return hash;
    }

    /**
     * 判断hash
     * @param userId
     * @param hash
     * @return
     */
    public boolean hashCheckout(String userId,String hash){
        RedisKeyName redisKeyName = new RedisKeyName(Hash, RedisTypeEnum.STRING_TYPE,userId);
        String redisHash = clientRedis().getRaw(redisKeyName.getOrderKey());
        if(StringUtils.isNotBlank(redisHash)&&redisHash.equals(hash)){
            clientRedis().remove(redisKeyName.getOrderKey());
            return true;
        }
        return false;
    }
}
