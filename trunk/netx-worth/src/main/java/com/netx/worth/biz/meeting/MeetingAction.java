package com.netx.worth.biz.meeting;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.wz.dto.meeting.MeetingUserSendListDto;
import com.netx.utils.DistrictUtil;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.enums.MeetingSendStatus;
import com.netx.worth.enums.MeetingType;
import com.netx.worth.service.WorthServiceprovider;
import com.netx.worth.vo.MeetingConflictDto;
import com.netx.worth.vo.MeetingDetailSendDto;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.MeetingRegisterStatus;
import com.netx.worth.enums.MeetingStatus;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;
import com.netx.worth.model.Settlement;

/**
 * <p>
 * 活动聚会表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class MeetingAction{

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private MeetingRegisterAction meetingRegisterAction;
	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private AddImgUrlPreUtil addImgUrlPreUtil;
	@Autowired
	private MeetingSendAction meetingSendAction;
	@Autowired
	private WorthServiceprovider worthServiceprovider;
	
	/* 获取用户发起的活动列表 */
	public Map<String, Object> getUserSendList(String userId, Page<MeetingSend> page) {
		Map<String, Object> map = new HashMap<>();
		List<MeetingSend> list = worthServiceprovider.getMeetingSendService().getSendList(userId, page);
		if (list.size() > 0) {
			List<String> meetingIds = list.stream().map(MeetingSend::getMeetingId).collect(Collectors.toList());
			List<Meeting> meetings = worthServiceprovider.getMeetingService().getMeetingListByIds(meetingIds);
			List<MeetingUserSendListDto> listDtos = new ArrayList<>();
			if(meetings != null && meetings.size() > 0) {
				for (Meeting meeting : meetings) {
					meeting.setMeetingImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket)));
	            	meeting.setPosterImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getPosterImagesUrl(), AliyunBucketType.ActivityBucket)));
				    MeetingUserSendListDto meetingUserSendListDto = new MeetingUserSendListDto();
                    VoPoConverter.copyProperties(meeting,meetingUserSendListDto);
                    meetingUserSendListDto.setBalance(getAmount(meeting.getBalance()));
                    meetingUserSendListDto.setAmount(getAmount(meeting.getAmount()));
                    meetingUserSendListDto.setAllRegisterAmount(getAmount(meeting.getAllRegisterAmount()));
                    meetingUserSendListDto.setOrderPrice(getAmount(meeting.getOrderPrice()));
                    listDtos.add(meetingUserSendListDto);
				}
			}
			map.put("meetingHash",listDtos);
		}
		return map;
	}

	private BigDecimal getAmount(Long cent){
		return Money.CentToYuan(cent).getAmount();
	}
	
	/* 同意联合发起的活动 */
	public boolean unionAccept(String userId, String meetingId) {
	    Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
	    if(meeting == null){
	        throw new RuntimeException("该活动不存在");
        }
        if(!meeting.getStatus().equals(MeetingStatus.WAITING.status)){
            return false;
        }
		MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().getSendByUserIdAndMeetingId(meetingId, userId);
		if(meetingSend==null){
			throw new RuntimeException("(⊙﹏⊙)你非该活动的联合发起人");
		}
        boolean success = worthServiceprovider.getMeetingSendService().unionAccept(userId, meetingId);
		if(!success){
		    throw new RuntimeException("程序手抖了，没处理成功");
        }
		return success;
	}
	
	/* 拒绝联合发起活动 */
	public boolean unionRefuse(String userId, String meetingId) {
	    Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
		MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().getSendByUserIdAndMeetingId(meetingId,userId);
        if(!meeting.getStatus().equals(MeetingStatus.WAITING.status)){
            throw new RuntimeException("已过处理时间");
        }
		if(meetingSend==null){
			throw new RuntimeException("你非该活动的联合发起人");
		}
		return worthServiceprovider.getMeetingSendService().unionRefuse(userId, meetingId);
	}

	/* 参与活动者申请退出活动 */
	public boolean cancelReg(String meetingId, String userId) {
		MeetingRegister meetingRegister = worthServiceprovider.getMeetingRegisterService().getRegisterStatus(userId,meetingId);
		if(meetingRegister == null){
		    logger.error("活动ID:"+meetingId+"报名者ID:"+userId);
		    throw new RuntimeException("咦，您还没报名？o(*^＠^*)o");
        }
		if(meetingRegister.getStatus().equals(MeetingRegisterStatus.CANCEL.status)){
		    throw new RuntimeException("噢~别挠我了，你已经退出了");
        }
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if(meetingRegister.getStatus().equals(MeetingRegisterStatus.FAIL.status) || (!meeting.getStatus().equals(MeetingStatus.WAITING.status) && !meetingRegister.getStatus().equals(MeetingRegisterStatus.SUCCESS.status))){
		    throw new RuntimeException("您未入选，不用退出");
        }
		/* 确定入选人之后，非入选者不可退出活动 **/
        if((new Date().getTime())>meeting.getRegStopAt().getTime() && !meetingRegister.getStatus().equals(MeetingRegisterStatus.SUCCESS.status)){
            throw new RuntimeException("确定入选人之后非入选者不可退出活动");
        }
        meeting.setRegCount(meeting.getRegCount()-meetingRegister.getAmount());
		meeting.setAllRegisterAmount(meeting.getAllRegisterAmount() - (meetingRegister.getFee()));
		return worthServiceprovider.getMeetingService().updateById(meeting);
	}

	/* 活动发起人同意开始活动 */
	@Transactional
	public Map<String, Object> sendAccept(String meetingId, String userId, Double lat, Double lon) throws Exception {
		Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
		if(meeting ==null){
            throw new RuntimeException("没有该活动信息");
        }
        if(StringUtils.isBlank(worthServiceprovider.getMeetingSendService().getMeetingSendUserId(meetingId,userId,1))){
		    throw new RuntimeException("非发起人，不可同意开始活动");
        }
		/* 如果meeting活动状态等于关闭 则抛出异常**/
		if (!meeting.getStatus().equals(MeetingStatus.STOP.status))
			throw new RuntimeException("请检查活动状态、只有截止报名并选定入选人才能获取验证码");
		if (!meeting.getConfirm())
			throw new RuntimeException("没有确认细节就不能开始");
		if (!worthServiceprovider.getMeetingService().updateStatusByIdAndUserId(MeetingStatus.CODE_GENERATOR, meetingId, meeting.getUserId())) {
			logger.error("根据状态值更新所传ID的记录失败");
			throw new RuntimeException("数据更新异常");
		}
		Map<String,Object> map = new HashMap<>();
		List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
		if(meeting.getMeetingType().equals(MeetingType.ONLINE.getType())){//如果是纯上线活动，则不用生成验证码！直接开始
            map.put("result",meetingRegisterAction.codeValidated(meetingId,1));
        } else {
		    Double distance = DistrictUtil.calcDistance(meeting.getLat().doubleValue(),meeting.getLon().doubleValue(),lat,lon);
			if(distance * 1000 <= 300){
                sendList.forEach(sendId -> {
                    if (sendId.getStatus() == 1) {
                        map.put(sendId.getUserId(), worthServiceprovider.getMeetingSendService().generatorCode(meetingId, sendId.getUserId()));
                    }
                });
                if (map.isEmpty()) {
                    logger.error(meetingId+"生成验证码失败");
                    throw new RuntimeException("生成验证码失败");
                }
            }else {
			    map.put("distance",distance * 1000 - 300);
            }
            
        }
		return map;
	}

	//确认活动细节页面
	public boolean confirmDetail(String meetingId) {
	    Meeting meeting= new Meeting();
	    meeting.setConfirm(true);
	    meeting.setId(meetingId);
		return worthServiceprovider.getMeetingService().updateMeeting(meeting);
	}
	
	//补足成功支付回掉
	public boolean payBalanceCallback(String id, String userId) {
		Meeting meeting = worthServiceprovider.getMeetingService ().selectById ( id );
		if(meeting.getBalancePay ()){
			return false;
		}
		meeting.setBalancePay(true);
		meeting.setPayFrom(userId);
		meeting.setConfirm(true);
		return worthServiceprovider.getMeetingService().editMeeting(id, meeting);
	}
	
	public Map<String, List> nearHasOneList(String userId, BigDecimal lon, BigDecimal lat, Double length,Page<Meeting> page) {
		Map<String, List> map = new HashMap<>();
		List<Meeting> list = worthServiceprovider.getMeetingService().nearHasOneList(userId, lon, lat, length, page);
		if(list != null && list.size() > 0) {
			for (Meeting meeting : list) {
				meeting.setMeetingImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket)));
            	meeting.setPosterImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getPosterImagesUrl(), AliyunBucketType.ActivityBucket)));
			}
		}
		//TODO
//		map.put("list", DistrictUtil.getDistrictVoList(lat, lon, list));
		return map;
	}
	
	/* 活动失败 */
	public void meetingFail(Meeting meeting, int regCount) {
	    meeting.setStatusDescription("报名人数低于"+meeting.getFloor());
		meeting.setStatus(MeetingStatus.FAIL.status); /* 设置活动失败 **/
		meeting.setRegSuccessCount(regCount); /* 重新设置活动已选入人数 **/
        worthServiceprovider.getMeetingService().updateById(meeting);
	}
	
	/* 截止活动报名 */
	public void stopRegister(Meeting meeting, int regCount) {
		meeting.setStatus(MeetingStatus.STOP.status); /* 设置活动状态为截止报名并已选定入选人 **/
		meeting.setRegSuccessCount(regCount); /* 设置活动入选人数 **/
		/* 如果报名总费用小于活动订单费用，则计算活动差额 **/
		if (meeting.getAllRegisterAmount().doubleValue() < meeting.getOrderPrice().doubleValue()) {
			meeting.setBalance(meeting.getAllRegisterAmount() - (meeting.getOrderPrice()));
		}
		worthServiceprovider.getMeetingService().updateById(meeting);
	}

	/* 确认出席、准备校验验证码 */
	public boolean registerStart(String meetingId, String userId) {
		return worthServiceprovider.getMeetingRegisterService().registerStart(meetingId, userId);
	}
	
	//todo 未使用
	// 定时任务：结算时检查出席校验是否通过
	public void checkSuccess(String meetingId) throws Exception {
		Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
		Long totalWage = null; // 报名总费用
		Long wage = null;   // 每个活动发起者可以获得金额
		if (meeting.getBalance().doubleValue() <= 0)
			totalWage = meeting.getAllRegisterAmount() - (meeting.getOrderPrice());
		// 获取所有的活动入选者
		List<MeetingRegister> meetingRegisters = worthServiceprovider.getMeetingRegisterService().getRegSuccessListByMeetingId(meetingId);
		// 获取所有的活动同意发起者
		List<MeetingSend> meetingSends = worthServiceprovider.getMeetingSendService().getAcceptListByMeetingId(meetingId);
		// 如果报名总费用不为空，并且活动发起者大于0
		if (totalWage != null && meetingSends.size() > 0) {
		    // 活动报名总费用除以活动发起者
			Money money = Money.CentToYuan(totalWage).divide(new BigDecimal(meetingSends.size()));
			wage = money.getCent();
		}
		meetingRegisters.forEach(meetingRegister -> {
		    // 如果活动报名者的验证状态==0 或者 ==2时
			if (meetingRegister.getValidationStatus()==0 || meetingRegister.getValidationStatus()==2) {
			    // 给予未成功通过验证的活动参与者扣减信用
				settlementAction.settlementCredit("Meeting", meetingId, meetingRegister.getUserId(), -2);
			}
		});
		for (MeetingSend meetingSend : meetingSends) {
		    // 如果活动发起者的出席验证未通过
			if (meetingSend.getValidationStatus() !=1) {
			    // 给予未成功通过验证的活动发布者扣减信用
				settlementAction.settlementCredit("Meeting", meetingId, meetingSend.getUserId(), -5);
			} else {
				if (wage != null) { // 如果发动发起者有收益金额
                    //活动结算时将活动分得的收益金额赏给活动发起者
					settlementAction.settlementAmountRightNow("结算活动费用", "Meeting", meetingId, meetingSend.getUserId(), Money.CentToYuan(wage).getAmount());
				}
			}
		}
	}
	
	/* 检查用户是否有未完成的活动 */
	public boolean checkHasUnComplete(String userId) {
		// 查询该用户发布的活动中状态为“同意开始，分发验证码”、“报名截止，已确定入选人”、“已发起，报名中”并且还未结束的活动。
		List<Meeting> meetingList = worthServiceprovider.getMeetingService().getUnComplete(userId);
		if (meetingList != null && meetingList.size() > 0) {
			return true;
		}
		// 查询该用户申请表中状态为“已入选”或者“确认出席，准备校验验证码”
		List<Integer> registerStatusList = new ArrayList<Integer>();
		registerStatusList.add(MeetingRegisterStatus.SUCCESS.status);
		registerStatusList.add(MeetingRegisterStatus.START.status);

		List<MeetingRegister> meetingRegisterList = worthServiceprovider.getMeetingRegisterService().getRegListByStatus(userId, registerStatusList);
		List<String> meetingIds = meetingRegisterList.stream().map(MeetingRegister::getMeetingId).collect(toList());
		if (meetingIds != null && meetingIds.size() > 0) {
			List<Meeting> list = worthServiceprovider.getMeetingService().selectBatchIds(meetingIds);
			Integer status = null;
			if (list != null && list.size() > 0) {
				for (Meeting meeting : list) {
					status = meeting.getStatus();
					// 时间在结束时间之前并且状态是报名截止,已确定入选人或者同意开始,分发验证码
					if (meeting.getEndAt().getTime() >= new Date().getTime()
							&& (status.equals(MeetingStatus.STOP.status) || status.equals(MeetingStatus.CODE_GENERATOR.status))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Transactional
	public boolean clean(String userId) throws Exception {
		List<Meeting> list = worthServiceprovider.getMeetingService().getMeetingListByUserId(userId);
		List<String> meetingIds = list.stream().map(Meeting::getId).collect(toList());
		boolean cleanSettlment = true;
		boolean cleanMeeting = true;
		// 没有数据的话直接返回true
		if (meetingIds != null && meetingIds.size() > 0) {
			// 删除结算表和子表
			List<Settlement> settlementList = worthServiceprovider.getSettlementService().getSettlementListByTypeAndId("Meeting", meetingIds);
			List<String> settlementIds = settlementList.stream().map(Settlement::getId).collect(toList());
			cleanSettlment = worthServiceprovider.getSettlementService().deleteSettlementListByTypeAndId("Meeting", meetingIds) && settlementAction.clean(settlementIds);
			cleanMeeting = worthServiceprovider.getMeetingService().deleteMeetingListByUserId(userId);
		}

		boolean cleanMeetingRegister = worthServiceprovider.getMeetingRegisterService().deleteMeetingRegisterListByUserId(userId);
		boolean cleanMeetingSend = worthServiceprovider.getMeetingSendService().deleteMeetingSendListByUserId(userId);

		boolean success = cleanMeeting && cleanMeetingSend && cleanMeetingRegister && cleanSettlment;
		if (success) {
			return success;
		} else {
			logger.error("清除该用户活动聚会的数据失败");
			throw new RuntimeException();
		}
	}
	
	/* 检查这段时间内该用户是否有未完成的活动 **/
	public Object checkHasUnComplete(String userId, Long registerAt, Long startAt, Long endAt,String id) {
		if(startAt-registerAt<1800000){
			throw new RuntimeException("报名截止时间必须提前于活动开始时间30分钟以上");
		}
		List<Integer> meetingStatusList = new ArrayList<Integer>();
		meetingStatusList.add(MeetingStatus.CODE_GENERATOR.status);
		meetingStatusList.add(MeetingStatus.WAITING.status);
		meetingStatusList.add(MeetingStatus.STOP.status);
		//检测是否有活动发起了但未完成的
		List<Meeting> conflict = worthServiceprovider.getMeetingService().getUnComplete(userId, startAt, endAt,meetingStatusList,id);
		List<MeetingConflictDto> conflictDtoList = new ArrayList<>();
		if(conflict.size()>0){
            for (Meeting meeting : conflict) {
                conflictDtoList.add(conflictDto(meeting));
            }
            return conflictDtoList;
        }else {
		    return true;
        }
	}

	private MeetingConflictDto conflictDto(Meeting meeting){
	    MeetingConflictDto meetingConflictDto = new MeetingConflictDto();
	    if(meeting!=null){
	        VoPoConverter.copyProperties(meeting,meetingConflictDto);
	        meetingConflictDto.setAmount(Money.CentToYuan(meeting.getAmount()).getAmount());
        }
	    return meetingConflictDto;
    }
	
	/* 获取验证码 */
	public Map<String,Object> getCode(String meetingId, String userId, Double lat, Double lon) throws Exception{
	    Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
	    if(meeting.getStatus() != 5){
	        throw new Exception("当前活动状态下不允许获取验证码！");
        }
        Map<String,Object> map = new HashMap<>();
        Double distance = DistrictUtil.calcDistance(meeting.getLat().doubleValue(),meeting.getLon().doubleValue(),lat,lon);
        if(distance * 1000 <=300){
            map.put("code",worthServiceprovider.getMeetingSendService().getCode(meetingId, userId));
            return map;
        } else {
            map.put("Distance",distance * 1000 - 300);
            return map;
        }
		
	}


    //获取单条联合活动信息
    public Boolean SeeMeetingNews(String meetingId,String userId){
        /* 获取联合发起者信息 **/
        MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().selectByMeetingIdAndUserId(meetingId,userId);
        if(meetingSend!=null){
            Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
            /* 当当前时间超过活动确定入选人时间时改为已处理 **/
            if (!meetingSend.getStatus().equals(MeetingSendStatus.WAITING.status) || (new Date().getTime())>meeting.getRegStopAt().getTime()) {
                return true;
            } else {
                return false;
            }
        }
        return null;
    }


	//获取用户一条最新的活动
	public MeetingDetailSendDto getMeetingById(String id) {
		MeetingDetailSendDto meetingDetailSendDto = new MeetingDetailSendDto();
		Meeting latestMeeting = worthServiceprovider.getMeetingService().selectById(id);
		if (latestMeeting == null) {
			return null;
		}
		VoPoConverter.copyProperties(latestMeeting, meetingDetailSendDto);
		meetingDetailSendDto.setMeetingImagesUrl(addImgUrlPreUtil.addImgUrlPres(latestMeeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket));
		meetingDetailSendDto.setMerchantId(latestMeeting.getPosterImagesUrl());
		meetingDetailSendDto.setMeetingId(latestMeeting.getId());
		meetingDetailSendDto.setAmount(latestMeeting.getAmount());
		return meetingDetailSendDto;
	}
}
