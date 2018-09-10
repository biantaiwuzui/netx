package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.shoppingmall.ordercenter.MerchantOrderFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="CheckIsSendJobHandler")
@Component
public class CheckIsSendJobHandler extends IJobHandler {

    @Autowired
    private MerchantOrderFuseAction merchantOrderFuseAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception{
        XxlJobLogger.log(new Date() + "：" + "三天不发货退款给用户定时任务开始");
        merchantOrderFuseAction.checkIsSend();
        return SUCCESS;
    }
}
