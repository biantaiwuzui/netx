package com.netx.worth.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.util.VerificationCodeUtil;
import com.netx.worth.enums.MeetingRegisterStatus;
import com.netx.worth.enums.MeetingSendStatus;
import com.netx.worth.mapper.MeetingSendMapper;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;

@Service
public class MeetingSendService extends ServiceImpl<MeetingSendMapper, MeetingSend> {
    /**
     * 写入日志
     **/
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**发起活动的活动时间内不能有“未完成的活动”**/

    /**
     * 定义List表格方法 用户存储查询到用户某个时间段内的活动列表
     **/
    public List<String> getUnCompleteSend(String userId, long startAt, long endAt, List<Integer> registerStatusList) {
        EntityWrapper<MeetingSend> meetingRegisterWrapper = new EntityWrapper<MeetingSend>(); /** mybatis plus Wrapper 对象 **/
        meetingRegisterWrapper.where("user_id={0}", userId); /** 查询条件 **/
        meetingRegisterWrapper.in("status", registerStatusList); /** in子查询 **/
        meetingRegisterWrapper.lt("started_at", endAt); /** lt 小于 **/
        meetingRegisterWrapper.setSqlSelect("id");
        return (List<String>) (List) selectObjs(meetingRegisterWrapper);/** 返回一列多行数据 **/
    }

    /**
     * 得到发起活动列表
     */
    public List<MeetingSend> getSendList(String userId, Page<MeetingSend> page) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        Page<MeetingSend> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }


    /* 获取活动发起者ID*/
    public List<String> getMeetingSendId(String meetingId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId);
        entityWrapper.setSqlSelect("user_id");
        return (List<String>) (List) selectObjs(entityWrapper);
    }


    public List<String> getMeetingSendId(String userId, MeetingSendStatus meetingSendStatus) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        if (meetingSendStatus != null) {
            entityWrapper.and("status={0}", meetingSendStatus.status);
        }
        entityWrapper.setSqlSelect("meeting_id");
        entityWrapper.groupBy("meeting_id");
        return (List<String>) (List) selectObjs(entityWrapper);
    }

    public List<String> getMeetingSendIdTwo(String userId, MeetingSendStatus meetingSendStatus) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        if (meetingSendStatus != null) {
            entityWrapper.and("status={0}", meetingSendStatus.status);
        }
        entityWrapper.setSqlSelect("meeting_id");
        entityWrapper.orderBy("create_time", false);//降序
        return (List<String>) (List) selectObjs(entityWrapper);
    }


    /**
     * 添加发起人
     */
    public boolean createSender(String userId, String meetingId) {
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setUserId(userId);
        meetingSend.setMeetingId(meetingId);
        meetingSend.setCreateUserId(userId);
        meetingSend.setDefault(true);
        meetingSend.setStatus(MeetingSendStatus.ACCEPT.status);
        return insert(meetingSend);
    }

    /**
     * 添加联合发起人
     */
    public boolean createUnionSender(String sendIds, String id) {
        List<MeetingSend> list = new ArrayList<>();
        for (String userId : sendIds.split(",")) {
            MeetingSend meetingSend = new MeetingSend();
            meetingSend.setUserId(userId.trim());
            meetingSend.setMeetingId(id);
            meetingSend.setDefault(null);
            list.add(meetingSend);
        }
        return insertBatch(list);
    }

    /**
     * 同意联合发起
     */
    public boolean unionAccept(String userId, String meetingId) {
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setStatus(MeetingSendStatus.ACCEPT.status);
        meetingSend.setDefault(true);
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0} and meeting_id={1}", userId, meetingId);
        return update(meetingSend, entityWrapper);
    }

    /**
     * 不同意发起
     */
    public boolean unionRefuse(String userId, String meetingId) {
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setStatus(MeetingSendStatus.REFUSE.status);
        meetingSend.setDefault(false);
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0} and meeting_id={1}", userId, meetingId);
        return update(meetingSend, entityWrapper);
    }

    /**
     * 生成验证码
     */
    public Integer generatorCode(String id, String userId) {
        Integer code = VerificationCodeUtil.generator();
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setCode(code);
        meetingSend.setUpdateTime(new Date());
        meetingSend.setUpdateUserId(userId);
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0} and meeting_id={1}", userId, id);
        boolean success = update(meetingSend, entityWrapper);
        if (!success) {
            return null;
        }
        return code;
    }

    /**
     * 更新验证状态
     */
    public boolean updateValidationStatus(String sendUserId, String meetingId, Integer status) {
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setValidationStatus(status);
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0} and meeting_id={1}", sendUserId, meetingId);
        return update(meetingSend, entityWrapper);
    }

    /**
     * 获取同意发起的列表
     */
    public List<MeetingSend> getAcceptListByMeetingId(String meetingId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and status={1}", meetingId, MeetingSendStatus.ACCEPT.status);
        return selectList(entityWrapper);
    }

    /**
     * 获取验证码
     */
    public Integer getCode(String meetingId, String userId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId).and("user_id={0}", userId);
        MeetingSend meetingSend = selectOne(entityWrapper);
        return meetingSend.getCode();
    }

    /**
     * 根距userId删除发起列表
     *
     * @return
     */
    public boolean deleteMeetingSendListByUserId(String userId) {
        EntityWrapper<MeetingSend> meetingSendWrapper = new EntityWrapper<MeetingSend>();
        meetingSendWrapper.where("user_id={0}", userId);
        return delete(meetingSendWrapper);
    }

    /**
     * 通过用户ID和活动ID查询活动发起
     */
    public MeetingSend selectByMeetingIdAndUserId(String meetingId, String sendUserId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0} and user_id={1}", meetingId, sendUserId);
        return selectOne(entityWrapper);
    }

    /**
     * 通过用户ID和status=1查询活动发起,获取活动id
     */
    public List<String> selectByStatusAndUserId(String sendUserId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect("meeting_id");
        entityWrapper.where("status=1 and user_id={0}", sendUserId);
        entityWrapper.orderBy("create_time");
        return (List<String>) (List) selectObjs(entityWrapper);
    }

    /**
     * 通过活动ID查询活动发起者列表
     */
    public List<MeetingSend> getSendListByMeetingId(String meetingId) {
        EntityWrapper<MeetingSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("meeting_id={0}", meetingId);
        return selectList(entityWrapper);
    }

    /**
     * 通过活动ID和用户id查询活动发起表的数据
     */
    public MeetingSend getSendByUserIdAndMeetingId(String meetingId, String sendUserId) {
        EntityWrapper<MeetingSend> sendWrapper = new EntityWrapper<>();
        sendWrapper.where("meeting_id={0} and user_id={1}", meetingId, sendUserId);
        return selectOne(sendWrapper);
    }

    public int getAttendSendCount(String meetingId) {
        EntityWrapper<MeetingSend> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0} and validation_status={1}", meetingId, 1);
        return selectCount(wrapper);
    }

    /* 纯线上活动校验 **/
    public boolean allThrough(String meetingId, Integer status) {
        MeetingSend meetingSend = new MeetingSend();
        meetingSend.setValidationStatus(status);
        EntityWrapper<MeetingSend> wrapper = new EntityWrapper<>();
        wrapper.where("meeting_id={0} and status={1}", meetingId, status);
        return update(meetingSend, wrapper);
    }

    /* 获取发起者userId **/
    public String getMeetingSendUserId(String meetingId, String userId, Integer... status) {
        EntityWrapper<MeetingSend> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id");
        wrapper.where("meeting_id={0} and user_id={1}", meetingId, userId);
        wrapper.in("status", status);
        return (String) selectObj(wrapper);
    }
}
