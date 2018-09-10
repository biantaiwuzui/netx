package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchGroupDTO;
import com.netx.worth.mapper.MatchGroupMapper;
import com.netx.worth.model.MatchGroup;
import com.netx.worth.vo.MatchGroupZoneVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛组
 * Created by Yawn on 2018/8/13 0013.
 */
@Service
public class MatchGroupService extends ServiceImpl<MatchGroupMapper, MatchGroup>{

    /**
     * 插入
     * @param dto
     * @return
     */
    public String addMatchGroup(MatchGroupDTO dto) {
        MatchGroup matchGroup = new MatchGroup();
        matchGroup.setId(dto.getId());
        matchGroup.setMatchGroupName(dto.getMatchGroupName());
        matchGroup.setMatchId(dto.getMatchId());
        matchGroup.setSort(dto.getSort());
        matchGroup.setAutoSelect(dto.getAutoSelect());
        matchGroup.setFree(dto.getFree());
        matchGroup.setQuota(dto.getQuota());
        return insert(matchGroup)?matchGroup.getId():null;
    }

    /**
     * 批量更新
     * @param dtos
     * @return
     */
    public boolean updateBatchMatchGroup(List<MatchGroupDTO> dtos){
        List<MatchGroup> matchGroupList=new ArrayList<>();
        for (MatchGroupDTO dto:dtos) {
            MatchGroup matchGroup=new MatchGroup();
            matchGroup.setSort(dto.getSort());
            matchGroup.setId(dto.getId());
            matchGroup.setMatchGroupName(dto.getMatchGroupName());
            matchGroup.setMatchId(dto.getMatchId());
            matchGroup.setAutoSelect(dto.getAutoSelect());
            matchGroup.setFree(dto.getFree());
            matchGroup.setQuota(dto.getQuota());
            matchGroupList.add(matchGroup);
        }
        return updateBatchById(matchGroupList);
    }
    /**
     * 删除一个赛组
     * @param id
     * @return
     */
    public boolean deleteMatchGroup(String id) {
        EntityWrapper<MatchGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }
    /**
     * 删除比赛id
     * @param matchId
     * @return
     */
    public boolean deleteMatchGroupByMatchId(String matchId) {
        EntityWrapper<MatchGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        return delete(entityWrapper);
    }

    /**
     * 根据比赛ID查询列表
     * @param matchId
     * @return
     */
    public List<MatchGroup> listMatchGroupByMatch(String matchId) {
        EntityWrapper<MatchGroup> matchGroupEntityWrapper = new EntityWrapper<>();
        matchGroupEntityWrapper.where("match_id = {0}", matchId).orderBy("sort",true);
        return selectList(matchGroupEntityWrapper);
    }

    /**
     * 通过ids获取对象
     * @param ids
     * @return
     */
    public List<MatchGroup> getMatchGroupListByIds(List<String> ids){
        return selectBatchIds(ids);
    }

    /**
     * 判断是否已经填写
     * @param matchId
     * @return
     */
    public Boolean IsWriteMatchGroup(String matchId) {
        EntityWrapper<MatchGroup> matchGroupEntityWrapper = new EntityWrapper<>();
        matchGroupEntityWrapper.where("match_id = {0}", matchId);
        if(selectCount(matchGroupEntityWrapper)>0){
            return true;
        }
        return false;
    }

    /**
     * 通过赛区查询相关信息
     * @param zoneId
     * @return
     */
    public List<MatchGroupZoneVo> selectMatchGroupByZoneId(String zoneId) {
        return baseMapper.selectMatchGroupByZoneId(zoneId);
    }
    /**
     * 通过比赛id查询相关信息
     * @param matchId
     * @return
     */
    public List<MatchGroupZoneVo> selectMatchGroupByMatchId(String matchId) {
        return baseMapper.selectMatchGroupByMatchId(matchId);
    }
}
