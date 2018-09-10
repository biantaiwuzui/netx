package com.netx.shopping.biz.order;

import com.netx.common.redis.util.RedisUtils;
import com.netx.common.vo.business.AddDeliverymanRequestDto;
import com.netx.common.vo.business.DelectDeliverymanRequestDto;
import com.netx.common.vo.business.GetDeliverymanRequestDto;
import com.netx.common.vo.business.UpdateDeliverymanRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create on 18-01-31
 *
 * @author liwei
 */
@Service("DeliverymanAction")
public class DeliverymanAction {

    private String DE="DE";

    @Autowired
    RedisUtils redisUtils;

    
    public List<Object> getDeliverymanList(GetDeliverymanRequestDto request){
        List<Object> deliveryman= redisUtils.lGet(DE+request.getSellerId(),0,-1);
        if (deliveryman.size()>0)
        {
            return deliveryman;
        }
        return null;
    }

    
    public List<String> add(AddDeliverymanRequestDto request){
        redisUtils.lSet(DE+request.getSellerId(),request.getDeliveryman());
        return request.getDeliveryman();
    }

    
    public boolean delete(DelectDeliverymanRequestDto request){
        List<Object> deliveryman= redisUtils.lGet(DE+request.getSellerId(),0,-1);
        int i=(int)request.getIndex();
        redisUtils.lRemove(DE+request.getSellerId(),request.getIndex()+1,deliveryman.get(i));
        return true;
    }

    
    public boolean update(UpdateDeliverymanRequestDto request){
        redisUtils.lUpdateIndex(DE+request.getSellerId(),request.getIndex(),request.getDeliveryman());
        return true;
    }
}
