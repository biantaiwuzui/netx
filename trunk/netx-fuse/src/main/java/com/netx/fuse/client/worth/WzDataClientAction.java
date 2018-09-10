package com.netx.fuse.client.worth;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.fuse.biz.worth.MeetingFuseAction;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.worth.biz.demand.DemandAction;
import com.netx.worth.biz.common.GiftAction;
import com.netx.worth.biz.common.InvitationAction;
import com.netx.worth.biz.meeting.MeetingAction;
import com.netx.worth.biz.skill.SkillAction;
import com.netx.worth.biz.wish.WishAction;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandRegister;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillRegister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class WzDataClientAction {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private GiftAction giftAction;
	@Autowired
	private InvitationAction invitationAction;
	@Autowired
	private WishAction wishAction;
	@Autowired
	private SkillAction skillAction;
	@Autowired
	private DemandAction demandAction;
	@Autowired
	private MeetingAction meetingAction;
	@Autowired
	private MeetingFuseAction meetingFuseAction;
	@Autowired
	private WishFuseAction wishFuseAction;


	public Integer giftCount(String userId) {
		try {
			return giftAction.getReceiveCount(userId);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	public Integer invitationCount(String userId) {
		try {
			return invitationAction.getReceiveCount(userId);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

//	public Map<String, Object> supportWishList(CommonPageDto commonPageDto) {
//		try {
//			Page<WishSupport> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
//			return wishFuseAction.getSupportListByWish(commonPageDto.getId(), page);
//		}catch (Exception e) {
//			logger.error(e.getMessage(),e);
//		}
//		return null;
//	}

	public Map<String, Object> sendSkillList(CommonPageDto commonPageDto) {
		try {
			Page<Skill> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			return skillAction.publishList(commonPageDto.getUserId(), page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	public Map<String, Object> registerSkillList(CommonPageDto commonPageDto) {
		try {
			Page<SkillRegister> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			Page<Skill> repage = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			//	return skillAction.getRegisterList(commonPageDto.getUserId(),repage,page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	public Map<String, Object> sendDemandList(CommonPageDto commonPageDto) {
		try {
			Page<Demand> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			return demandAction.getUserPublishDemand(commonPageDto.getUserId(), page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}



	public Map<String, Object> registerDemandList(CommonPageDto commonPageDto) {
		try {
			Page<DemandRegister> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			return demandAction.getUserRegDemand(commonPageDto.getId(), commonPageDto.getUserId(), page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	public Map<String, Object> sendMeetingList(CommonPageDto commonPageDto) {
		try {
			Page<MeetingSend> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
			return meetingFuseAction.getUserSendMeeting(commonPageDto.getUserId(), null,null,page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;

	}

	public Map<String, Object> receiveMeetinList(CommonPageDto commonPageDto) {
		try {
			Page<MeetingRegister> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
//    		return meetingFuseAction.getUserRegMeeting(commonPageDto.getUserId(), null,null,page);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
