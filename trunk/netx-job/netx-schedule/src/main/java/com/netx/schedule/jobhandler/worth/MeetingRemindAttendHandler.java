package com.netx.schedule.jobhandler.worth;


import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.fuse.biz.worth.MeetingFuseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler("MeetingRemindAttendHandler")
@Component
public class MeetingRemindAttendHandler extends IJobHandler{
    
    @Autowired
    private MeetingFuseAction meetingFuseAction;

    @Override
    public ReturnT<String> execute(String meetingId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时活动("+meetingId+")活动开始前30分钟提醒参与者出席");
        meetingFuseAction.remindAttendMeeting(meetingId);
        return  SUCCESS;
    }
}
