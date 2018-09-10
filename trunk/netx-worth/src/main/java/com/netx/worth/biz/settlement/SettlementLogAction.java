package com.netx.worth.biz.settlement;

import java.math.BigDecimal;

import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.worth.model.SettlementLog;
import com.netx.worth.service.SettlementLogService;

/**
 * <p>
 * 结算流水表 每次结算时插入 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-10-05
 */
@Service
public class SettlementLogAction{
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    
    public void createAmountLog(String settlementId, String settlementAmountId, String userId, Long amount) {
        SettlementLog settlementLog = new SettlementLog();
        settlementLog.setSettlementId(settlementId);
        settlementLog.setSettlementAmountId(settlementAmountId);
        settlementLog.setUserId(userId);
        settlementLog.setAmount(amount);
        worthServiceprovider.getSettlementLogService().insert(settlementLog);
    }

    
    public void creatCreditLog(String settlementId, String settlementCreditId, String userId, Integer credit) {
        SettlementLog settlementLog = new SettlementLog();
        settlementLog.setSettlementId(settlementId);
        settlementLog.setSettlementCreditId(settlementCreditId);
        settlementLog.setUserId(userId);
        settlementLog.setCredit(credit);
        worthServiceprovider.getSettlementLogService().insert(settlementLog);
    }
}
