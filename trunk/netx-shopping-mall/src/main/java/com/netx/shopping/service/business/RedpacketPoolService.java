package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerRedpacketPoolMapper;
import com.netx.shopping.model.business.SellerRedpacketPool;
import org.springframework.stereotype.Service;

/**
 * Created by liwei on 3/1/2018.
 */
@Service("oldRedpacketPoolService")
public class RedpacketPoolService  extends ServiceImpl<SellerRedpacketPoolMapper,SellerRedpacketPool> {
    /**
     * 获取数据
     * @return
     */
    public SellerRedpacketPool get(){
        EntityWrapper wrapper=new EntityWrapper();
        return this.selectOne(wrapper);
    }

    /**
     * 修改
     * @return
     */
    public boolean update(SellerRedpacketPool redpacketPool){
        return this.update(redpacketPool, new EntityWrapper<>());
    }
}
