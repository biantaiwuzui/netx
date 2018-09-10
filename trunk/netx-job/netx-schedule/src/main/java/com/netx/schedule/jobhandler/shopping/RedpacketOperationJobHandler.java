package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.client.shoppingmall.RedpacketPoolClientAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 红包0点更新
 * @author 黎子安 2018-4-10
 */
@JobHandler(value="RedpacketOperationJobHandler")
@Component
public class RedpacketOperationJobHandler extends IJobHandler {

    @Autowired
    private RedpacketPoolClientAction redpacketPoolClient;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(new Date() + "：" + "午夜12点红包相关操作定时任务启动");
        try {
            redpacketPoolClient.updateReapacketAmount();
        }catch (Exception e){
            XxlJobLogger.log(e);
        }
        return SUCCESS;
    }
}
