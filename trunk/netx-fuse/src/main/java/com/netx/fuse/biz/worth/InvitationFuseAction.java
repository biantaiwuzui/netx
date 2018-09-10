package com.netx.fuse.biz.worth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.wz.dto.invitation.InvitationSendDto;
import com.netx.common.wz.dto.invitation.InvitationSuggestDto;
import com.netx.fuse.proxy.ClientProxy;
import com.netx.fuse.proxy.EvaluateProxy;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.model.Invitation;
import com.netx.worth.model.Settlement;

@Service
public class InvitationFuseAction {

//	@Autowired
//	private QuartzService quartzService;
	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private ClientProxy clientProxy;
	@Autowired
	private UserClientProxy userClientProxy;
	@Autowired
	private WorthServiceprovider worthServiceprovider;
	@Autowired
	private MessagePushProxy messagePushProxy;// 推送工具类

	@Autowired
	private UserAction userAction;
	
	public Map getUserReceiveInvitation(String userId, Page<Invitation> page) {
		List<Invitation> list = worthServiceprovider.getInvitationService().getUserInvitations(userId, page, "receive");
		List<String> userIdList  = list.stream().map(Invitation::getFromUserId).collect(Collectors.toList());
		List<CommonUserBaseInfoDto> userBaseInfoList = userClientProxy.getUsers(userIdList);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("userBaseInfoList", userBaseInfoList);
		return map;
	}

	public Map getUserSendInvitation(String userId, Page<Invitation> page) {
		List<Invitation> list = worthServiceprovider.getInvitationService().getUserInvitations(userId, page, "send");
		List<String> userIdList  = list.stream().map(Invitation::getToUserId).collect(Collectors.toList());
		List<CommonUserBaseInfoDto> userBaseInfoList = userClientProxy.getUsers(userIdList);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("userBaseInfoList", userBaseInfoList);
		return map;
	}

	/*
	// 活动邀请好友
	public boolean meetingInvitation(String friends, String meetingId, String fromUserId, boolean isAnonymity){
		Meeting meeting = meetingService.selectById(fromUserId);
		String toUserIds [] = friends.split(",");
		List<Invitation> list = new ArrayList<>();
		for(String toUserId : toUserIds){
			Invitation invitation = new Invitation();
			invitation.setFromUserId(fromUserId);
			invitation.setToUserId(toUserId);
			invitation.setTitle(meeting.getTitle());
			invitation.setAddress(meeting.getAddress());
			invitation.setStartAt(meeting.getStartedAt());
			invitation.setEndAt(meeting.getEndAt());
			invitation.setAmout(new Long(0));
			invitation.setDescription(meeting.getDescription());
			invitation.setAnonymity(isAnonymity);
			invitation.setLon(meeting.getLon());
			invitation.setLat(meeting.getLat());
			if(meeting.getMeetingType() == 1){
				invitation.setOnline(true);
			}else{
				invitation.setOnline(false);
			}
			invitation.setStatus(InvitationStatus.SEND.status);
			invitation.setArticleId(meetingId);
			list.add(invitation);
		}
		return worthServiceprovider.getInvitationService().sends(list);
	}*/

	@Transactional
	public boolean send(InvitationSendDto invitationSendDto) throws Exception {
		boolean success = false;
		Invitation invitation = new Invitation();
		success = worthServiceprovider.getInvitationService().send(invitationSendDto, invitation);
		if (!success)
			return false;
		if (invitationSendDto.getStartAt() != null && invitationSendDto.getEndAt() != null) {
			Settlement settlement = settlementAction.create("初始化", "Invitation", invitation.getId(), false,
					invitationSendDto.getEndAt() + 25l * 3600 * 1000);
			success = settlement != null && StringUtils.hasText(settlement.getId());
			if (!success)
				throw new RuntimeException("settlement init error");
		}
		//TODO
//		success = quartzService.checkInvitationSuccess(invitation.getId(), invitation.getStartAt().getTime() + 1800l * 1000);
		if (!success)
			throw new RuntimeException("checkInvitationSuccess error");

		//TODO
//		success = quartzService.checkInvitationEvaluate(invitation.getId(), invitation.getEndAt().getTime() + 24l * 3600 * 1000);
		if (!success)
			throw new RuntimeException("checkInvitationEvaluate error");

		return success;
	}
	
	public Map<String, Object> detail(String userId, String invitationId) {
		Map<String, Object> map = new HashMap<>();
		ArrayList<String> ids = new ArrayList<>();
		ids.add(userId);
		Map<String, UserSynopsisData> userMap = userClientProxy.selectUserMapByIds(ids);
		Invitation invitation = new Invitation();
		invitation.setId(invitationId);
		invitation = worthServiceprovider.getInvitationService().selectById(invitation);
		map.put("invitation", invitation);
		map.put("userMap", userMap);
		return map;
	}
	
	public boolean suggest(InvitationSuggestDto invitationSuggestDto) {
		StringBuffer buffer = new StringBuffer("");
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(invitationSuggestDto.getInvitationId());
		invitation.setAddress(invitationSuggestDto.getAddress());
		invitation.setStartAt(new Date(invitationSuggestDto.getStartAt()));
		invitation.setEndAt(new Date(invitationSuggestDto.getEndAt()));
		invitation.setSuggestion(invitationSuggestDto.getSuggestion());
		boolean ret = worthServiceprovider.getInvitationService().editInvitationById(invitationSuggestDto.getInvitationId(), invitation);
		if (ret) {
			List<SelectFieldEnum> selectFieldEnums = new LinkedList<>();
			selectFieldEnums.add(SelectFieldEnum.NICKNAME);
			UserInfoResponseDto dto = selectUserInfoByUserId(invitation.getToUserId(), selectFieldEnums);
			buffer = buffer.append(dto != null ? dto.getNickname() : invitation.getToUserId());
			Map map = new HashMap();
			map.put("id", invitation.getId());// 根据邀请的id获取邀请信息
			messagePushProxy.multipleParamMessagePushJump(MessageTypeEnum.ACTIVITY_TYPE,
					new StringBuilder("你向" + buffer.toString()).append("发出的").append(invitation.getTitle())
							.append("邀请，对方已接受并对时间、地址等有新的建议，请你确认").toString(),
					"向邀请者提出建议", invitation.getFromUserId(), PushMessageDocTypeEnum.WZ_INVOCATION, map);
		}
		return ret;
	}
	
	public UserInfoResponseDto selectUserInfoByUserId(String userId, List<SelectFieldEnum> selectList) {
		if (selectList == null || selectList.size() == 0) {
			return null;
		}
		UserInfoRequestDto dto = new UserInfoRequestDto();
		dto.setSelectData(userId);
		dto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
		dto.setSelectFieldEnumList(selectList);
		return userClientProxy.selectUserInfo(dto);
	}
	
	/** 检查评价 */
	public void checkEvaluate(String invitationId) {
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(invitationId);
		String fromUserId = invitation.getFromUserId();
		String toUserId = invitation.getToUserId();
		List<String> list = new EvaluateProxy().notEvaluateUsers(Arrays.asList(fromUserId, toUserId), invitationId);
		list.forEach(userId -> {
			settlementAction.settlementCredit("Invitation", invitationId, userId, -2);
		});
	}

}
