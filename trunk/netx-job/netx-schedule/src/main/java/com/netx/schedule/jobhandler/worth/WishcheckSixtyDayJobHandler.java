package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.WishFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value="WishcheckSixtyDayJobHandler")
@Component
public class WishcheckSixtyDayJobHandler extends IJobHandler {
    @Autowired
    WishFuseAction wishFuseAction;


    @Override
    public ReturnT<String> execute(String wishId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时活动("+wishId+")创建检查60天内有无新的用款申请");
        wishFuseAction.check60DayHasWishApply(wishId);
        return SUCCESS;
    }
}
