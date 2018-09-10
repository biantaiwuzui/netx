package com.netx.worth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.SettlementAmountMapper;
import com.netx.worth.model.SettlementAmount;

@Service
public class SettlementAmountService extends ServiceImpl<SettlementAmountMapper, SettlementAmount>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    public List<SettlementAmount> selectBySettlementId(String settlementId) {
        EntityWrapper<SettlementAmount> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("settlement_id={0}", settlementId).orderBy("id");
        return selectList(entityWrapper);
    }

    public boolean cleanSettlementAmount(List<String> settlementIds) {
    	EntityWrapper<SettlementAmount> settlementAmountWrapper = new EntityWrapper<>();
    	settlementAmountWrapper.in("settlement_id", settlementIds);
    	return delete(settlementAmountWrapper);
    }
}
