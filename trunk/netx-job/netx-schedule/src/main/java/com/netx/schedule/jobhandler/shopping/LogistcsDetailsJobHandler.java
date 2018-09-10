package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="LogistcsDetailsJobHandler")
@Component
public class LogistcsDetailsJobHandler extends IJobHandler {

    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception{
        XxlJobLogger.log(new Date() + "：" + "收集订单物流信息定时任务启动");
        merchantOrderInfoAction.updateOrderInfoShippingDetails();
        return SUCCESS;
    }
}
