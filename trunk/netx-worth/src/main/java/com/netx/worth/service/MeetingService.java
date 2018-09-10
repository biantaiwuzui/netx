package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.worth.model.MeetingSend;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.meeting.ModifyMeetingDto;
import com.netx.common.wz.dto.meeting.SendMeetingDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.MeetingStatus;
import com.netx.worth.enums.MeetingType;
import com.netx.worth.mapper.MeetingMapper;
import com.netx.worth.model.Meeting;

@Service
public class MeetingService extends ServiceImpl<MeetingMapper, Meeting> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 根据ids查询排序后的活动列表
     */
    public List<Meeting> getMeetingListByIds(List<String> meetingIds) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<Meeting>();
        entityWrapper.in("id", meetingIds);
        entityWrapper.orderBy(" FIELD(status, 0, 1, 5, 4, 2, 3),create_time DESC");
        return selectList(entityWrapper);
    }

    /* 根据活动状态和时间排序*/
    public List<Meeting> getMeetingPageByIds(List<String> meetingIds, Page page) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<Meeting>();
        entityWrapper.in("id", meetingIds);
        entityWrapper.orderBy(" FIELD(status, 0, 1, 5, 4, 2, 3),create_time DESC");
        return selectPage(page, entityWrapper).getRecords();
    }


    public List<Meeting> getMeetingListsByUserId(String userId) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<Meeting>();
        entityWrapper.where("user_id={0}", userId);
        entityWrapper.orderBy(" FIELD(status, 0, 1, 5, 4, 2, 3),create_time DESC");
        return selectList(entityWrapper);
    }

    /* 根据用户userId查询用户发起的活动 按时间排序*/
    public List<Meeting> getSelfSendMeetingCreateTimeDesc(String userId, Page page) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<Meeting>();
        entityWrapper.where("user_id={0}", userId);
        entityWrapper.orderBy("create_time",false);
        return selectPage(page, entityWrapper).getRecords();
    }

    /**
     * 获取相同的活动列表
     */
    public Integer getSameMeeting(String user_id, Date startedAt, Date endAt, String id) {
        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<Meeting>();
        meetingWrapper.where("end_at >={0} and started_at <={1} and user_id={2} and status in(0,1,5)", startedAt, endAt, user_id);
        meetingWrapper.notIn("id", id);
        return selectCount(meetingWrapper);
    }

    /**
     * 发起活动
     */
    public boolean create(SendMeetingDto sendMeetingDto, Meeting meeting) {
        meeting.setId(sendMeetingDto.getId());
        meeting.setUserId(sendMeetingDto.getUserId());
        meeting.setTitle(sendMeetingDto.getTitle());
        meeting.setMeetingType(sendMeetingDto.getMeetingType());
        //检测是否有emoji
//        patternEmoji(sendMeetingDto.getMeetingLabel());
        meeting.setMeetingLabel(sendMeetingDto.getMeetingLabel());
        meeting.setStartedAt(new Date((sendMeetingDto.getStartedAt())));
        meeting.setEndAt(new Date(sendMeetingDto.getEndAt()));
        meeting.setRegStopAt(new Date(sendMeetingDto.getRegStopAt()));
        meeting.setObj(sendMeetingDto.getObj());
        meeting.setObjList(sendMeetingDto.getObjList());
        if (sendMeetingDto.getAmount() != null) {
            meeting.setAmount(new Money(sendMeetingDto.getAmount()).getCent());
        }
        meeting.setAddress(sendMeetingDto.getAddress());
        meeting.setOrderIds(sendMeetingDto.getOrderIds());
        meeting.setOrderPrice(new Money(sendMeetingDto.getOrderPrice()).getCent());
        meeting.setLon(sendMeetingDto.getLon());
        meeting.setLat(sendMeetingDto.getLat());
        meeting.setDescription(sendMeetingDto.getDescription());
        meeting.setPosterImagesUrl(sendMeetingDto.getMerchantId());
        meeting.setMeetingImagesUrl((sendMeetingDto.getPic()));
        meeting.setCeil(sendMeetingDto.getCeil());
        meeting.setFloor(sendMeetingDto.getFloor());
        meeting.setFeeNotEnough(sendMeetingDto.getFeeNotEnough());
        meeting.setCreateUserId(sendMeetingDto.getUserId());
        return insertOrUpdate(meeting);
    }

    /**
     * 补充活动信息
     */
    public boolean modify(ModifyMeetingDto modifyMeetingDto) throws Exception {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", modifyMeetingDto.getId()).and("user_id={0}", modifyMeetingDto.getUserId());
        Meeting meeting = selectOne(entityWrapper);
        meeting.setAddress(modifyMeetingDto.getAddress());
        meeting.setOrderIds(modifyMeetingDto.getOrderIds());
        meeting.setOrderPrice(new Money(modifyMeetingDto.getOrderPrice()).getCent());
        meeting.setLon(modifyMeetingDto.getLon());
        meeting.setLat(modifyMeetingDto.getLat());
        meeting.setPosterImagesUrl((modifyMeetingDto.getPic()));
        meeting.setConfirm(true);
        return meeting.insertOrUpdate();
    }

    // 更新meeting.confirm状态
    public boolean updateMeeting(Meeting meeting) {
        Wrapper<Meeting> wrapper = new EntityWrapper<>();
        wrapper.where("id={0}", meeting.getId());
        return update(meeting, wrapper);
    }

    /**
     * 根据状态值更新所传ID的记录
     */
    public boolean updateStatusByIdAndUserId(MeetingStatus status, String meetingId, String userId) {
        Meeting meeting = new Meeting();
        meeting.setStatus(status.status);
        meeting.setStatusDescription(status.description);
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0} and user_id={1}", meetingId, userId);
        return update(meeting, entityWrapper);
    }

    /**
     * 根距ID修改该活动信息
     */
    public boolean editMeeting(String meetingId, Meeting meeting) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", meetingId);
        return update(meeting, entityWrapper);
    }

    /**
     * 根距userId查询发起的活动列表
     *
     * @return
     */
    public List<Meeting> getMeetingListByUserId(String userId) {
        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<Meeting>();
        meetingWrapper.where("user_id={0}", userId);
        return selectList(meetingWrapper);
    }

    /**
     * 根距userId删除发起的活动列表
     *
     * @return
     */
    public boolean deleteMeetingListByUserId(String userId) {
        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<Meeting>();
        meetingWrapper.where("user_id={0}", userId);
        return delete(meetingWrapper);
    }

    /**
     * 得到没完成的活动列表
     */
    public List<Meeting> getUnComplete(String userId) {
        List<Integer> meetingStatusList = new ArrayList<Integer>();
        meetingStatusList.add(MeetingStatus.CODE_GENERATOR.status);
        meetingStatusList.add(MeetingStatus.WAITING.status);
        meetingStatusList.add(MeetingStatus.STOP.status);

        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<Meeting>();
        meetingWrapper.where("user_id={0}", userId);
        meetingWrapper.in("status", meetingStatusList);
        meetingWrapper.gt("end_at", new Date().getTime());
        return selectList(meetingWrapper);
    }

    /**
     * 发起活动的活动时间内不能有未完成的活动
     */
    public List<Meeting> getUnComplete(String userId, long startAt, long endAt, List meetingStatusList, String id) {
        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<Meeting>();
        meetingWrapper.where("user_id={0}", userId);
        if (StringUtils.isNotBlank(id)) {
            meetingWrapper.where("id <> {0}", id);
        }
        meetingWrapper.in("status", meetingStatusList);
        meetingWrapper.ge("end_at", new Date(startAt));
        meetingWrapper.le("started_at", new Date(endAt));
        return selectList(meetingWrapper);
    }

    /**
     * 上个月活动成功的列表
     */
    public List<Meeting> getSuccessMeeting(String userId, long firstDay, long lastDay) {
        EntityWrapper<Meeting> meetingWrapper = new EntityWrapper<>();
        meetingWrapper.where("user_id={0}", userId);
        meetingWrapper.and("status={0}", MeetingStatus.SUCCESS.status);
        meetingWrapper.between("create_time", firstDay, lastDay);
        return selectList(meetingWrapper);
    }

    public List<Meeting> nearHasOneList(String userId, BigDecimal lon, BigDecimal lat, Double length, Page<Meeting> page) {
//		EntityWrapper<Meeting> entityWrapper = DistrictUtil.buildEntityWrapper("geohash",DistrictUtil.getHashAdjcent(lat, lon, length));
        EntityWrapper<Meeting> entityWrapper = null;
        entityWrapper.eq("meetingType", MeetingType.HAS_ONE.type);
        Page<Meeting> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }

    /* **/
    public int getExist(String meetingId, String userId) {
        EntityWrapper<Meeting> wrapper = new EntityWrapper<>();
        wrapper.where("id={0}", meetingId);
        wrapper.like("obj_list", userId);
        return selectCount(wrapper);
    }

    //统计用户发布的活动数量
    public Integer getSendCount(String userId) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        return selectCount(entityWrapper);
    }

    //获取用户发布的成功活动总数
    public Integer getSuccessCount(String userId, Integer status) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0} and status={1}", userId, status);
        return selectCount(entityWrapper);
    }

    //获取用户发布的最新活动信息
    public Meeting getLatestMeeting(String userId) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        entityWrapper.orderBy("create_time", false);
        return selectOne(entityWrapper);
    }

    /* 根据订单表查出活动信息*/
    public Meeting getOderIdMeeting(String orderId) {
        EntityWrapper<Meeting> wrapper = new EntityWrapper<>();
        wrapper.where("order_ids={0}", orderId);
        return selectOne(wrapper);
    }

    public List<Map<String, Object>> queryMeeting(Collection<?> userIds) {
        EntityWrapper<Meeting> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status={0} and deleted=0", MeetingStatus.SUCCESS.status);
        entityWrapper.in("user_id", userIds);
        entityWrapper.setSqlSelect("user_id as userId,GROUP_CONCAT(id) as ids");
        entityWrapper.groupBy("userId");
        return selectMaps(entityWrapper);
    }

    private boolean patternEmoji(String tab) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(tab);
        if (matcher.find()) {
            throw new RuntimeException("标签不支持Emoji标签");
        }
        return false;
    }
}