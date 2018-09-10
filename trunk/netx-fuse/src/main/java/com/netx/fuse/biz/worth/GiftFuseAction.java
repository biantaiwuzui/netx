package com.netx.fuse.biz.worth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.wz.dto.gift.GiftSendDto;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.worth.enums.GiftType;
import com.netx.worth.model.Gift;

@Service
public class GiftFuseAction {
	@Autowired
	private WorthServiceprovider worthServiceprovider;
	@Autowired
	private UserClientProxy userClientProxy;
	@Autowired
	private InvitationFuseAction invitationFuseAction;
	@Autowired
	private MessagePushProxy messagePushProxy;

	public Map getUserReceiveGifts(String userId, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Gift> list = worthServiceprovider.getGiftService().getUserGifts(userId, page, "receive");
		List<String> userIdList = list.stream().map(Gift::getFromUserId).collect(Collectors.toList());
		List<CommonUserBaseInfoDto> userBaseInfoList = userClientProxy.getUsers(userIdList);
		map.put("list", list);
		map.put("userBaseInfoList", userBaseInfoList);
		return map;
	}

	public Map getUserSendGifts(String userId, Page<Gift> page) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Gift> list = worthServiceprovider.getGiftService().getUserGifts(userId, page, "send");
		List<String> userIdList = list.stream().map(Gift::getToUserId).collect(Collectors.toList());
		List<CommonUserBaseInfoDto> userBaseInfoList = userClientProxy.getUsers(userIdList);
		map.put("list", list);
		map.put("userBaseInfoList", userBaseInfoList);
		return map;
	}

	/**
	 * 次方法只适用于当前赠送推送 parameterNum=1，用于推送字符串消息唯一的不同之处的替换，巧妙的用了该方法
	 * 
	 * @param gift
	 * @param isToUserId
	 * @param msg
	 * @param title
	 * @param parameterNum
	 */
	private void sendMessage(Gift gift, boolean isToUserId, String msg, String title, int parameterNum) {
		List<SelectFieldEnum> selectFieldEnums = new LinkedList<>();
		selectFieldEnums.add(SelectFieldEnum.NICKNAME);
		String userId = isToUserId == true ? gift.getToUserId() : gift.getFromUserId();
		UserInfoResponseDto dto = invitationFuseAction.selectUserInfoByUserId(userId, selectFieldEnums);
		String nickname = dto != null ? dto.getNickname() : userId;
		Map map = new HashMap();
		map.put("id", gift.getId());
		if (parameterNum == 1) {
			msg = msg.replace("#0", nickname);
		}
		messagePushProxy.multipleParamMessagePushJump(MessageTypeEnum.ACTIVITY_TYPE,msg, title,
				isToUserId == true ? gift.getFromUserId() : gift.getToUserId(), PushMessageDocTypeEnum.WZ_GIFTDETAIL,
				map);
	}

	public boolean add(GiftSendDto giftDto) {
		Gift gift = new Gift();
		boolean ret = worthServiceprovider.getGiftService().add(giftDto, gift);
		if (ret) {
			// 推送消息给收礼者:“XXX送给你一份神秘礼物，请在24小时确认是否接受，逾期将自动取消哦”
			sendMessage(gift, false, "#0送给你一份神秘礼物，请在24小时确认是否接受，逾期将自动取消哦", "赠送礼物收礼者通知", 1);
		}
		return ret;
	}

	public Map<String, Object> detail(String giftId, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Gift gift = worthServiceprovider.getGiftService().selectById(giftId);
		List<String> ids = new ArrayList<String>();
		if (gift.getToUserId().equals(userId)) {
			ids.add(gift.getFromUserId());
		}else {
			ids.add(gift.getToUserId());
		}
		Map<String, UserSynopsisData> userMap = userClientProxy.selectUserMapByIds(ids);
		map.put("gift", gift);
		map.put("userMap", userMap);
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean accept(String userId, String giftId) {
		Gift gift = worthServiceprovider.getGiftService().selectById(giftId);
		boolean success = worthServiceprovider.getGiftService().accept(userId, giftId);
		if (!success) {
			return false;
		}
		// TODO 更新用户表中的总礼物数量
		switch (GiftType.getGiftType(gift.getGiftType())) {
		case MONEY:
			break;
		case CURRENCY:
			break;
		case GOODS:
			break;
		}
		// 推送消息给收礼者:“你赠送给XXXX的礼物，对方已接受，你们已互为好友。加油！”
		sendMessage(gift, true, "你赠送给#0的礼物，对方已接受，你们已互为好友。加油！", "赠送礼物接受赠送者通知", 1);
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean refuse(String userId, String giftId) {
		Gift gift = worthServiceprovider.getGiftService().selectById(giftId);
		boolean success = worthServiceprovider.getGiftService().refuse(userId, giftId);
		if (!success) {
			return false;
		}
		// TODO 同上
		switch (GiftType.getGiftType(gift.getGiftType())) {
		case MONEY:
			break;
		case CURRENCY:
			break;
		case GOODS:
			break;
		}
		// 推送消息给收礼者:“发送信息给赠送者“你赠送给XXXX的礼物，对方已拒收，所有款项均已退回你账户。加油！”
		sendMessage(gift, true, "你赠送给#0的礼物，对方已拒收，所有款项均已退回你账户。加油！", "拒收礼物赠送者通知", 1);
		return true;
	}

}
