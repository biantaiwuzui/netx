package com.netx.worth.biz.common;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;

import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.invitation.InvitationConfirmDto;
import com.netx.common.wz.util.VerificationCodeUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.InvitationStatus;
import com.netx.worth.model.Invitation;
import com.netx.worth.model.Settlement;

/**
 * <p>
 * 邀请表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class InvitationAction{

	private Logger logger = LoggerFactory.getLogger(InvitationAction.class);

	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private WorthServiceprovider worthServiceprovider;

	@Transactional(rollbackFor = Exception.class)
	public boolean accept(String userId, String invitationId) throws Exception {
		boolean success = false;
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(invitationId);
		if (!invitation.getStatus().equals(InvitationStatus.SEND.status))
			throw new RuntimeException("状态异常，请检查");
		long now = new Date().getTime();
		long sendAt = invitation.getCreateTime().getTime();
		if (now > sendAt + 24l * 3600 * 1000)
			throw new RuntimeException("邀请已超时，请拒绝！");
		invitation.setStatus(InvitationStatus.ACCEPT.status);
		success = worthServiceprovider.getInvitationService().editInvitation(invitationId, userId, invitation);
		return success;
	}

	public boolean refuse(String userId, String invitationId) {
		boolean success = false;
		Invitation invitation = new Invitation();
		invitation.setStatus(InvitationStatus.REFUSE.status);
		success = worthServiceprovider.getInvitationService().editInvitation(invitationId, userId, invitation);
		return success;
	}

	@Transactional
	public boolean confirm(InvitationConfirmDto invitationConfirmDto) throws RuntimeException {
		boolean success = false;
		Invitation invitation = new Invitation();
		invitation.setConfirm(true);
		invitation.setAddress(invitationConfirmDto.getAddress());
		invitation.setStartAt(new Date(invitationConfirmDto.getStartAt()));
		invitation.setEndAt(new Date(invitationConfirmDto.getEndAt()));
		invitation.setCode(VerificationCodeUtil.generator());
		success = worthServiceprovider.getInvitationService().editInvitationById(invitationConfirmDto.getInvitationId(), invitation);
		if (!success)
			return false;
		Settlement settlement = settlementAction.create("初始化", "Invitation", invitation.getId(), false,
				invitation.getEndAt().getTime() + 25l * 3600 * 1000);
		success = settlement != null && StringUtils.hasText(settlement.getId());
		if (!success)
			throw new RuntimeException("settlement init error");

		return success;
	}

	@Transactional
	public boolean check(CommonCheckDto commonCheckDto) {
		boolean success = false;
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(commonCheckDto.getId());
		//TODO
//		Double distance = DistrictUtil.calcDistance(invitation.getLat(), invitation.getLon(), commonCheckDto.getLat(),
//				commonCheckDto.getLon());
//		boolean validationStatus = (distance * 1000) <= 100 ? true : false;
		boolean validationStatus = false;

		if (commonCheckDto.getFromOrTo().equals("0")) {// 主方
			success = updateFromUserValidationStatus(invitation.getId(), validationStatus);
		} else {// 客方
			success = updateToUserValidationStatus(invitation.getId(), validationStatus);
		}
		return validationStatus && success;
	}

	public boolean updateFromUserValidationStatus(String id, boolean validationStatus) {
		Invitation invitation = new Invitation();
		invitation.setFromValidationStatus(validationStatus);
		invitation.setId(id);
		return worthServiceprovider.getInvitationService().updateById(invitation);
	}

	public boolean updateToUserValidationStatus(String id, boolean validationStatus) {
		Invitation invitation = new Invitation();
		invitation.setToValidationStatus(validationStatus);
		invitation.setId(id);
		return worthServiceprovider.getInvitationService().updateById(invitation);
	}

	public boolean updateVerificationCode(String userId, String id, int times) {
		return worthServiceprovider.getInvitationService().updateVerificationCode(userId, id, times);
	}

	public boolean startInvitation(String userId, String id) {
		return worthServiceprovider.getInvitationService().startInvitation(userId, id);
	}

	public int getReceiveCount(String userId) {
		return worthServiceprovider.getInvitationService().getReceiveCount(userId);
	}

	public void checkSuccess(String invitationId) throws Exception {
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(invitationId);
		if (invitation.getOnline())
			return;
		if (!invitation.getFromValidationStatus()) {
			settlementAction.settlementCredit("Invitation", invitationId, invitation.getFromUserId(), -5);
		}
		if (!invitation.getToValidationStatus() || !invitation.getValidation()) {
			settlementAction.settlementCredit("Invitation", invitationId, invitation.getToUserId(), -5);
		} else {
			settlementAction.settlementAmountRightNow("邀请报酬结算", "Invitation", invitationId, invitation.getToUserId(),
					Money.CentToYuan(invitation.getAmout()).getAmount());
		}
	}

	public int getSendCount(String userId, String articleId) {
		return worthServiceprovider.getInvitationService().getSendCount(userId, articleId);
	}

	public Integer getSendCountByFromUserId(String fromUserId){
		return worthServiceprovider.getInvitationService().getSendCountByFromUserId(fromUserId);
	}
	public boolean checkHasUnComplete(String userId) {
		List<Invitation> invitationList = worthServiceprovider.getInvitationService().getUnComplete(userId);
		if (invitationList != null && invitationList.size() > 0) {
			return true;
		}
		return false;
	}

	@Transactional
	public boolean clean(String userId) throws Exception {
		// 先删除结算表
		List<Invitation> list = worthServiceprovider.getInvitationService().getSendAndReceive(userId);
		List<String> invitationIds = list.stream().map(Invitation::getId).collect(toList());
		if (invitationIds != null && invitationIds.size() > 0) {
			EntityWrapper<Settlement> settlementWrapper = new EntityWrapper<Settlement>();
			settlementWrapper.where("relatableType={0}", "Invitation");
			settlementWrapper.in("relatableId", invitationIds);
			// 删除结算表和子表
			List<Settlement> settlementList = worthServiceprovider.getSettlementService().getSettlementListByTypeAndId("Invitation", invitationIds);
			List<String> settlementIds = settlementList.stream().map(Settlement::getId).collect(toList());
			boolean cleanSettlment = worthServiceprovider.getSettlementService().deleteSettlementListByTypeAndId("Invitation", invitationIds)
					&& settlementAction.clean(settlementIds);
			;

			boolean success = worthServiceprovider.getInvitationService().deleteSendAndReceive(userId) && cleanSettlment;
			if (success) {
				return success;
			} else {
				logger.error("清除该用户邀请的数据失败");
				throw new RuntimeException();
			}
		} else {
			// 没有数据
			return true;
		}
	}

	public Invitation selectById(String invitationId) {
		return worthServiceprovider.getInvitationService().selectById(invitationId);
	}

	public boolean updateCode(String invitationId){
		Invitation invitation = worthServiceprovider.getInvitationService().selectById(invitationId);
		boolean success = false;
		if(invitation.getStartAt() != null && invitation.getEndAt() != null) {
			if (!invitation.getOnline()) {
				if(invitation.getAddress()!=null){
					success = true;
				}
			}else{
				success = true;
			}

		}
		if(success){
			invitation.setCode(VerificationCodeUtil.generator());
			worthServiceprovider.getInvitationService().editInvitationById(invitationId, invitation);
		}
		return success;
	}

}
