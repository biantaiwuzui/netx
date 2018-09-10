package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonCostSettingMapper;
import com.netx.ucenter.model.common.CommonCostSetting;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-3
 */
@Service
public class CostService extends ServiceImpl<CommonCostSettingMapper, CommonCostSetting>{

    public List<CommonCostSetting> selectByState(Integer state){
        return this.selectList(createWrapper(state));
    }

    private Wrapper createWrapper(Integer state){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("state = {0}",state);
        wrapper.orderBy("dispose_time desc");
        return wrapper;
    }

    public CommonCostSetting selectNewlyByState(Integer state){
        return this.selectOne(createWrapper(state));
    }

    public Map<String,Object> selectNewlyByState(String sqlField,Integer state){
        Wrapper wrapper = createWrapper(state);
        wrapper.setSqlSelect(sqlField);
        return this.selectMap(wrapper);
    }

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonCostSetting>().where("dispose_user={0}",userId));
    }
    public List<CommonCostSetting> queryListforBoss(){
        EntityWrapper wrapper=new EntityWrapper();
        return selectList(wrapper);
    }

}
