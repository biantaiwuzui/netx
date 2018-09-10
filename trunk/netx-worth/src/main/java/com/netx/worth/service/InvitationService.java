package com.netx.worth.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.invitation.InvitationSendDto;
import com.netx.utils.money.Money;
import com.netx.utils.money.Money;
import com.netx.worth.enums.InvitationStatus;
import com.netx.worth.mapper.InvitationMapper;
import com.netx.worth.model.Invitation;

@Service
public class InvitationService extends ServiceImpl<InvitationMapper, Invitation> {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**根基userId查询邀请或者被邀请的列表*/
    public List<Invitation> getUserInvitations(String userId, Page<Invitation> page, String type) {
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        boolean flagSend = type.equals("send");
        if (flagSend) {
            entityWrapper.where("from_user_id={0}", userId);
        } else {
        	entityWrapper.where("to_user_id={0}", userId);
        }
        entityWrapper.orderBy("create_time", false);//降序
        Page<Invitation> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }
    
    /**根据Id和userId修改邀请信息*/
    public boolean editInvitation(String invitationId,String userId ,Invitation invitation) {
    	EntityWrapper<Invitation> entityWrapper = new EntityWrapper<>();
    	entityWrapper.where("id={0}", invitationId).and("to_user_id={0}", userId);
    	return update(invitation, entityWrapper);
    }
    
    /**根据Id修改邀请信息*/
    public boolean editInvitationById(String invitationId,Invitation invitation) {
    	EntityWrapper<Invitation> entityWrapper = new EntityWrapper<>();
    	entityWrapper.where("id={0}", invitationId);
    	return update(invitation, entityWrapper);
    }
    
    /**发送邀请*/
    public boolean send(InvitationSendDto invitationSendDto,Invitation invitation){
        boolean success = false;
        invitation.setFromUserId(invitationSendDto.getFromUserId());
        invitation.setToUserId(invitationSendDto.getToUserId());
        invitation.setTitle(invitationSendDto.getTitle());
        invitation.setAddress(invitationSendDto.getAddress());
        invitation.setStartAt(new Date(invitationSendDto.getStartAt()));
        invitation.setEndAt(new Date(invitationSendDto.getEndAt()));
        invitation.setAmout(new Money(invitationSendDto.getAmout()).getCent());
        invitation.setDescription(invitationSendDto.getDescription());
        invitation.setAnonymity(invitationSendDto.getAnonymity());
        invitation.setLon(invitationSendDto.getLon());
        invitation.setLat(invitationSendDto.getLat());
        invitation.setOnline(invitationSendDto.getOnline());
        invitation.setStatus(InvitationStatus.SEND.status);
        invitation.setArticleId(invitationSendDto.getArticleId());
        return insert(invitation);
    }
/*
    public boolean sends(List<Invitation> invitation){
        return insertBatch(invitation);
    }*/
    
    /**修改验证码次数*/
    public boolean updateVerificationCode(String userId, String id, int times) {
        Invitation invitation = new Invitation();
        invitation.setTimes(times);
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        entityWrapper.where("from_user_id={0}", userId);
        entityWrapper.and("id={0}", id);
        return update(invitation, entityWrapper);
    }
    
    /**开始改邀请*/
    public boolean startInvitation(String userId, String id) {
        Invitation invitation = new Invitation();
        invitation.setValidation(true);
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        entityWrapper.where("from_user_id={0}", userId);
        entityWrapper.and("id={0}", id);
        return update(invitation, entityWrapper);
    }
    
    /**获取收到的邀请次数*/
    public int getReceiveCount(String userId) {
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        entityWrapper.where("to_user_id={0}", userId);
        return selectCount(entityWrapper);
    }
    
    /**获取发去的邀请数量*/
    public int getSendCount(String userId, String articleId) {
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        entityWrapper.where(StringUtils.hasText(userId), "from_user_id={0}", userId).and("article_id={0}", articleId);
        return selectCount(entityWrapper);
    }

    /**根据FromUserId获取发去的邀请数量*/
    public Integer getSendCountByFromUserId(String fromUserId) {
        EntityWrapper<Invitation> entityWrapper = new EntityWrapper<Invitation>();
        entityWrapper.where("from_user_id={0}", fromUserId);
        return selectCount(entityWrapper);
    }
    
    /**获取未完成的邀请列表*/
	public List<Invitation> getUnComplete(String userId) {
    	EntityWrapper<Invitation> invitationWrapper = new EntityWrapper<Invitation>();
    	invitationWrapper.gt("end_at", new Date().getTime());
		invitationWrapper.where("from_user_id={0}", userId);
		return selectList(invitationWrapper);
	}
	
	/**查询用户发送和送到的邀请列表
	 * @return */
	public List<Invitation> getSendAndReceive(String userId) {
		EntityWrapper<Invitation> invitationWrapper = new EntityWrapper<Invitation>();
		invitationWrapper.where("from_user_id={0}", userId);
		invitationWrapper.or("to_user_id={0}", userId);
		return selectList(invitationWrapper);
	}
	
	/**删除用户发送和送到的邀请列表
	 * @return */
	public boolean deleteSendAndReceive(String userId) {
		EntityWrapper<Invitation> invitationWrapper = new EntityWrapper<Invitation>();
		invitationWrapper.where("from_user_id={0}", userId);
		invitationWrapper.or("to_user_id={0}", userId);
		return delete(invitationWrapper);
	}
	
	/** 上个月接收邀请获得的报酬
	 * @return */
	public List<Invitation> getInvitationList(long firstDay,long lastDay,String userId) {
		EntityWrapper<Invitation> invitationWrapper = new EntityWrapper<>();
		invitationWrapper.where("from_user_id={0}", userId);
		invitationWrapper.and("status={0}",InvitationStatus.ACCEPT.status);
		invitationWrapper.and("from_validation_status={0}","1");
		invitationWrapper.and("from_validation_status={0}","1");
		invitationWrapper.between("create_time", firstDay, lastDay);
		return selectList(invitationWrapper);
	}
   
}
