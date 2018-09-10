package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="ChangePutOffJobHandler")
@Component
public class ChangePutOffJobHandler extends IJobHandler {

    @Autowired
    private ProductOrderFuseAction productOrderFuseAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception{
        XxlJobLogger.log(new Date() + "：" + "延期收货到期确认用户收货并付款给商家定时任务启动");
        productOrderFuseAction.changePutOffStatusAndPay();
        return SUCCESS;
    }
}
