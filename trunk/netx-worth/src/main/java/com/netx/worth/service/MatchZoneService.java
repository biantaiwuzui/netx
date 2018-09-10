package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchZoneDto;
import com.netx.worth.mapper.MatchZoneMapper;
import com.netx.worth.model.MatchZone;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赛区的Service
 * @author 老肥猪
 * @since 2018-08-15
 */
@Service
public class MatchZoneService extends ServiceImpl<MatchZoneMapper,MatchZone> {
    /**
     * 添加赛区
     * @param matchZoneDto
     * @return
     */
    public String addMatchZone(MatchZoneDto matchZoneDto){
        MatchZone matchZone=new MatchZone();
        matchZone.setId(matchZoneDto.getId());
        matchZone.setMatchId(matchZoneDto.getMatchId());
        matchZone.setSort(matchZoneDto.getSort());
        matchZone.setZoneAdress(matchZoneDto.getZoneAdress());
        matchZone.setZoneName(matchZoneDto.getZoneName());
        matchZone.setZoneSite(matchZoneDto.getZoneSite());
        return insert(matchZone)?matchZone.getId():null;
    }
    /**
     * 更新赛区
     * @param matchZoneDtoList
     * @return
     */
    public boolean updateMatchZone(List<MatchZoneDto> matchZoneDtoList){
        List<MatchZone> matchZoneList=new ArrayList<>();
        for(MatchZoneDto dto:matchZoneDtoList){
            MatchZone matchZone=new MatchZone();
            matchZone.setId(dto.getId());
            matchZone.setMatchId(dto.getMatchId());
            matchZone.setSort(dto.getSort());
            matchZone.setZoneAdress(dto.getZoneAdress());
            matchZone.setZoneName(dto.getZoneName());
            matchZone.setZoneSite(dto.getZoneSite());
            matchZoneList.add(matchZone);
        }
        return updateBatchById(matchZoneList);
    }
    /**
     * 通过ids删除赛区
     * @param matchZoneIdList
     * @return
     */
    public boolean deleteBatchMatchZone(List<String> matchZoneIdList){
        return deleteBatchIds(matchZoneIdList);
    }

    /**
     * 通过id删除赛区
     * @param matchZoneIdList
     * @return
     */
    public boolean deleteMatchZone(String matchZoneIdList){
        return deleteById(matchZoneIdList);
    }
    /**
     * 通过matchid删除赛区
     * @param matchId
     * @return
     */
    public boolean deleteMatchZoneByMatchId(String matchId){
        EntityWrapper<MatchZone> matchZoneEntityWrapper=new EntityWrapper<>();
        matchZoneEntityWrapper.where("match_id={0}",matchId);
        return delete(matchZoneEntityWrapper);
    }
    /**
     * 获得赛区列表
     * @param matchId
     * @return
     */
    public List<MatchZone> getMatchZoneListByMatchId(String matchId){
        EntityWrapper<MatchZone> matchZoneEntityWrapper=new EntityWrapper<>();
        matchZoneEntityWrapper.where("match_id={0}",matchId).orderBy("sort",true);
        return selectList(matchZoneEntityWrapper);
    }
    /**
     * 获得赛区列表
     * @param matchId
     * @return
     */
    public List<MatchZone> getZoneIdListByMatchId(String matchId){
        EntityWrapper<MatchZone> matchZoneEntityWrapper=new EntityWrapper<>();
        matchZoneEntityWrapper.setSqlSelect("id").where("match_id={0}",matchId);
        return selectList(matchZoneEntityWrapper);
    }

    /**
     * 判断是否已经填写
     * @param matchId
     * @return
     */
    public Boolean IsWriteMatchZone(String matchId) {
        EntityWrapper<MatchZone> matchZoneEntityWrapper=new EntityWrapper<>();
        matchZoneEntityWrapper.where("match_id={0}",matchId);
        if(selectCount(matchZoneEntityWrapper)>0){
            return true;
        }
        return false;
    }
}
