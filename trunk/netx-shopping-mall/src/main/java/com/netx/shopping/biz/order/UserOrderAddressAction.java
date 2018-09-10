package com.netx.shopping.biz.order;

import com.netx.common.redis.util.RedisUtils;
import com.netx.common.vo.business.AddOrUpdateRequestDto;
import com.netx.common.vo.business.DelectAddressRequestDto;
import com.netx.common.vo.business.UpdateAddressRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create on 18-01-29
 *
 * @author liwei
 */
@Service("UserOrderAddressAction")
public class UserOrderAddressAction {

    private String DZ="DZ";

    @Autowired
    RedisUtils redisUtils;

    
    public List<Object> getUserOrderAddressList(String userId){
        List<Object> userOrderDz= redisUtils.lGet(DZ+userId,0,-1);
        if (userOrderDz.size()>0)
        {
            return userOrderDz;
        }
        return null;
    }

    
    public List<String> addorUpdate(AddOrUpdateRequestDto request){

        redisUtils.lSet(DZ+request.getUserId(),request.getAddress());
        return request.getAddress();
    }

    
    public boolean delete(DelectAddressRequestDto request){
        List<Object> userOrderDz= redisUtils.lGet(DZ+request.getUserId(),0,-1);
        int i=(int)request.getIndex();
        redisUtils.lRemove(DZ+request.getUserId(),request.getIndex()+1,userOrderDz.get(i));
        return true;
    }

    
    public boolean update(UpdateAddressRequestDto request){
        redisUtils.lUpdateIndex(DZ+request.getUserId(),request.getIndex(),request.getAddress());
        return true;
    }

}
