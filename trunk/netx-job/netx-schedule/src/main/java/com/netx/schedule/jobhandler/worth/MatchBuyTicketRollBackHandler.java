package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.MatchFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 大赛购票回滚
 * ticketId门票的编号
 */
@JobHandler(value="MatchBuyTicketRollBackHandler")
@Component
public class MatchBuyTicketRollBackHandler  extends IJobHandler {
    @Autowired
    private MatchFuseAction matchFuseAction;
    @Override
    public ReturnT<String> execute(String ticketId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"购票的时候，未及时付款，将票数回滚。");
        matchFuseAction.matchTicketRollBack(ticketId);
        return SUCCESS;
    }

}
