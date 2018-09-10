package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.DemandFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 需求评论
 *
 */
@JobHandler(value="DemandCheckStartHandler")
@Component
public class DemandCheckStartHandler extends IJobHandler {

    @Autowired
    DemandFuseAction demandFuseAction;

    @Override
    public ReturnT<String> execute(String demandOrderId) throws Exception {
        XxlJobLogger.log(new Date ()+"："+"检查启动情况，报名方是否确认启动失败");
        demandFuseAction.checkStart(demandOrderId);
        return SUCCESS;
    }
}

