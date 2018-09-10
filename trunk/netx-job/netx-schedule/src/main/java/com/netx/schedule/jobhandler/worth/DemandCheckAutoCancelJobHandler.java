package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.DemandFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@JobHandler(value="DemandCheckAutoCancelJobHandler")
@Component
public class DemandCheckAutoCancelJobHandler extends IJobHandler {

    @Autowired
    private DemandFuseAction demandFuseAction;

    @Override
    public ReturnT<String> execute(String demandId) throws Exception {
        XxlJobLogger.log(new Date ()+"："+"定时需求("+demandId+")需求因无入选人而自动取消");
        demandFuseAction.checkDemandAutoCancel (demandId);
        return SUCCESS;
    }
}
