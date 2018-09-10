package com.netx.fuse.client.worth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netx.fuse.biz.worth.SettlementFuseAction;
import org.springframework.stereotype.Service;

@Service
public class SettlementClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private SettlementFuseAction settlementFuseAction;
    public void start() {
    	try {
			settlementFuseAction.start();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }
}
