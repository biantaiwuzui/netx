package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonLuckyMoneyMapper;
import com.netx.ucenter.model.common.CommonLuckyMoney;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Create by wongloong on 17-9-8
 */
@Service
public class LuckyMoneyService extends ServiceImpl<CommonLuckyMoneyMapper, CommonLuckyMoney>{

    public List<CommonLuckyMoney> query(Integer status, String time) throws Exception {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.andNew("status={0}",status);
        if (StringUtils.isEmpty(time)) {
            entityWrapper.orderBy("send_time asc");
        } else {
            entityWrapper.andNew("send_time>={0}", time).orderBy("send_time asc");
        }
        return this.selectList(entityWrapper);
    }

    public List<String> getIdsByStatus(Integer status){
        Wrapper wrapper = buildWrapperByStatus(status);
        wrapper.setSqlSelect("id");
        return this.selectObjs(wrapper);
    }

    private Wrapper buildWrapperByStatus(Integer status){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("status={0}",status);
        return wrapper;
    }

    public List<CommonLuckyMoney> getWzCommonLuckyMoneysByStatus(Integer status){
        Wrapper wrapper = buildWrapperByStatus(status);
        return this.selectList(wrapper);
    }

    public Boolean deleteByStatus(Integer status){
        Wrapper wrapper = buildWrapperByStatus(status);
        return delete(wrapper);
    }

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonLuckyMoney>().eq("examine_user_id",userId));
    }
}

