package com.netx.worth.biz.settlement;

import java.math.BigDecimal;
import java.util.List;

import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.utils.money.Money;
import com.netx.worth.model.SettlementAmount;
import com.netx.worth.util.ArithUtil;

/**
 * <p>
 * 结算金额表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-10-05
 */
@Service
public class SettlementAmountAction{
    @Autowired
    private WorthServiceprovider worthServiceprovider;
//    
//    public boolean addAmount(String settlementId, BigDecimal amount, String userId) {
//        SettlementAmount settlementAmount = new SettlementAmount();
//        settlementAmount.setSettlementId(settlementId);
//        settlementAmount.setAmount(ArithUtil.abs(amount));
//        settlementAmount.setUserId(userId);
//        return insert(settlementAmount);
//    }
//
//    
//    public boolean subAmount(String settlementId, BigDecimal amount, String userId) {
//        SettlementAmount settlementAmount = new SettlementAmount();
//        settlementAmount.setSettlementId(settlementId);
//        settlementAmount.setAmount(ArithUtil.unAbs(amount));
//        settlementAmount.setUserId(userId);
//        return insert(settlementAmount);
//    }


    
    public boolean create(String settlementId, BigDecimal amount, String userId) {
        SettlementAmount settlementAmount = new SettlementAmount();
        settlementAmount.setSettlementId(settlementId);
        settlementAmount.setAmount(new Money(ArithUtil.unAbs(amount)).getCent());
        settlementAmount.setUserId(userId);
        return worthServiceprovider.getSettlementAmountService().insert(settlementAmount);
    }

    
    public List<SettlementAmount> selectBySettlementId(String settlementId) {
        return worthServiceprovider.getSettlementAmountService().selectBySettlementId(settlementId);
    }

}
