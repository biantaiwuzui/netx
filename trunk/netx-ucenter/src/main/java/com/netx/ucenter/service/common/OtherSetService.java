package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonOtherSetMapper;
import com.netx.ucenter.model.common.CommonOtherSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by wongloong on 17-9-4
 */
@Service
public class OtherSetService extends ServiceImpl<CommonOtherSetMapper, CommonOtherSet>{
    private Logger logger = LoggerFactory.getLogger(OtherSetService.class);
    @Autowired
    CommonOtherSetMapper wzCommonOtherSetMapper;

    public CommonOtherSetMapper getWzCommonOtherSetMapper() {
        return wzCommonOtherSetMapper;
    }

    public CommonOtherSet getWzCommonOtherSetOne(){
        return this.selectOne(new EntityWrapper<>());
    }

    public CommonOtherSet query(Integer type,Boolean isAsc){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("can_use={0}",type);
        wrapper.orderBy("create_time"+getAscStr(isAsc));
        return this.selectOne(wrapper);
    }

    private String getAscStr(Boolean isAsc){
        return isAsc?"":" desc";
    }

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonOtherSet>().eq("dispose_user_id",userId));
    }
}
