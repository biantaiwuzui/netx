package com.netx.ucenter.biz.common;

import com.netx.ucenter.model.common.JobHistory;
import com.netx.ucenter.service.common.JobHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobHistoryAction {

    private Logger logger = LoggerFactory.getLogger(JobHistoryAction.class);

    @Autowired
    private JobHistoryService jobHistoryService;

    public Boolean addJobHistory(Integer jobId, String param, String typeId, String typeName, String handler){
        JobHistory jobHistory = new JobHistory();
        jobHistory.setJobId(jobId);
        jobHistory.setParam(param);
        jobHistory.setTypeId(typeId);
        jobHistory.setTypeName(typeName);
        jobHistory.setCreateUserId("superAdmin");
        jobHistory.setHandler(handler);
        return jobHistoryService.insert(jobHistory);
    }

}