package com.netx.worth.biz.settlement;

import java.util.List;

import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.worth.model.SettlementCredit;
import com.netx.worth.service.SettlementCreditService;

/**
 * <p>
 * 结算信用表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-10-05
 */
@Service
public class SettlementCreditAction{
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    
    public List<SettlementCredit> selectBySettlementIdAndCan(String settlementId) {
        return worthServiceprovider.getSettlementCreditService().selectBySettlementIdAndCan(settlementId);
    }

    private SettlementCredit selectCanBySettlementId(String settlementId, String userId) {
        EntityWrapper<SettlementCredit> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("settlement_id={0}", settlementId).and("user_id={0}", userId).and("is_can={0}", true).orderBy("id");
        ;
        return worthServiceprovider.getSettlementCreditService().selectOne(entityWrapper);
    }

    
    @Transactional
    public boolean create(String settlementId, Integer credit, String userId) {
        boolean greatThanLast = false;//是否比上次的大
        SettlementCredit lastMaxSettlementCredit = selectCanBySettlementId(settlementId, userId);
        if (lastMaxSettlementCredit != null) {
            if (credit > lastMaxSettlementCredit.getCredit()) {
                lastMaxSettlementCredit.setCan(false);
                greatThanLast = true;
                worthServiceprovider.getSettlementCreditService().updateById(lastMaxSettlementCredit);
            }
        } else {
            greatThanLast = true;
        }
        boolean can = greatThanLast ? true : false;
        SettlementCredit settlementCredit = new SettlementCredit();
        settlementCredit.setSettlementId(settlementId);
        settlementCredit.setUserId(userId);
        settlementCredit.setCan(can);
        settlementCredit.setCredit(credit);
        worthServiceprovider.getSettlementCreditService().insert(settlementCredit);
        return true;
    }

}
