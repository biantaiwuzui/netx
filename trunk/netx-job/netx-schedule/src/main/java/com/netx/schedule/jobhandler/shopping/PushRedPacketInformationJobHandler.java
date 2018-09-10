package com.netx.schedule.jobhandler.shopping;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value="PushRedPacketInformationJobHandler")
@Component
public class PushRedPacketInformationJobHandler extends IJobHandler{

    @Autowired
    private RedpacketSendFuseAction redpacketSendFuseAction;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log(new Date() + "：" + "网值发红包啦，赶快来抢啊！");
        redpacketSendFuseAction.PushRedPacketInformation();
        return SUCCESS;
    }
}
