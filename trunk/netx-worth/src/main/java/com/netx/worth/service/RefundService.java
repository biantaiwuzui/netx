package com.netx.worth.service;

import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.enums.RefundStatus;
import com.netx.worth.mapper.RefundMapper;
import com.netx.worth.model.Refund;

import java.util.Date;

@Service
public class RefundService extends ServiceImpl<RefundMapper, Refund>{

    @Autowired
    private WorthServiceprovider worthServiceprovider;

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public Refund getRefundByRelIdAndRelType(String relatableId,String relatableType) {
		EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("relatable_id={0}", relatableId);
		entityWrapper.and("relatable_type={0}", relatableType);
		return selectOne(entityWrapper);
	}
	
	/* 设置状态为已拒绝退款*/
    public boolean reject(String refundId, String userId) {
        Refund refund = worthServiceprovider.getRefundService().selectById(refundId);
        refund.setStatus(RefundStatus.REFUSE.status);
        refund.setOperateUserId(userId);
        refund.setUpdateTime(new Date());
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", refundId);
        return updateById(refund);
    }
    
    /* 同意退款*/
    public boolean accept(String refundId, String userId) {
        Refund refund = worthServiceprovider.getRefundService().selectById(refundId);
        refund.setStatus(RefundStatus.ACCEPT.status);
        refund.setOperateUserId(userId);
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_id={0}", refundId);
        return updateById(refund);
    }

    public boolean timeoutAutoAccept(String refundId, String userId){
        Refund refund = worthServiceprovider.getRefundService().selectById(refundId);
        refund.setStatus(RefundStatus.EXPIRED.status);
        refund.setOperateUserId(userId);
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_id={0}", refundId);
        return updateById(refund);
    }
    
    /* 通过refundId拿到预约者Id*/
    public Refund getByUserId(String id) {
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", id);
        return selectOne(entityWrapper);
    }
    
    
    public Refund getRefundByRelId(String userId) {
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        return selectOne(entityWrapper);
    }

    
    public Refund getRefund(String relatableId) {
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_id={0}", relatableId);
        return selectOne(entityWrapper);
    }
    
    //判断是否已经申请过退款
    public Integer getRefund(String relatableId,String userId) {
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_id={0}", relatableId);
        entityWrapper.where("user_id={0}", userId);
        return selectCount(entityWrapper);
    }
    
    //拿ID
    public Refund getRefundId(String relatableId,String userId) {
        EntityWrapper<Refund> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_id={0}", relatableId);
        entityWrapper.where("user_id={0}", userId);
        return selectOne(entityWrapper);
    }
}
