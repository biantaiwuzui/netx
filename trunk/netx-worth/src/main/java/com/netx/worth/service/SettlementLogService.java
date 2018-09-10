package com.netx.worth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.SettlementLogMapper;
import com.netx.worth.model.SettlementLog;

@Service
public class SettlementLogService extends ServiceImpl<SettlementLogMapper, SettlementLog>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public boolean cleanSettlementLog(List<String> settlementIds) {
		EntityWrapper<SettlementLog> settlementLogWrapper = new EntityWrapper<>();
		settlementLogWrapper.in("settlement_id", settlementIds);
		return delete(settlementLogWrapper);
	}
}
