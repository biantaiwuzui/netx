package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchChildInfoDTO;
import com.netx.utils.format.DateFormatUtils;
import com.netx.worth.mapper.MatchChildInfoMapper;
import com.netx.worth.model.MatchChildInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 比赛孩子信息
 * Created by Yawn on 2018/8/2 0002.
 */
@Service
public class MatchChildInfoService extends ServiceImpl<MatchChildInfoMapper, MatchChildInfo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 添加孩子信息
     * @param dto
     * @return
     */
    public String addChildrenInfo(MatchChildInfoDTO dto) {
        MatchChildInfo matchChildInfo = new MatchChildInfo();
        if (StringUtils.isNotEmpty(dto.getId()))
            matchChildInfo.setId(dto.getId());
        matchChildInfo.setHeadImage(dto.getHeadImage());
        matchChildInfo.setOtherRequirement(dto.getOtherRequirement());
        matchChildInfo.setImagesUrl(dto.getImagesUrl());
        matchChildInfo.setIntroduction(dto.getIntroduction());
        matchChildInfo.setName(dto.getName());
        matchChildInfo.setParticipantId(dto.getParticipantId());
        matchChildInfo.setSex(dto.getSex());
        matchChildInfo.setAge(getAge(dto.getBirthday()));
        matchChildInfo.setMobile(dto.getMobile());
        matchChildInfo.setUserId(dto.getUserId());
        return insertOrUpdate(matchChildInfo)?matchChildInfo.getId():null;
    }

    /**
     * 用户团队添加孩子信息，文聪用的
     * @param dto
     * @return
     */
    public String addChildrenInfoForUser(MatchChildInfoDTO dto) {
        MatchChildInfo matchChildInfo = new MatchChildInfo();
        matchChildInfo.setHeadImage(dto.getHeadImage());
        matchChildInfo.setOtherRequirement(dto.getOtherRequirement());
        matchChildInfo.setImagesUrl(dto.getImagesUrl());
        matchChildInfo.setIntroduction(dto.getIntroduction());
        matchChildInfo.setName(dto.getName());
        matchChildInfo.setParticipantId(dto.getParticipantId());
        matchChildInfo.setSex(dto.getSex());
        matchChildInfo.setAge(getAge(dto.getBirthday() == null ? new Date() : dto.getBirthday()));
        matchChildInfo.setMobile(dto.getMobile());
        matchChildInfo.setUserId(dto.getUserId());
        return insert(matchChildInfo)?matchChildInfo.getId():null;
    }

    /**
     * 日期转换成年龄
     * @param birthDay
     * @return 年龄
     */
    private static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new RuntimeException("该日期超过当前日期");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
    /**
     * 更新某个孩子信息
     * @param dto
     * @return
     */
    public boolean updateChildInfoByDto(MatchChildInfoDTO dto) {
        MatchChildInfo matchChildInfo = new MatchChildInfo();
        matchChildInfo.setImagesUrl(dto.getImagesUrl());
        matchChildInfo.setIntroduction(dto.getIntroduction());
        matchChildInfo.setName(dto.getName());
        matchChildInfo.setParticipantId(dto.getParticipantId());
        matchChildInfo.setSex(dto.getSex());
        EntityWrapper<MatchChildInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("name = {0}", dto.getName())
                .and("participant_id = {0}", dto.getParticipantId());
        return update(matchChildInfo, entityWrapper);
    }

    /**
     * 更新某个孩子信息
     * @param dto
     * @return
     */
    public boolean updateChildInfoById(MatchChildInfoDTO dto) {
        MatchChildInfo matchChildInfo = new MatchChildInfo();
        matchChildInfo.setImagesUrl(dto.getImagesUrl());
        matchChildInfo.setIntroduction(dto.getIntroduction());
        matchChildInfo.setName(dto.getName());
        matchChildInfo.setParticipantId(dto.getParticipantId());
        matchChildInfo.setSex(dto.getSex());
        return updateById(matchChildInfo);
    }

    /**
     * 删除某个孩子信息
     * @param childId
     * @return
     */
    public boolean deleteChildrenInfoByChildId(String childId){
        return deleteById(childId);
    }

    /**
     * 查询监护人的未成年人团队的List
     */
    public List<MatchChildInfo> listChildInfoByParticipantId(String participantId, Page<MatchChildInfo> page) {
        EntityWrapper<MatchChildInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("participant_id={0}", participantId).isNull("mobile");
        if(page==null) {
            return selectList(entityWrapper);
        }
        return selectPage(page,entityWrapper).getRecords();
    }
    /**
     * 查询有手机号的团队成员
     */
    public List<MatchChildInfo> listChildInfoHaveMobileByParticipantId(String participantId) {
        EntityWrapper<MatchChildInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("participant_id={0}", participantId).isNotNull("mobile");
        return selectList(entityWrapper);
    }
    /**
     * 获取参加比赛的未成年成员信息
     * @param macthChildId
     * @return
     */
    public MatchChildInfo getMacthChildInfo(String macthChildId){
        return selectById(macthChildId);
    }
    /**
     * 查询比赛的未成年人团队的List
     */
    public List<MatchChildInfo> ListChildInfo(String matchId) {
        EntityWrapper<MatchChildInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        return selectList(entityWrapper);
    }
}
