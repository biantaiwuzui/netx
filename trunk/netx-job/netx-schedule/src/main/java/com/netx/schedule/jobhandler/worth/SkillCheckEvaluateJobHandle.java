package com.netx.schedule.jobhandler.worth;


import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.SkillFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务：是否评价
 *
 */
@JobHandler(value="SkillCheckEvaluateJobHandle")
@Component
public class SkillCheckEvaluateJobHandle extends IJobHandler {

    @Autowired
    SkillFuseAction skillFuseAction;

    @Override
    public ReturnT<String> execute(String skillOrderId) throws Exception {
        XxlJobLogger.log(new Date ()+"："+"定时：是否评价");
        skillFuseAction.checkEvaluate(skillOrderId);
        return SUCCESS;
    }
}

