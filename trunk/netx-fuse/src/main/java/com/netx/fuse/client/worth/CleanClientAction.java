package com.netx.fuse.client.worth;

import com.netx.worth.biz.common.GiftAction;
import com.netx.worth.biz.common.InvitationAction;
import com.netx.worth.biz.demand.DemandAction;
import com.netx.worth.biz.meeting.MeetingAction;
import com.netx.worth.biz.skill.SkillAction;
import com.netx.worth.biz.wish.WishAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class CleanClientAction {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private DemandAction demandAction;
	@Autowired
	private SkillAction skillAction;
	@Autowired
	private MeetingAction meetingAction;
	@Autowired
	private WishAction wishAction;
	@Autowired
	private InvitationAction invitationAction;
	@Autowired
	private GiftAction giftAction;

	public boolean clean(String userId) {
		try {
			boolean cleanMeeting = meetingAction.clean(userId);
			boolean cleanGift = giftAction.clean(userId);
			boolean cleanDemand = demandAction.clean(userId);
			boolean cleanInvitation = invitationAction.clean(userId);
			boolean cleanWish = wishAction.clean(userId);
			boolean cleanSkill = skillAction.clean(userId);
			return cleanMeeting && cleanGift && cleanDemand && cleanInvitation && cleanWish && cleanSkill;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

}
