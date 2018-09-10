package com.netx.shopping.service.redpacketcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.redpacketcenter.RedpacketSendMapper;
import com.netx.shopping.model.redpacketcenter.RedpacketSend;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  购物车详情服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service("newRedpacketSendService")
public class RedpacketSendService extends ServiceImpl<RedpacketSendMapper, RedpacketSend> {
    /**
     * 获取红包发放列表
     * @param startTime
     * @return
     */
    public List<RedpacketSend> getRedpacketSendList(Date startTime){
        return this.selectList(createEntityWrapper(startTime,">="));
    }

    private EntityWrapper createEntityWrapper(Date startTime, String jude){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("create_time"+jude+"{0}", startTime).orderBy("create_time desc");
        return wrapper;
    }

    /**
     * 获取红包发放详情
     * @param startTime
     * @return
     */
    public RedpacketSend getRedpacketSend(Date startTime,String jude){
        return this.selectOne(createEntityWrapper(startTime,jude));
    }

    public Integer countRedpackSend(Date startTime,String jude){
        return this.selectCount(createEntityWrapper(startTime,jude));
    }
}
