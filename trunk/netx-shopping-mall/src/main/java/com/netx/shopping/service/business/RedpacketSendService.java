package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerRedpacketSendMapper;
import com.netx.shopping.model.business.SellerRedpacketSend;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service("oldRedpacketSendService")
public class RedpacketSendService extends ServiceImpl<SellerRedpacketSendMapper,SellerRedpacketSend> {

    /**
     * 获取红包发放列表
     * @param startTime
     * @return
     */
    public List<SellerRedpacketSend> getRedpacketSendList(Date startTime){
        return this.selectList(createEntityWrapper(startTime,">="));
    }

    private EntityWrapper createEntityWrapper(Date startTime,String jude){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("create_time"+jude+"{0}", startTime).orderBy("create_time desc");
        return wrapper;
    }

    /**
     * 获取红包发放详情
     * @param startTime
     * @return
     */
    public SellerRedpacketSend getRedpacketSend(Date startTime,String jude){
        return this.selectOne(createEntityWrapper(startTime,jude));
    }

    public Integer countRedpackSend(Date startTime,String jude){
        return this.selectCount(createEntityWrapper(startTime,jude));
    }
}
