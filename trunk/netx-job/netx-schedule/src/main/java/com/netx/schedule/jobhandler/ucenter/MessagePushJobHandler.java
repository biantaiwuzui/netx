package com.netx.schedule.jobhandler.ucenter;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.ucenter.biz.common.MessagePushAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-2
 */
@JobHandler(value="MessagePushJobHandler")
@Component
public class MessagePushJobHandler extends IJobHandler {

    @Autowired
    private MessagePushAction messagePushAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时极光推送启动");
        messagePushAction.sendMessage();
        return SUCCESS;
    }
}
