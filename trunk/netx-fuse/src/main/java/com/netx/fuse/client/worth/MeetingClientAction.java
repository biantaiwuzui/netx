package com.netx.fuse.client.worth;

import java.util.Map;

import com.netx.fuse.biz.worth.MeetingFuseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netx.worth.biz.meeting.MeetingAction;
import org.springframework.stereotype.Service;

@Service
public class MeetingClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private MeetingAction meetingAction;
	@Autowired
	private MeetingFuseAction meetingFuseAction;

    public void checkRegisterSuccess(Map map) {
    	try {
    		meetingFuseAction.SureSelected((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkPublishStart(Map map) {
    	try {
    		meetingFuseAction.checkPublishStart((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkSuccess(Map map) {
    	try {
    		meetingAction.checkSuccess((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkEvaluate(Map map) {
    	try {
    		meetingFuseAction.checkEvaluate((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public boolean checkHasUnComplete(String userId) {
    	try {
    		return meetingAction.checkHasUnComplete(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	return false;
    	
    }

//    public void checkMeetingRegisterCount(Map map) {
//    	try {
//    		meetingFuseAction.checkMeetingRegisterCount((String)map.get("meetingId"));
//		} catch (Exception e) {
//			logger.error(e.getMessage(),e);
//		}
//    	
//    }

    public void checkAmountEnough(Map map) {
    	try {
    		meetingFuseAction.checkAmountEnough((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkConfirmDetail(Map map) {
    	try {
    		meetingFuseAction.checkConfirmDetail((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkNoConfirmDetail(Map map) {
    	try {
    		meetingFuseAction.checkNoConfirmDetail((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public boolean autoMeetingSettle(Map map) {
    	try {
    		return meetingFuseAction.autoMeetingSettle((String)map.get("meetingId"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	return false;
    	
    }

}
