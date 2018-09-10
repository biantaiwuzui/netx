package com.netx.schedule.jobhandler.ucenter;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.ucenter.biz.user.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天0点操作
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="UserDayUpdateJobHandler")
@Component
public class UserDayUpdateJobHandler extends IJobHandler {

    @Autowired
    private UserAction userAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时清理用户日常字段启动");
        userAction.getUserService().updateDayDay();
        return SUCCESS;
    }
}
