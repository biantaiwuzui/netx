package com.netx.schedule.jobhandler.worth;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.SkillFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @Auther: JayJay
 * @date: 2018/9/6 15:50
 * @Description: 检测预约者申请退款36小时后发布者是否处理申请
 */
@JobHandler(value = "SkillCheckTimeoutRefundJobHandle")
@Component
public class SkillCheckTimeoutRefundJobHandle extends IJobHandler {

    @Autowired
    SkillFuseAction skillFuseAction;

    @Override
    public ReturnT<String> execute(String json) throws Exception {
        XxlJobLogger.log(new Date() + "：" + "检测预约者申请退款36小时后发布者是否处理" );
        boolean success = skillFuseAction.publishTimeoutNotHandleRefund(json);
        if(!success){
            throw new RuntimeException("检测预约者申请退款36小时后发布者是否处理出现异常");
        }
        return SUCCESS;
    }
}
