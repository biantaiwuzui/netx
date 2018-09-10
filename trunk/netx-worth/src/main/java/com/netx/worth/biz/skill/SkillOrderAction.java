package com.netx.worth.biz.skill;


import com.netx.common.wz.util.VerificationCodeUtil;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.worth.enums.SkillOrderStatus;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;



/**
 * <p>
 * 技能单表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-19
 */
@Service
public class SkillOrderAction{
	
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private SkillOrderAction skillOrderAction;
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;

    
    public SkillOrder create(String id, String userId, String skillRegisterId) {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        if (skillRegister == null) {
            return null;
        }
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().create(skillOrderCreat(id,userId,skillRegisterId,skillRegister)); //查Order单数据
        if(skillOrder == null) {
        	logger.error("插入Order单数据失败");
        	throw new RuntimeException();
        }
        return skillOrder;
    }

    public SkillOrder publishGeneratCode(String reId,String orderId) {
    	SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().publishGeneratCode(skillOrderActionGeneratCode(orderId,reId),orderId, reId);
    	if(skillOrder == null) {
        	logger.error("生成验证码并修改状态失败");
        	throw new RuntimeException();
        }
        return skillOrder;
    }

    public boolean publishCancelOrder(String id) {
        SkillOrder skillOrder = new SkillOrder();
            skillOrder.setId(id);
            skillOrder.setStatus(SkillOrderStatus.CANCEL.status);
            return worthServiceprovider.getSkillOrderService().updateById(skillOrder);
    }

    public SkillOrder skillOrderCreat(String id, String userId, String skillRegisterId,SkillRegister skillRegister){
        SkillOrder skillOrder = new SkillOrder();
        skillOrder.setCreateUserId(userId);
        skillOrder.setSkillRegisterId(skillRegisterId);
        skillOrder.setStartAt(skillRegister.getStartAt());
        skillOrder.setEndAt(skillRegister.getEndAt());
        skillOrder.setUnit(skillRegister.getUnit());
        skillOrder.setAmount(skillRegister.getAmount());
        skillOrder.setNumber(skillRegister.getNumber());
        skillOrder.setFee(skillRegister.getFee());
        skillOrder.setDescription(skillRegister.getDescription());
        skillOrder.setLon(skillRegister.getLon());
        skillOrder.setLat(skillRegister.getLat());
        return skillOrder;
    }
    
    public SkillOrder skillOrderActionGeneratCode(String orderId,String reId) {
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(orderId);
        skillOrder.setCode(VerificationCodeUtil.generator());
        skillOrder.setStatus(SkillOrderStatus.START.status);
        skillOrder.setValidationStatus(true);
        skillOrder.setUpdateUserId(reId);
        return skillOrder;
    }
    
    public SkillOrder skillOrderActionStatus(boolean validationStatus){
        SkillOrder skillOrder = new SkillOrder();
        skillOrder.setValidationStatus(validationStatus);
        return skillOrder;
    }
}
