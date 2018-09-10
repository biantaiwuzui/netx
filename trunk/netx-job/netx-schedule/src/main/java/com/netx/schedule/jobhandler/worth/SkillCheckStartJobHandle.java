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
 * 定时任务：技能预约开始时间到了发布者是否已经启动预约
 */
@JobHandler(value = "SkillCheckStartJobHandle")
@Component
public class SkillCheckStartJobHandle extends IJobHandler {

    @Autowired
    SkillFuseAction skillFuseAction;

    @Override
    public ReturnT<String> execute(String skillOrderId) throws Exception {
        XxlJobLogger.log(new Date() + "：" + "发布者是否已经启动预约：" + skillOrderId);
        skillFuseAction.checkRegister(skillOrderId);
        return SUCCESS;
    }
}
