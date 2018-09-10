package com.netx.fuse.client.worth;

import java.util.Map;

import com.netx.fuse.biz.worth.SkillFuseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.netx.worth.biz.skill.SkillAction;
@Service
public class SkillClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private SkillAction skillAction;
	@Autowired
	private SkillFuseAction skillFuseAction;
	
    public boolean publishCancel(Map map) {
    	try {
    		return skillAction.publishCancel((String)map.get("id"), (String)map.get("userId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
    }

    public void checkOrderStart(Map map) {
    	try {
    		skillFuseAction.checkOrderStart((String)map.get("skillOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkSuccess(Map map) {
    	try {
    		skillFuseAction.checkSuccess((String)map.get("skillOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkEvaluate(Map map) {
    	try {
    		skillFuseAction.checkEvaluate((String)map.get("skillOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public boolean checkHasUnComplete(@RequestParam("userId") String userId) {
    	try {
    		return skillAction.checkHasUnComplete(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
    	
    }

}


