package com.netx.fuse.client.worth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netx.worth.biz.settlement.SettlementAction;
import org.springframework.stereotype.Service;

@Service
public class BillClientAction {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private SettlementAction settlementAction;
	
	public Map<String, Object> getTradingVolume(String userId) {
    	try {
    		return settlementAction.getTradingVolume(userId);
    	}catch (Exception e) {
    		logger.error(e.getMessage(),e);
    	}
    	return null;
	}


}
