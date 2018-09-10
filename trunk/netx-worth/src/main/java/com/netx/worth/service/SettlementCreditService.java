package com.netx.worth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.SettlementCreditMapper;
import com.netx.worth.model.SettlementCredit;

@Service
public class SettlementCreditService extends ServiceImpl<SettlementCreditMapper, SettlementCredit> {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public List<SettlementCredit> selectBySettlementIdAndCan(String settlementId) {
		EntityWrapper<SettlementCredit> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("settlement_id={0}", settlementId).and("is_can={0}", true).orderBy("id");
		;
		return selectList(entityWrapper);
	}

	public boolean cleanSettlementCredit(List<String> settlementIds) {
		EntityWrapper<SettlementCredit> settlementCreditWrapper = new EntityWrapper<>();
		settlementCreditWrapper.in("settlement_id", settlementIds);
		return delete(settlementCreditWrapper);
	}
}
