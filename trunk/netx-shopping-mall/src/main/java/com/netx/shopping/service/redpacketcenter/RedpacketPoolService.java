package com.netx.shopping.service.redpacketcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.redpacketcenter.RedpacketPoolMapper;
import com.netx.shopping.model.redpacketcenter.RedpacketPool;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  购物车详情服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service("newRedpacketPoolService")
public class RedpacketPoolService extends ServiceImpl<RedpacketPoolMapper, RedpacketPool> {

    /**
     * 修改
     * @return
     */
    public boolean updateByRedpacketPool(RedpacketPool redpacketPool){
        return this.update(redpacketPool, new EntityWrapper<>());
    }

    /**
     * 获取数据
     * @return
     */
    public RedpacketPool get(){
        EntityWrapper wrapper=new EntityWrapper();
        return this.selectOne(wrapper);
    }
}
