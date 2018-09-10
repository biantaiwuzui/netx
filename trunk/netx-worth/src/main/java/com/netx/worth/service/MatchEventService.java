package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.matchEvent.SendMatchEventDTO;
import com.netx.worth.enums.MatchStatusCode;
import com.netx.worth.mapper.MatchEventMapper;
import com.netx.worth.model.MatchEvent;
import com.netx.worth.vo.MatchAllApplyInfoVO;
import com.netx.worth.vo.MatchEventNewest;
import com.netx.worth.vo.MatchEventSimpleVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 *  赛事管理
 *  Created by Yawn on 2018/8/1
 */
@Service
public class MatchEventService extends ServiceImpl<MatchEventMapper, MatchEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 创建比赛
     * @param matchEventDto
     * @return
     */

    public String insertOrUpdateMatch(SendMatchEventDTO matchEventDto) {
        MatchEvent matchEvent = new MatchEvent();
        if (StringUtils.isNotEmpty(matchEventDto.getId()))
            matchEvent.setId(matchEventDto.getId());
        matchEvent.setInitiatorId(matchEventDto.getInitiatorId());
        matchEvent.setTitle(matchEventDto.getTitle());
        matchEvent.setSubTitle(matchEventDto.getSubTitle());
        matchEvent.setMatchRule(matchEventDto.getMatchRule());
        matchEvent.setGrading(matchEventDto.getGrading());
        matchEvent.setMatchIntroduction(matchEventDto.getMatchIntroduction());
        matchEvent.setMatchImageUrl(matchEventDto.getMatchImageUrl());
        matchEvent.setMatchStatus(matchEventDto.getMatchStatus());
        matchEvent.setApproved(false);
        matchEvent.setMatchKind(matchEventDto.getMatchKind());
        Date now=new Date();
        matchEvent.setUpdateTime(now);
        matchEvent.setLon(matchEventDto.getLon());
        matchEvent.setLat(matchEventDto.getLat());
        return insertOrUpdate(matchEvent)?matchEvent.getId():null;
    }

    /**
     * 更新比赛状态
     * @return
     */
    @Transactional
    public boolean updateMatchStatus(String id, MatchStatusCode matchStatusCode) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        MatchEvent matchEvent = selectById(id);
        matchEvent.setMatchStatus(matchStatusCode.status);
        Date now=new Date();
        matchEvent.setPassTime(now);
        entityWrapper.where("id = {0}", id);
        return update(matchEvent, entityWrapper);
    }

    /**
     * 获取所有发布的比赛
     * @return
     */
    public List<MatchEvent> matchEventList() {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_approved = 1");
        return selectList(entityWrapper);
    }

    public Page<MatchEvent> matchEventList(Page<MatchEvent> page) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_approved = 1");
        return selectPage(page,entityWrapper);
    }

    /**
     * 查看两个比赛
     * @return
     */
    public Page<MatchEvent> matchTwoEventList() {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_approved = 1");
        Page<MatchEvent> page = new Page<>(1, 2);
        return selectPage(page, entityWrapper);
    }

    /**
     * 根据比赛ID删除比赛
     * @param id
     * @return
     */
    public boolean deleteMatchById(String id) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return deleteById(id);
    }

    /**
     * 比赛通过审核
     * @param id
     * @return
     */
    @Transactional
    public boolean approveMatch(String id) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        MatchEvent matchEvent = selectById(id);
        matchEvent.setApproved(true);
        entityWrapper.where("id = {0}", id)
                .and("is_approved = 0");
        return update(matchEvent, entityWrapper);
    }

    /**
     * 根据比赛获取比赛的信息
     * @param Match_id
     * @return
     */
    public MatchEvent getMatchEventByMatchId(String Match_id){
        return selectById(Match_id);
    }

    /**
     * 获取标题
     * @param matchId
     * @return
     */
    public String getMatchTitleByMatchId(String matchId) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect("title")
                .where("id = {0}", matchId);
        return (String) selectObj(entityWrapper);
    }

    public MatchEvent getMatchEventByDTO(SendMatchEventDTO dto) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("initiator_id = {0}", dto.getInitiatorId())
                .and("title = {0}", dto.getTitle());
        return selectOne(entityWrapper);
    }

    /**
     * 获取赛事昵称
     * @param matchId
     * @return
     */
    public String getMatchName(String matchId) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", matchId)
                .setSqlSelect("title");
        MatchEvent matchEvent = selectOne(entityWrapper);
        if (matchEvent != null && StringUtils.isNoneBlank(matchEvent.getTitle())) {
            return matchEvent.getTitle();
        }
        return "";
    }

    /**
     * 获得该比赛的所有报名信息
     * @param matchId
     * @return
     */
    public List<MatchAllApplyInfoVO> getMatchAllZoneApplyInfo(String matchId) {
        return  baseMapper.getMatchAllZoneApplyInfo(matchId);
    }
    /**
     * 获得该比赛的所有报名信息
     * @param matchId
     * @return
     */
    public List<MatchAllApplyInfoVO> getMatchFirstZoneApplyInfo(String matchId) {
        return  baseMapper.getMatchFirstZoneApplyInfo(matchId);
    }

    /**
     *查看自己创建的比赛
     * @param userId
     * @return
     */
    public List<MatchEventSimpleVo> selectSimpleMatchEventList(String userId,Integer pageCount,Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("page_count",pageCount);
        map.put("page_size",pageSize);
        return baseMapper.selectSimpleMatchEventList(map);
    }

    /**
     * 查看用户所有创建的比赛
     * @param userId
     * @return
     */
    public List<MatchEventSimpleVo> selectAllMyMatchEvent(String userId,Integer pageCount,Integer pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("page_count",pageCount);
        map.put("page_size",pageSize);
        return baseMapper.selectAllMySimpleMatchEventList(map);
    }
    /**
     * 查看报名了的比赛
     * @param userId
     * @return
     */
    public List<MatchEventSimpleVo> selectParticipantMatch(String userId) {
        return baseMapper.selectParticipantMatch(userId);
    }

    /**
     * 获得比赛的详情
     *
     * @param matchId
     * @return
     */
    public List<MatchEventSimpleVo> selectActiveMatchEventById(String matchId) {
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        return baseMapper.selectAllMySimpleMatchEventList(map);
    }
    /**
     * 选择已通过的比赛
     * @param dto
     * @return
     */
    public List<MatchEventSimpleVo> selectApprovedMatchEvent(CommonPageDto dto) {
        int start = (dto.getCurrentPage() - 1) * dto.getSize();
        int  size = dto.getSize();
        return baseMapper.selectApprovedMatchEvent(start, size);
    }

    public MatchEventSimpleVo selectOneMatchEventIsApproved(String userId,Boolean isApproved) {
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("is_approved",isApproved);
        return baseMapper.selectOneMatchEvent(map);
    }

    /**
     * 获得最新的一次比赛信息
     * @param userId
     * @return
     */
    public MatchEventNewest getNewestMatchByUserId(String userId) {
        return baseMapper.getNewestMatchByUserId(userId);
    }

    /**
     * 获得自己创建的比赛
     * @param userId
     * @return
     */
    public List<MatchEvent> getMatchByUserId(String userId) {
        EntityWrapper<MatchEvent> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("initiator_id = {0}", userId);
        return  selectList(entityWrapper);
    }
}
