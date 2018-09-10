package com.netx.worth.biz.meeting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netx.worth.enums.MeetingRegisterStatus;
import com.netx.worth.enums.MeetingStatus;
import com.netx.worth.enums.MeetingType;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;

/**
 * <p>
 * 活动聚会报名表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class MeetingRegisterAction{
	@Autowired
	private MeetingSendAction meetingSendAction;
	@Autowired
	private WorthServiceprovider worthServiceprovider;
	
	/* 校验验证码、确认出场 **/
	public boolean codeValidated(String userId, String meetingId,String sendUserId,Integer status) throws Exception{
		MeetingRegister meetingRegister = new MeetingRegister();
		meetingRegister.setValidationStatus(status);
		Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
		if(meeting.getStatus()!=5){
		    throw new Exception("当前活动状态下不能进行出席校验");
        }
        MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().getSendByUserIdAndMeetingId(meetingId, sendUserId);
		if(meetingSend==null){
			throw new Exception("未找到该活动发起信息"); 
		}
		if(meetingSend.getStatus()==1) {
		    if(!worthServiceprovider.getMeetingSendService().updateValidationStatus(sendUserId,meetingId,status)){
		        throw new Exception("更新发起者验证状态异常！"); 
		    } 
		}else{
		    throw new Exception("当前状态不能进行出席校验"); 
		}
        return worthServiceprovider.getMeetingRegisterService().updateValidationStatus(meetingId,userId,meetingRegister);
	}
	
	public BigDecimal getAllRegisterAmount(String id) {
		BigDecimal amount = new BigDecimal("0");
		for (MeetingRegister mr : worthServiceprovider.getMeetingRegisterService().selectSuccessRegListByMeetingId(id)) {
			amount.add(BigDecimal.valueOf(mr.getFee()));
		}
		return amount.divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
	}
	
	public boolean hasValidCode(String meetingId) {
		List<MeetingRegister> list = worthServiceprovider.getMeetingRegisterService().selectRegListByMeetingId(meetingId);
		for (MeetingRegister meetingRegister : list) {
			if (meetingRegister.getValidationStatus()==1) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	//确定活动入选者
	public boolean regSuccess(String meetingId) {
		Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
		meeting.setStatus(MeetingStatus.STOP.status);
		List<String> sendList = worthServiceprovider.getMeetingSendService().getMeetingSendId(meetingId);
		List<MeetingRegister> filterList = worthServiceprovider.getMeetingRegisterService().selectRegisterdRegListByMeetingId(meetingId,sendList);
		int regSuccessCount = 0;
		Map<String, MeetingRegister> map = filterList.stream().collect(Collectors.toMap(MeetingRegister::getId, e ->e));
		List<String> pool = getPoolByAmount(filterList);
		Collections.shuffle(pool);//打乱顺序
		if (!meeting.getMeetingType().equals(MeetingType.HAS_ONE.type)) {// 不是1对1活动
			if (filterList.size() <= meeting.getCeil()) {// 如果报名人数小于入选者上限
				for (MeetingRegister meetingRegister : filterList) {
					meetingRegister.setStatus(MeetingRegisterStatus.SUCCESS.status);
				}
				regSuccessCount = filterList.size();
				meeting.setRegSuccessCount(regSuccessCount);
				return worthServiceprovider.getMeetingRegisterService().updateBatchById(filterList) && meeting.insertOrUpdate();
			} else {// 如果报名人数大于入选者上限
				int ceil = 0;//人选人数
				Set<String> regSuccessSet = new HashSet<>();
				Set<String> regFailSet = new HashSet<>();
				for (int i = 0; i < filterList.size(); i++) {
					boolean add = regSuccessSet.add(pool.get(i));
					if(add) {
						ceil++; 
					}
					if (ceil > meeting.getCeil()) {
						regFailSet.add(pool.get(i));
					}
				}
				regSuccessCount = meeting.getCeil();
				meeting.setRegSuccessCount(regSuccessCount);
				//入选的
				MeetingRegister success = new MeetingRegister();
				success.setStatus(MeetingRegisterStatus.SUCCESS.status);
				//未入选
				MeetingRegister fail = new MeetingRegister();
				fail.setStatus(MeetingRegisterStatus.FAIL.status);
				return meeting.insertOrUpdate() && worthServiceprovider.getMeetingRegisterService().successReg(regSuccessSet, success) && worthServiceprovider.getMeetingRegisterService().failReg(regFailSet, fail);
			}
		} else {// 如果是活动(一对一)
			List<MeetingRegister> oneSuccess = new ArrayList<>();
			MeetingRegister success= null;//标记
			for (int i = 0;i<pool.size();i++) {
				if(i==0) {
					MeetingRegister successReg = map.get(pool.get(i));
					success = successReg;
					successReg.setStatus(MeetingRegisterStatus.SUCCESS.status);
					oneSuccess.add(successReg);
				}else {
					MeetingRegister failReg = map.get(pool.get(i));
					if(failReg == success) {
						continue;
					}
					failReg.setStatus(MeetingRegisterStatus.FAIL.status);
					oneSuccess.add(failReg);
				}
			}
			regSuccessCount = 1;
			meeting.setRegSuccessCount(regSuccessCount);
			return worthServiceprovider.getMeetingRegisterService().updateBatchById(oneSuccess) && meeting.insertOrUpdate();
		}
	}
	
	public List<String> getPoolByAmount(List<MeetingRegister> list) {
		List<String> pool = new ArrayList<>();
		Map<String, Integer> map = list.stream().collect(Collectors.toMap(MeetingRegister::getId, MeetingRegister::getStatus));
		Set<Entry<String,Integer>> entrySet = map.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			Integer value = entry.getValue();
			for(int i = 0; i < value; i ++) {
				pool.add(entry.getKey());
			}
		}
		return pool;
	}
	
	
	/* 纯上线活动出席校验 **/
	public boolean codeValidated(String meetingId,Integer status) throws Exception {
//        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
//        if(meeting.getStatus()!=5){
//            throw new RuntimeException("当前活动状态下不能进行出席校验");
//        }
        return worthServiceprovider.getMeetingRegisterService().allThrough(meetingId,status) && worthServiceprovider.getMeetingSendService().allThrough(meetingId,status);
    }
    
//            if(send==null){
//        MeetingRegister register = worthServiceprovider.getMeetingRegisterService().getRegisterStatus(userId,meetingId);
//        if(register==null){
//            throw new RuntimeException("不存在此校验对象！");
//        }
//        if(register.getStatus()!=5 && register.getValidationStatus()!=0){
//            throw new RuntimeException("你的当前状态下不能进行校验");
//        }
//        register.setValidationStatus(1);
//        return worthServiceprovider.getMeetingRegisterService().updateValidationStatus(meetingId,userId,register);
//    }
//	    return worthServiceprovider.getMeetingSendService().updateValidationStatus(userId,meetingId,status);


}
