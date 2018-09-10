package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.DemandFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value = "DemandCheckPassCodeJobHandler")
@Component
public class DemandCheckPassCodeJobHandler extends IJobHandler {
    @Autowired
    private DemandFuseAction demandFuseAction;

    @Override
    public ReturnT<String> execute(String demandId) throws Exception {
        XxlJobLogger.log ( new Date () +" : "+"定时需求( "+ demandId + " )需求开始30分钟后无入选者的验证码通过,需求失败." );
        demandFuseAction.checkCodePass(demandId);
        return SUCCESS;
    }
}
