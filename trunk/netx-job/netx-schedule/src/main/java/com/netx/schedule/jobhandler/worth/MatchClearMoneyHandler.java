package com.netx.schedule.jobhandler.worth;

/**
 * Created by Yawn on 2018/8/29 0029.
 */

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
@JobHandler(value="MatchClearMoneyHandler")
@Component
public class MatchClearMoneyHandler extends IJobHandler {
    @Autowired
    private MatchFuseAction matchFuseAction;

    public ReturnT<String> execute(String matchId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"赛事结束时结算资金");
        matchFuseAction.repealFrozenMatchMoney(matchId);
        return SUCCESS;
    }
}
