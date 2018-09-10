package com.netx.schedule.jobhandler.ucenter;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.ucenter.biz.user.UserScoreAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天0点操作
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="UserLoginJobHandler")
@Component
public class UserLoginJobHandler extends IJobHandler {

    @Autowired
    private UserScoreAction userScoreAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时扣除连续10内没有登录的用户积分启动");
        userScoreAction.updateScoreByLoginStatus();
        return SUCCESS;
    }

}
