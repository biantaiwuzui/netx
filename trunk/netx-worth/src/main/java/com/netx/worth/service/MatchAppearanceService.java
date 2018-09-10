package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchAppearanceDTO;
import com.netx.common.wz.dto.matchEvent.MatchParticipantDTO;
import com.netx.worth.enums.MatchAppearanceStatus;
import com.netx.worth.mapper.MatchAppearanceMapper;
import com.netx.worth.model.MatchAppearance;
import com.netx.worth.model.MatchParticipant;
import com.netx.worth.vo.MatchAppearanceOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比赛出演顺序操作
 * Created by Yawn on 2018/8/2 0002.
 */
@Service
public class MatchAppearanceService extends ServiceImpl<MatchAppearanceMapper, MatchAppearance> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 增加出场者
     * @param dto
     * @return
     */
    public boolean addAppearance(MatchParticipant dto) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setMatchId(dto.getMatchId());
        //matchAppearance.setProgressId();
        matchAppearance.setZoneId(dto.getZoneId());
        matchAppearance.setGroupId(dto.getGroupId());
//        matchAppearance.setVenueId(dto.get);
        matchAppearance.setPerformerId(dto.getId());
        matchAppearance.setHeadImageUrl(dto.getHeadImagesUrl());
        matchAppearance.setAppearanceOrder(0);
        matchAppearance.setAppearanceStatus(0);
        matchAppearance.setPerformerName(dto.getUserName());
        return insert(matchAppearance);
    }

    /**
     * 增加出场者, 有赛程
     * @param dto
     * @return
     */
    public boolean addAppearance(MatchParticipant dto, String progressId) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setMatchId(dto.getMatchId());
        matchAppearance.setProgressId(progressId);
        matchAppearance.setZoneId(dto.getZoneId());
        matchAppearance.setGroupId(dto.getGroupId());
//        matchAppearance.setVenueId(dto.get);
        matchAppearance.setPerformerId(dto.getId());
        matchAppearance.setHeadImageUrl(dto.getHeadImagesUrl());
        matchAppearance.setAppearanceOrder(0);
        matchAppearance.setAppearanceStatus(0);
        matchAppearance.setPerformerName(dto.getUserName());
        return insert(matchAppearance);
    }
    /**
     * 增加出场者, 有赛程
     * @param dto
     * @return
     */
    public boolean addAppearance(MatchParticipantDTO dto, String progressId) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setMatchId(dto.getMatchId());
        matchAppearance.setProgressId(progressId);
        matchAppearance.setZoneId(dto.getZoneId());
        matchAppearance.setGroupId(dto.getGroupId());
//        matchAppearance.setVenueId(dto.get);
        matchAppearance.setPerformerId(dto.getId());
        matchAppearance.setHeadImageUrl(dto.getHeadImagesUrl());
        matchAppearance.setAppearanceOrder(0);
        matchAppearance.setAppearanceStatus(0);
        matchAppearance.setPerformerName(dto.getUserName());
        return insert(matchAppearance);
    }
    /**
     * 删除特定的表演
     * @param dto
     * @return
     */
    public boolean deleteAppearanceById(MatchAppearanceDTO dto) {
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", dto.getId());
        return delete(entityWrapper);
    }
    /**
     * 删除特定的表演
     * @param dto
     * @return
     */
    public boolean deleteAppearanceByPerformerIds(String[] ids) {
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("performer_id",ids);
        return delete(entityWrapper);
    }
    /**
     * 更新演出次序
     * @param dto
     * @return
     */
    public boolean updateAppearanceOrder(MatchAppearanceDTO dto) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setAppearanceOrder(dto.getAppearanceOrder());
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", dto.getId());
        return update(matchAppearance, entityWrapper);
    }

    /**
     * 更新演出状态
     * @param dto
     * @return
     */
    public boolean updateAppearanceStatus(MatchAppearanceDTO dto) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setAppearanceStatus(dto.getAppearanceStatus());
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", dto.getId());
        return update(matchAppearance, entityWrapper);
    }

    /**
     * 更新表演时间
     * @param dto dto
     * @return
     */
    public boolean updateShowTime(MatchAppearanceDTO dto) {
        MatchAppearance matchAppearance = new MatchAppearance();
        matchAppearance.setPerformanceTime(dto.getPerformanceTime());
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", dto.getId());
        return update(matchAppearance, entityWrapper);
    }

    /**
     * 根据场次查看报名者
     * @param matchId 比赛ID
     * @param zoneId 场次ID
     * @return 列表
     */
    public List<MatchAppearanceOrderVo> listMatchAppearance(String matchId, String zoneId) {
        Map<String, String>map = new HashMap<>();
        map.put("matchId", matchId);
        map.put("zoneId", zoneId);
        return baseMapper.listMatchAppearanceVo(map);
    }

    /**
     * 根据赛组返回出场次序
     * @param progressId 比赛ID
     * @param groupId 赛组ID
     * @return 列表
     */
    public List<MatchAppearanceOrderVo>listMatchAppearanceByGroup(String progressId, String zoneId, String groupId) {
        Map<String, String>map = new HashMap<>();
        map.put("progressId", progressId);
        map.put("groupId", groupId);
        map.put("zoneId", zoneId);
        return baseMapper.listMatchAppearanceVoByProgress(map);
    }

    /**
     * 是否有该出场者
     * @param progressId
     * @param participantId
     * @param groupId
     * @return
     */
    public boolean hadAppearance(String progressId, String participantId, String groupId) {
        EntityWrapper<MatchAppearance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("progress_id = {0}", progressId)
                .and("performer_id = {0}", participantId)
                .and("group_id = {0}", groupId);
        MatchAppearance m = selectOne(entityWrapper);
        if (m == null)
            return false;
        return true;
    }

}
