package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="CheckIsSendFinishJobHandler")
@Component
public class CheckIsSendFinishJobHandler extends IJobHandler {

    @Autowired
    private RedpacketSendFuseAction redpacketSendFuseAction;

    @Override
    public ReturnT<String> execute(String redpacketSendId) throws Exception{
        XxlJobLogger.log(new Date() + "：" + "下个红包发放时间到而此红包未发放完则把余额加入下个红包");
        redpacketSendFuseAction.checkIsSendFinish(redpacketSendId);
        return SUCCESS;
    }
}
