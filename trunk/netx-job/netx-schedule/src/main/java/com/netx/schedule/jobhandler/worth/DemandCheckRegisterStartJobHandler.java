package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.DemandFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value="DemandCheckRegisterStartJobHandler")
@Component
public class DemandCheckRegisterStartJobHandler extends IJobHandler {

    @Autowired
    private DemandFuseAction demandFuseAction;

    @Override
    public ReturnT<String> execute(String demandId) throws Exception {
        XxlJobLogger.log(new Date ()+"："+"检测需求("+demandId+")开始前60分钟入选者是否已启动需求");
        demandFuseAction.checkSixtyMinuteStart (demandId);
        return SUCCESS;
    }
}
