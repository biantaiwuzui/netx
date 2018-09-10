package com.netx.schedule.jobhandler.worth;

import com.alibaba.fastjson.JSONObject;
import com.netx.common.wz.dto.wish.WishPublishDto;
import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.WishFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@JobHandler(value="WishCheckEvaluateJobHandler")
@Component
public class WishCheckEvaluateJobHandler extends IJobHandler {
    @Autowired
    WishFuseAction wishFuseAction;

    @Override
    public ReturnT<String> execute(String wishId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时活动("+wishId+")检测是否双方已评价");
        wishFuseAction.checkEvaluate(wishId);
        return SUCCESS;
    }


}
