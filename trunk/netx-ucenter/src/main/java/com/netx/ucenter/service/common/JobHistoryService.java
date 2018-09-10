package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.JobEnum;
import com.netx.ucenter.mapper.common.JobHistoryMapper;
import com.netx.ucenter.model.common.JobHistory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobHistoryService extends ServiceImpl<JobHistoryMapper, JobHistory> {

    public JobHistory queryJob(JobEnum jobEnum, String typeId, String typeName, String param){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("handler={0}",jobEnum.getName());
        addAndToWrapper("type_id",typeId,wrapper);
        if(StringUtils.isNotBlank(typeName)){
            addAndToWrapper("type_name",typeName,wrapper);  
        }
        addAndToWrapper("param",param,wrapper);
        return selectOne(wrapper);
    }

    private void addAndToWrapper(String key,String value,Wrapper wrapper){
        if(StringUtils.isNotBlank(value)){
            wrapper.and(key+"={0}",value);
        }else{
            wrapper.and(key+" is null",value);
        }
    }
    
    /* 获取某个活动的所有定时任务 **/
    public List<JobHistory> batchDeleteMeetingJob(String typeId){
        EntityWrapper<JobHistory> wrapper = new EntityWrapper<>();
        wrapper.where("type_id={0}",typeId);
        return selectList(wrapper);
    }
}
