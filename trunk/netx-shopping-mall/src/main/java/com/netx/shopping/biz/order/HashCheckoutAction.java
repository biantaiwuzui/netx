package com.netx.shopping.biz.order;

import com.netx.common.redis.util.RedisUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create on 18-01-31
 *
 * @author liwei
 */
@Service("HashCheckoutAction")
public class HashCheckoutAction {

    @Autowired
    RedisUtils redisUtils;

    private String Hash="Hash";

    
    public String getHashByToken(String token){
        //生成hash值
        int Ihash=token.hashCode();
        String Shash= String.valueOf(Ihash);
        //存储到redis 缓存时间1小时
        redisUtils.set(Hash+Shash,Shash,60*60);
        String res= redisUtils.get(Hash+Shash).toString();
        return res;
    }

    
    public boolean hashCheckout(String Shash){

        //判断值是否相同
        boolean res= redisUtils.hasKey(Hash+Shash);
        if (res){//删除掉key
            redisUtils.del(Hash+Shash);
            return true;
        }
        return false;
    }
}
