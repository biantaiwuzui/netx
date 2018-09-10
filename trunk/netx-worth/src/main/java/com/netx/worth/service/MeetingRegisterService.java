package com.netx.worth.service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.meeting.RegMeetingDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.MeetingRegisterStatus;
import com.netx.worth.mapper.MeetingRegisterMapper;
import com.netx.worth.model.MeetingRegister;

@Service
public class MeetingRegisterService extends ServiceImpl<MeetingRegisterMapper, MeetingRegister> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 得到分页申请列表
     */
    public List<MeetingRegister> getRegList(String userId, Page<MeetingRegister> page) {
        Wrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        Page<MeetingRegister> selectPage = selectPage(page, wrapper);
        return selectPage.getRecords();
    }
    /**
     * 得到分页申请列表
     * create by FRWIN
     */
    public List<MeetingRegister> getRegListTwo(String userId, Page<MeetingRegister> page) {
        Wrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        wrapper.orderBy ( "create_time",false );//降序
        Page<MeetingRegister> selectPage = selectPage(page, wrapper);
        return selectPage.getRecords();
    }

    /**
     * 获取确认出席的申请单数据
     *
     * @return
     */
    public List<MeetingRegister> getStartListById(String meetingId) {
        EntityWrapper<MeetingRegister> registerWrapper = new EntityWrapper<MeetingRegister>();
        registerWrapper.where("meeting_id={0}", meetingId);
        registerWrapper.and("status={0}", MeetingRegisterStatus.START.status);
        return selectList(registerWrapper);
    }

    /**
     * 取消申请
     */
    public boolean cancelReg(String meetingId, String userId) {
        MeetingRegister meetingRegister = new MeetingRegister();
        meetingRegister.setStatus(MeetingRegisterStatus.CANCEL.status);
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", meetingId, userId);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 更新验证状态
     */
    public boolean updateValidationStatus(String meetingId, String userId, MeetingRegister meetingRegister) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", meetingId, userId);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 获取申请列表ById
     */
    public List<MeetingRegister> getRegListByMeetingId(String meetingId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId);
        return selectList(entityWrapper);
    }

    /**
     * 确认出席
     */
    public boolean registerStart(String meetingId, String userId) {
        MeetingRegister meetingRegister = new MeetingRegister();
        meetingRegister.setStatus(MeetingRegisterStatus.START.status);
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", meetingId, userId);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 获取报名的列表
     */
    public List<MeetingRegister> getRegSuccessListByMeetingId(String meetingId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId).and("status={0}", MeetingRegisterStatus.REGISTERD.status);
        return selectList(entityWrapper);
    }

    /**
     * 通过状态列表查询申请列表
     *
     * @return
     */
    public List<MeetingRegister> getRegListByStatus(String userId, List<Integer> registerStatusList) {
        EntityWrapper<MeetingRegister> meetingRegisterWrapper = new EntityWrapper<MeetingRegister>();
        meetingRegisterWrapper.where("user_id={0}", userId);
        meetingRegisterWrapper.in("status", registerStatusList);
        return selectList(meetingRegisterWrapper);
    }

    /**
     * 通过id查询已入选人员
     *
     * @return
     */
    public List<MeetingRegister> getSuccessRegListById(String meetingId) {
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<MeetingRegister>();
        wrapper.where("meeting_id={0}", meetingId);
        wrapper.where("status={0}", MeetingRegisterStatus.SUCCESS.status);
        return selectList(wrapper);
    }

    /**
     * 通过id查询未入选人员
     * @author hgb
     * @return
     */
    public List<MeetingRegister> getFailRegListById(String meetingId) {
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<MeetingRegister>();
        wrapper.where("meeting_id={0}", meetingId);
        wrapper.where("status={0}", MeetingRegisterStatus.FAIL.status);
        return selectList(wrapper);
    }

    /**
     * 根距userId删除申请列表
     *
     * @return
     */
    public boolean deleteMeetingRegisterListByUserId(String userId) {
        EntityWrapper<MeetingRegister> meetingRegisterWrapper = new EntityWrapper<MeetingRegister>();
        meetingRegisterWrapper.where("user_id={0}", userId);
        return delete(meetingRegisterWrapper);
    }

    /**
     * 活动报名
     */
    @Transactional
    public boolean register(RegMeetingDto regMeetingDto, String Id) {
        MeetingRegister meetingRegister = new MeetingRegister();
        meetingRegister.setMeetingId(regMeetingDto.getMeetingId());
        meetingRegister.setUserId(Id);
        meetingRegister.setFriends(regMeetingDto.getFriends());
        meetingRegister.setAnonymity(regMeetingDto.getAnonymity());
        meetingRegister.setAmount(regMeetingDto.getAmount());
        meetingRegister.setFee(new Money(regMeetingDto.getFee()).getCent());
        meetingRegister.setPay(true);
        meetingRegister.setStatus(MeetingRegisterStatus.REGISTERD.status);
        return insert(meetingRegister);
    }

    /**
     * 修改验证码验证次数
     */
    public boolean updateVerificationCode(String userId, String meetingId, int times) {
        MeetingRegister meetingRegister = new MeetingRegister();
        meetingRegister.setTimes(times);
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", meetingId, userId);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 发起活动的活动时间内不能有“未完成的活动”
     */
    public List<MeetingRegister> getUnCompleteRegister(String userId, long startAt, long endAt) {
        List<Integer> registerStatusList = new ArrayList<Integer>();
        registerStatusList.add(MeetingRegisterStatus.SUCCESS.status);
        registerStatusList.add(MeetingRegisterStatus.START.status);

        EntityWrapper<MeetingRegister> meetingRegisterWrapper = new EntityWrapper<MeetingRegister>();
        meetingRegisterWrapper.where("user_id={0}", userId);
        meetingRegisterWrapper.in("status", registerStatusList);
        meetingRegisterWrapper.gt("end_at", startAt);
        meetingRegisterWrapper.lt("started_at", endAt);
        return selectList(meetingRegisterWrapper);
    }

    /**
     * 根据userId或meetingId修改申请表
     */
    public boolean updateByUserIDAndMeetingId(String meetingId, String userId, MeetingRegister meetingRegister) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1} and status={2}", meetingId, userId, 1);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 根据活动ID和用户id查询验证码使用次数
     **/
    public MeetingRegister selectByMeetingIdAndUserId(String id, String userId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", id, userId);
        return selectOne(entityWrapper);
    }

    /**
     * 通过meetingId查询所有入选成功的申请单
     */
    public List<MeetingRegister> selectSuccessRegListByMeetingId(String meetingId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId).and("status={0}", MeetingRegisterStatus.SUCCESS.status);
        return selectList(entityWrapper);
    }

    /**
     * 通过meetingId查询所有已报名的申请单
     */
    public List<MeetingRegister> selectRegisterdRegListByMeetingId(String meetingId,List<String> sendUserId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId).and("status={0}", MeetingRegisterStatus.REGISTERD.status);
        entityWrapper.notIn("user_id",sendUserId);
        return selectList(entityWrapper);
    }

    /**
     * 通过meetingId查询所有申请单
     */
    public List<MeetingRegister> selectRegListByMeetingId(String meetingId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId);
        return selectList(entityWrapper);
    }

    /**
     * 通过meetingId修改RegEntity
     */
    public boolean updateRegEntityById(String meetingId, MeetingRegister meetingRegister) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", meetingId);
        return update(meetingRegister, entityWrapper);
    }

    /**
     * 设置为已入选
     */
    public boolean successReg(Set<String> regSuccessSet, MeetingRegister meetingRegister) {
        meetingRegister.setUpdateTime(new Date());
        EntityWrapper<MeetingRegister> successWrapper = new EntityWrapper<>();
        successWrapper.in("id", regSuccessSet);
        return update(meetingRegister, successWrapper);
    }

    /**
     * 设置为未入选
     */
    public boolean failReg(Set<String> regFailSet, MeetingRegister meetingRegister) {
        meetingRegister.setUpdateTime(new Date());
        EntityWrapper<MeetingRegister> failWrapper = new EntityWrapper<>();
        failWrapper.in("id", regFailSet);
        return update(meetingRegister, failWrapper);
    }

    /**
     * 通过meetingId查询所有申请单数量
     */
    public int getAllRegisterCount(String meetingId, List<String> sendUserId) {
        EntityWrapper<MeetingRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and status=1", meetingId)
                    .notIn("user_id", sendUserId);
        return selectCount(entityWrapper);
    }

    /**
     * 查询报名后的用户信息
     **/
    public List<MeetingRegister> getRegisterSuccessUserId(String meetingId) {
        Wrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0}", meetingId);
        return selectList(wrapper);
    }

    /**
     * 根据userId meetingId 查询个人活动信息
     **/
    public MeetingRegister getRegisterStatus(String userId, String meetingId) {
        Wrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and meeting_id={1}", userId, meetingId);
        wrapper.orderBy("create_time desc");
        return selectOne(wrapper);
    }


    /**
     * 查询报名后的用户信息
     **/
    public List<MeetingRegister> getRegisterSuccessUserId(String id, Page<MeetingRegister> page) {
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("meeting_id={0}", id);
        Page<MeetingRegister> selectPage = selectPage(page, wrapper);
        return selectPage.getRecords();
    }

    /**
     * 获取所有预约信息
     *
     * @return
     */
    public List<Map<String, Object>> queryMeetingStat(Collection<?> userIds) {
        Wrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id as userId,sum(fee) as total");
        wrapper.where("deleted=0 and status!={0} and is_pay={1}", MeetingRegisterStatus.CANCEL.status, true);
        wrapper.in("user_id", userIds);
        wrapper.groupBy("userId");
        return selectMaps(wrapper);
    }

    public long getMeetingIncomeTotal(String[] meetingIds) {
        EntityWrapper<MeetingRegister> skillOrderWrapper = new EntityWrapper<>();
        skillOrderWrapper.in("meeting_id", meetingIds);
        skillOrderWrapper.where("status={0} and is_pay={1} and deleted=0", MeetingRegisterStatus.START.status, true);
        skillOrderWrapper.setSqlSelect("sum(fee)");
        Long total = (Long) selectObj(skillOrderWrapper);
        return total == null ? 0 : total;
    }

    /* 统计除发起者外,并且状态不为4的报名人数 **/
    public int getNotSendCount(String meetingId, List<String> sendId) {
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0}", meetingId);
        wrapper.notIn("user_id", sendId);
        wrapper.notIn("status", 4);
        return selectCount(wrapper);
    }

    /* 已出席并校验 **/
    public List<MeetingRegister> getAttend(String meetingId) {
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0} and validation_status={1}", meetingId, 1);
        return selectList(wrapper);
    }


    /* 纯线上活动校验者 */
    public boolean allThrough(String meetingId, Integer status) {
        MeetingRegister meetingRegister = new MeetingRegister();
        meetingRegister.setValidationStatus(status);
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0} and status in (2,5)", meetingId);
        return update(meetingRegister, wrapper);
    }
    
    /* 获取有效报名者 **/
    public List<MeetingRegister> registerStatusList(String meetingId,Integer... status){
        EntityWrapper<MeetingRegister> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0}",meetingId);
        wrapper.in("status",status);
        return selectList(wrapper);
    }
}
