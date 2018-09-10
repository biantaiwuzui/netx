package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchGroupAndZoneDto;
import com.netx.worth.mapper.MatchGroupAndZoneMapper;
import com.netx.worth.model.MatchGroupAndZone;
import com.netx.worth.model.MatchZone;
import com.netx.worth.vo.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 老肥猪
 * @since 2018-8-15
 */
@Service
public class MatchGroupAndZoneService extends ServiceImpl<MatchGroupAndZoneMapper,MatchGroupAndZone> {
    /**
     * 添加
     * @param matchGroupAndZoneDto
     * @return
     */
    public boolean addOrUpdateMatchGroupAndZone(MatchGroupAndZoneDto matchGroupAndZoneDto){
        MatchGroupAndZone matchGroupAndZone=new MatchGroupAndZone();
        matchGroupAndZone.setMatchGroupId(matchGroupAndZoneDto.getMatchGroupId());
        matchGroupAndZone.setMatchZoneId(matchGroupAndZoneDto.getMatchZoneId());
        matchGroupAndZone.setEndTime(matchGroupAndZoneDto.getEndTime());
        matchGroupAndZone.setStartTime(matchGroupAndZoneDto.getStartTime());
        matchGroupAndZone.setDefault(matchGroupAndZoneDto.isDefault());
        matchGroupAndZone.setZoneTime(matchGroupAndZoneDto.isZoneTime());
        matchGroupAndZone.setId(matchGroupAndZoneDto.getId());
        return insertOrUpdate(matchGroupAndZone);
    }

    public MatchGroupAndZone getZoneDefaultApplyTime(String zoneId){
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.where("is_default",1).and("is_zone_time",1);
        return selectOne(matchGroupAndZoneEntityWrapper);
    }
    /**
     * 获取所有场次
     * @param matchId
     * @return
     */
    public List<MatchGroupAndZoneVo> getAllMacthGroupAndZoneByMatchId(String matchId){
        return baseMapper.getAllMacthGroupAndZoneByMatchId(matchId);
    }
    /**
     * 获取所有场次
     * @param zoneId
     * @return
     */
    public List<MatchGroupAndTimeVo> getMatchGroupByZoneId(String zoneId){
        return baseMapper.getMatchGroupByZoneId(zoneId);
    }

    /**
     * 通过赛区获取报名信息
     * @param zoneId
     * @return
     */
    public List<MatchApplyInfoVo> getApplyInfoByZoneId(String zoneId){
        return baseMapper.getApplyInfoByZoneId(zoneId);
    }

    /**
     * 查询
     * @param groupId
     * @param zoneId
     * @return
     */
    public MatchGroupAndZone getMatchGroupAndZone(String groupId,String zoneId){
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.where("match_zone_id={0}",zoneId).and("match_group_id={0}",groupId);
        return selectOne(matchGroupAndZoneEntityWrapper);
    }
    /**
     * 根据赛区Ids删除购票时间表
     * @param zoneIds
     * @return
     */
    public boolean deleteMatchGroupAndZoneByZoneId(String[] zoneIds){
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.in("match_zone_id",zoneIds);
        return delete(matchGroupAndZoneEntityWrapper);
    }

    /**
     * 根据赛区Id删除购票时间表
     */
    public boolean deleteMatchGroupAndZoneByZoneId(String zoneId){
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.where("match_zone_id={0}",zoneId);
        return delete(matchGroupAndZoneEntityWrapper);
    }
    /**
     * 根据赛组Id删除购票时间表
     */
    public boolean deleteMatchGroupAndZoneByGroupId(String groupId){
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.where("match_group_id={0}",groupId);
        return delete(matchGroupAndZoneEntityWrapper);
    }

    /**
     * 判断是否已经填写统一时间
     * @param zoneIds
     * @return
     */
    public Boolean IsWriteMatchApply(String[] zoneIds) {
        EntityWrapper<MatchGroupAndZone> matchGroupAndZoneEntityWrapper=new EntityWrapper<>();
        matchGroupAndZoneEntityWrapper.in("match_zone_id",zoneIds);
        if(selectCount(matchGroupAndZoneEntityWrapper)>0){
            return true;
        }
        return false;
    }
    public Date getEndApplyTimeByMatchId(String matchId) {
        return baseMapper.getEndApplyTimeByMatchId(matchId);
    }
}
