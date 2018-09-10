package com.netx.schedule.jobhandler.worth;
import com.alibaba.fastjson.JSONObject;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.wz.dto.skill.SkillDingDto;
import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.core.util.HttpClientUtil;
import com.netx.fuse.biz.worth.SkillFuseAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillRegister;
import com.netx.worth.service.WorthServiceprovider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.net.www.http.HttpClient;

import java.util.Date;

/**
 * 定时任务：未接受或预约者设定的时间开始后，发布者均未响应，则预约失败，托管费用解冻并退回给预约者
 *
 */
@JobHandler(value="SkillCheckSkillRegisterJobHandler")
@Component
public class SkillCheckSkillRegisterJobHandler extends IJobHandler {

    @Autowired
    SkillFuseAction skillFuseAction;
    @Autowired
    WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    WorthServiceprovider worthServiceprovider;

    @Override
    public ReturnT<String> execute(String skillRegisterId) throws Exception {
        XxlJobLogger.log(new Date ()+"："+"定时发布者未响应");
        skillFuseAction.checkSkillRegisterAccept(skillRegisterId);

        return SUCCESS;
    }

}
