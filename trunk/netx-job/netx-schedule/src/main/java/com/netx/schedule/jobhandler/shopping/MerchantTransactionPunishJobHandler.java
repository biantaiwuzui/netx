package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value="MerchantTransactionPunishJobHandler")
@Component
public class MerchantTransactionPunishJobHandler extends IJobHandler {

    @Autowired
    private MerchantFuseAction merchantFuseAction;

    @Override
    public ReturnT<String> execute(String sellerId) throws Exception{
        XxlJobLogger.log(new Date()+"："+"定时检查商家("+sellerId+")注册后三个月，每个季度是否有交易记录");
        return merchantFuseAction.transactionPunish(sellerId)?SUCCESS:FAIL;
    }
}
