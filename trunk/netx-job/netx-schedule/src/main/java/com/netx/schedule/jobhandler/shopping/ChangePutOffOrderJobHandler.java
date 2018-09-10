package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每5分钟执行一次
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="ChangePutOffOrderJobHandler")
@Component
public class ChangePutOffOrderJobHandler extends IJobHandler {

    @Autowired
    ProductOrderAction productOrderAction;

    @Override
    public ReturnT<String> execute(String param) throws Exception{
        XxlJobLogger.log(new Date() + "：" + "商家未同意延期自动同意延期定时任务启动");
        productOrderAction.changePutOffAndOrderStatus();
        return SUCCESS;
    }
}
