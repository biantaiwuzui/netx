package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchFlowDTO;
import com.netx.common.wz.dto.matchEvent.MatchVenueDTO;
import com.netx.worth.mapper.MatchVenueMapper;
import com.netx.worth.model.MatchVenue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 比赛分会场服务
 * Created by Yawn on 2018/8/1 0001.
 */
@Service
public class MatchVenueService extends ServiceImpl<MatchVenueMapper, MatchVenue> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 增加分会场
     * @param dto
     * @return
     */
    public String addVenue(MatchVenueDTO dto) {
        MatchVenue matchVenue = new MatchVenue();
        if (!StringUtils.isEmpty(dto.getId())) {
            matchVenue.setId(dto.getId());
        }
        matchVenue.setZoneId(dto.getZoneId());
        matchVenue.setTitle(dto.getTitle());
        matchVenue.setKind(dto.getKind());
        matchVenue.setBeginTime(dto.getBeginTime());
        matchVenue.setEndTime(dto.getEndTime());
        matchVenue.setAddress(dto.getAddress());
        matchVenue.setSite(dto.getSite());
        matchVenue.setSiteImageUrl(dto.getSiteImageUrl());
        matchVenue.setSort(dto.getSort());
        matchVenue.setGroupIds(dto.getGroupIds());
        return insertOrUpdate(matchVenue)? matchVenue.getId(): null;
    }

    /**
     * 更新比赛会场信息
     * @param matchFlowDTOList
     * @return
     */
    public boolean updateVenueByIds(List<MatchFlowDTO> matchFlowDTOList) {
        List<MatchVenue> matchVenueList=new ArrayList<>();
        for (int i = 0; i < matchFlowDTOList.size(); i++) {
            MatchVenue matchVenue = new MatchVenue();
            matchVenue.setFlowPath(matchFlowDTOList.get(i).getFlowPath());
            matchVenue.setId(matchFlowDTOList.get(i).getVenueId());
            matchVenue.setFlowPathSort(matchFlowDTOList.get(i).getFlowPathSort());
            matchVenueList.add(matchVenue);
        }
        return updateBatchById(matchVenueList);
    }

    /**
     * 删除分会场
     * @param id
     * @return
     */
    public boolean deleteVenue(String id) {
        return deleteById(id);
    }
    /**
     * 删除所有赛区的场次信息
     * @param zoneId
     * @return
     */
    public boolean deleteVenueByMatchZoneId(String zoneId) {
        EntityWrapper<MatchVenue> matchVenueEntityWrapper = new EntityWrapper<>();
        matchVenueEntityWrapper.where("zone_id = {0}", zoneId);
        return delete(matchVenueEntityWrapper);
    }
    /**
     * 删除所有赛区的场次信息
     * @param zoneId
     * @return
     */
    public boolean deleteVenueByMatchZoneId(String[] zoneId) {
        EntityWrapper<MatchVenue> matchVenueEntityWrapper = new EntityWrapper<>();
        matchVenueEntityWrapper.in("zone_id", zoneId);
        return delete(matchVenueEntityWrapper);
    }
    /**
     * 获取当前赛区的场次
     * @param zoneIds
     * @return
     */
    public List<MatchVenue> getMatchVenueListByMatchZoneId(String[] zoneIds) {
        EntityWrapper<MatchVenue> matchVenueEntityWrapper = new EntityWrapper<>();
        matchVenueEntityWrapper.in("zone_id",zoneIds ).orderBy("flow_path,flow_path_sort,sort",true);
        matchVenueEntityWrapper.getSqlSegment();
        return selectList(matchVenueEntityWrapper);
    }
    /**
     * 获取当前赛区的场次
     * @param zoneId
     * @return
     */
    public List<MatchVenue> getMatchAllVenueListByMatchZoneIds(String zoneId) {
        EntityWrapper<MatchVenue> matchVenueEntityWrapper = new EntityWrapper<>();
        matchVenueEntityWrapper.where("zone_id = {0}", zoneId).orderBy("sort",true);
        return selectList(matchVenueEntityWrapper);
    }
    /**
     * 通过id批量的查询
     * @param ids
     * @return
     */
    public List<MatchVenue> getMatchVenueListByIds(List<String> ids){
        return selectBatchIds(ids);
    }
    /**
     * 根据类型和赛程id找出相对应的会场信息
     * @param matchVenueDTO
     * @return
     */
    public MatchVenue getMatchVenue(MatchVenueDTO matchVenueDTO){
        EntityWrapper<MatchVenue> matchVenueEntityWrapper=new EntityWrapper<>();
        matchVenueEntityWrapper.where("zone_id = {0}", matchVenueDTO.getZoneId())
                .and("kind = {0}", matchVenueDTO.getKind())
                .and("title = {0}", matchVenueDTO.getTitle());
        return selectOne(matchVenueEntityWrapper);
    }
    public boolean isHaveMatchVenueByMatchZoneId(String matchzoneId){
        EntityWrapper<MatchVenue> matchVenueEntityWrapper=new EntityWrapper<>();
        matchVenueEntityWrapper.where("zone_id = {0}",matchzoneId);
        Integer count=selectCount(matchVenueEntityWrapper);
        if(count>0){
            return true;
        }
        return false;
    }

    /**
     * 通过主键进行更新
     * @param
     * @return
     */
    /*public boolean updateMatchVenueByMatchVenueId(MatchVenueDTO matchVenueDTO){
        MatchVenue matchVenue = new MatchVenue();
        matchVenue.setTitle(dto.getTitle());
        matchVenue.setBeginTime(dto.getBeginTime());
        matchVenue.setEndTime(dto.getEndTime());
        matchVenue.setAddress(dto.getAddress());
        matchVenue.setSite(dto.getSite());
        matchVenue.setSiteImageUrl(dto.getSiteImageUrl());
        matchVenue.setKind(matchVenueDTO.getKind());
        matchVenue.setId(matchVenueDTO);
        return updateById(matchVenue);
    }*/

    public Date getLastEndTimeBtMatchId(String matchId){
        return baseMapper.getLastEndTimeBtMatchId(matchId);
    }
}
