package com.netx.fuse.client.worth;

import java.util.Map;

import com.netx.fuse.biz.worth.DemandFuseAction;
import com.netx.ucenter.biz.user.UserAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netx.worth.biz.demand.DemandAction;

@Service
public class DemandClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private DemandAction demandAction;
	@Autowired
	private DemandFuseAction demandFuseAction;
	@Autowired
	private UserAction useraction;
    public void checkStart(Map map) {
    	try {
			demandAction.checkStart((String)map.get("demandOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void checkConfirm(Map map) {
    	try {
    		demandFuseAction.checkConfirm(((String)map.get("demandOrderId")));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void checkSuccess(Map map) {
    	try {
			demandAction.checkSuccess((String)map.get("demandOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public void checkEvaluate(Map map) {
    	try {
    		demandFuseAction.checkEvaluate((String)map.get("demandOrderId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }

    public boolean checkHasUnComplete(@RequestParam("userId") String userId) {
    	try {
			return checkHasUnComplete(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	return false;
    	
    }

    /**
     * 仲裁成功，不退款，支付给入选者
     * */
    public boolean registerRejectRefund(String demandOrderId,String userId) {
    	try {
			return demandFuseAction.registerRejectRefund(demandOrderId, userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
    }

    /**
     * 仲裁不成功，退款给发布者
     * */
    @RequestMapping(value = "/wz/demand/registerAcceptRefund", method = RequestMethod.POST)
    public boolean registerAcceptRefund(String demandOrderId,String userId) {
    	try {
			return demandFuseAction.registerAcceptRefund(demandOrderId, userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
    }
}