package com.netx.fuse.client.worth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.fuse.biz.worth.InvitationFuseAction;
import com.netx.worth.biz.common.InvitationAction;
@Service
public class InvitationClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private InvitationAction invitationAction;
	@Autowired
	private InvitationFuseAction invitationFuseAction;
	
    public void checkSuccess(Map map) {
    	try {
    		invitationAction.checkSuccess((String)map.get("invitationId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkEvaluate(Map map) {
    	try {
    		invitationFuseAction.checkEvaluate((String)map.get("invitationId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkHasUnComplete(String userId) {
    	try {
    		invitationAction.checkHasUnComplete(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

	/**
	 * 获取咨讯的邀请数量
	 * @param userId
	 * @param articleId
	 * @return
	 */
    public Integer sendListCount(String userId,String articleId){
		try {
			return invitationAction.getSendCount(userId,articleId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}

}
