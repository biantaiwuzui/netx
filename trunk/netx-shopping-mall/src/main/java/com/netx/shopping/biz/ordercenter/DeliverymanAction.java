package com.netx.shopping.biz.ordercenter;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.vo.business.AddDeliverymanRequestDto;
import com.netx.common.vo.business.GetDeliverymanRequestDto;
import com.netx.common.vo.business.UpdateDeliverymanRequestDto;
import com.netx.shopping.vo.DelectDeliverymanRequestDto;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create on 18-01-31
 *
 * @author liwei
 */
@Service("newDeliverymanAction")
public class DeliverymanAction {

    private final String DE="DE";

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    private RedisKeyName getRedisKeyName(String merchatId){
        return new RedisKeyName(DE, RedisTypeEnum.LIST_TYPE,merchatId);
    }
    
    public List<Object> getDeliverymanList(GetDeliverymanRequestDto request){
        List<Object> deliveryman = clientRedis().lRange(getRedisKeyName(request.getSellerId()).getSellerKey(),0,-1);
        return deliveryman.size()>0?deliveryman:null;
    }

    public List<String> add(AddDeliverymanRequestDto request){
        clientRedis().lpush(getRedisKeyName(request.getSellerId()).getSellerKey(),request.getDeliveryman());
        return request.getDeliveryman();
    }

    public boolean delete(DelectDeliverymanRequestDto request){
        RedisKeyName redisKeyName = getRedisKeyName(request.getSellerId());
        clientRedis().lRem(redisKeyName.getSellerKey(),0,request.getValue());
        return true;
    }

    
    public boolean update(UpdateDeliverymanRequestDto request){
        RedisKeyName redisKeyName = getRedisKeyName(request.getSellerId());
        clientRedis().lset(redisKeyName.getOrderKey(),request.getIndex(),request.getDeliveryman());
        return true;
    }
}
