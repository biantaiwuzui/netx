package com.netx.worth.biz.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.utils.money.Money;
import com.netx.worth.enums.PayWay;
import com.netx.worth.enums.RefundStatus;
import com.netx.worth.mapper.RefundMapper;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.model.Refund;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;

/**
 * <p>
 * 退款表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-14
 */
@Service
public class RefundAction extends ServiceImpl<RefundMapper, Refund>{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;

    public Refund create(String description, BigDecimal bail, Integer payWay, String relatableId, String relatableType, BigDecimal amount, String userId) {
        Refund refund = new Refund();
        refund.setAmount(new Money(amount).getCent());
        refund.setRelatableId(relatableId);
        refund.setRelatableType(relatableType);
        refund.setUserId(userId);
        refund.setDescription(description);
        refund.setBail(new Money(bail).getCent());
        refund.setPayWay(payWay);
        long now = new Date().getTime();
        refund.setExpiredAt(new Date(now + (3600l * 36)));
        return insert(refund)?refund:null;
    }

    public boolean reject(String refundId, String userId) {
        return worthServiceprovider.getRefundService().reject(refundId, userId);
    }

    public boolean accept(String refundId, String userId) {
        return worthServiceprovider.getRefundService().accept(refundId, userId);
    }
    public boolean timeoutAutoAccept(String refundId, String userId) {
        return worthServiceprovider.getRefundService().timeoutAutoAccept(refundId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void start() throws Exception {
        List<Refund> list = getAvailableRefund();
        for (Refund refund : list) {
            Integer status = refund.getStatus();
            Long bail = refund.getBail();
            if (bail > 0) {//用户重复支付了一笔钱，需要处理一下。
                processBail(refund);
            }

            if (status.equals(RefundStatus.REQUEST.status)) {//未处理且超时的，标记为超期自动同意退款
                refund.setStatus(RefundStatus.EXPIRED.status);
            }
            refund.setProcess(true);
            refund.setProcessAt(new Date());
            updateById(refund);
        }
    }

    public void processBail(Refund refund) throws Exception {
        Integer payWay = refund.getPayWay();
        if (payWay.equals(PayWay.MONEY.code)) {
            settlementAction.settlementAmountRightNow("用户退款时重复支付", refund.getRelatableType(), refund.getRelatableId(), refund.getUserId(),new BigDecimal(refund.getBail()));
        } else {
            //TODO 调用网币的退回方法
        }

        if (refund.getRelatableType().equals("SkillOrder")) {
            SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(refund.getRelatableId());
            SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
            skillRegister.setBail(refund.getBail() - (skillRegister.getBail()));//减掉相应的冻结费
            worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
        } else if (refund.getRelatableType().equals("DemandOrder")) {
            DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(refund.getRelatableId());
            demandOrder.setBail(refund.getBail() - (demandOrder.getBail()));
            worthServiceprovider.getDemandOrderService().updateById(demandOrder);
        }
    }

    public List<Refund> getAvailableRefund() {
        Page<Refund> page = new Page<>(1, 10);
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("isProcess={0}", false)
                .andNew("status={0}", RefundStatus.ACCEPT.status)
                .or("(status={0} and expiredAt<={1})", RefundStatus.REQUEST.status, new Date().getTime())
                .orderBy("id");
        selectPage(page, entityWrapper);
        return page.getRecords();
    }

    public boolean isUnProcess(String relatableType, String relatableId) {
        return worthServiceprovider.getRefundService().getRefundByRelIdAndRelType(relatableType, relatableId).getStatus().equals(RefundStatus.REQUEST.status);
    }
    
    /* 根据userId和技能单id判断是否存在此条数据**/
    public int getCountByUserIdAndSkillId(String userId,String id) {
        EntityWrapper<Refund> refundWrapper = new EntityWrapper<>();
        refundWrapper.where("user_id={0}", userId);
        refundWrapper.and("id={0}", id);
        return selectCount(refundWrapper);
    }

    public Refund cre(String description, BigDecimal bail, Integer payWay, String relatableId, String relatableType, BigDecimal amount, String userId) {
        Refund refund = new Refund();
        refund.setAmount(new Money(amount).getCent());
        refund.setRelatableId(relatableId);
        refund.setRelatableType(relatableType);
        refund.setUserId(userId);
        refund.setDescription(description);
        refund.setBail(new Money(bail).getCent());
        refund.setPayWay(payWay);
        long now = new Date().getTime();
        refund.setExpiredAt(new Date(now + (3600l * 36)));
        worthServiceprovider.getRefundService().insert(refund);
        return refund;
    }
}
