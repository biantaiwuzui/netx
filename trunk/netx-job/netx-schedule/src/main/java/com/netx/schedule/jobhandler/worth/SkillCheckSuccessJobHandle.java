package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.SkillFuseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务：验证码、距离是否通过
 */
@JobHandler(value = "SkillCheckSuccessJobHandle")
@Component
public class SkillCheckSuccessJobHandle extends IJobHandler {

    @Autowired
    SkillFuseAction skillFuseAction;


    @Override
    public ReturnT<String> execute(String skillOrderId) throws Exception {
        XxlJobLogger.log(new Date() + "：" + "定时距离验证，订单号：" + skillOrderId);
        skillFuseAction.checkSuccess(skillOrderId);
        return SUCCESS;
    }
}


