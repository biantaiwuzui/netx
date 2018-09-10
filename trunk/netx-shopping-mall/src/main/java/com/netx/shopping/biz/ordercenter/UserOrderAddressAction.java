package com.netx.shopping.biz.ordercenter;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.vo.business.AddOrUpdateRequestDto;
import com.netx.common.vo.business.AddressRequestDto;
import com.netx.common.vo.business.UpAddressRequestDto;
import com.netx.common.vo.business.UpdateAddressRequestDto;
import com.netx.shopping.vo.DelectAddressRequestDto;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单地址
 * @author 黎子安
 * @since 2018-05-07
 */
@Service("newUserOrderAddressAction")
public class UserOrderAddressAction {

    private Logger logger = LoggerFactory.getLogger(UserOrderAddressAction.class);

    private String DZ="DZ";

    private RedisCache redisCache;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisKeyName getRedisKeyName(String userId){
        return new RedisKeyName(DZ, RedisTypeEnum.LIST_TYPE,userId);
    }

    public RedisCache clientReidsCache(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public List<UpAddressRequestDto> getUserOrderAddressList(String userId){
        List<UpAddressRequestDto> userOrderDz = getList(getRedisKeyName(userId),0,-1);
        if(userOrderDz.size()>0){
            int index=0;
            for(UpAddressRequestDto upAddressRequestDto:userOrderDz){
                upAddressRequestDto.setId(index++);
            }
        }
        return userOrderDz;
    }

    private List<UpAddressRequestDto> getList(RedisKeyName redisKeyName,int start,int end){
        return (List<UpAddressRequestDto>)(List) clientReidsCache().lRange(redisKeyName.getOrderKey(),start,end);
    }
    
    public void addorUpdate(UpAddressRequestDto request,String userId){
        RedisKeyName redisKeyName = getRedisKeyName(userId);
        clientReidsCache();
        if(request.getId()==null){
            add(redisKeyName,request);
        }else{
            update(redisKeyName,request.getId(),request);
        }
    }

    private void add(RedisKeyName redisKeyName,AddressRequestDto addressRequestDto){
        redisCache.lRem(redisKeyName.getOrderKey(),0,addressRequestDto);
        redisCache.rpush(redisKeyName.getOrderKey(),addressRequestDto);
    }

    private void update(RedisKeyName redisKeyName,long id,AddressRequestDto addressRequestDto){
        redisCache.lset(redisKeyName.getOrderKey(),id,addressRequestDto);
    }

    
    public boolean delete(int index,String userId){
        RedisKeyName redisKeyName = getRedisKeyName(userId);
        List<UpAddressRequestDto> userOrderDz = getList(redisKeyName,index,index);
        if(userOrderDz.size()>0){
            redisCache.lRem(redisKeyName.getOrderKey(),0,userOrderDz.get(0));
        }
        return true;
    }

}
