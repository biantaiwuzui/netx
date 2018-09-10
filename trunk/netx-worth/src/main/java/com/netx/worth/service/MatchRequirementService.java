package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchRequirementDTO;
import com.netx.worth.mapper.MatchRequirementMapper;
import com.netx.worth.model.MatchRequirement;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 比赛要求
 * Created by Yawn on 2018/8/4 0004.
 */
@Service
public class MatchRequirementService extends ServiceImpl<MatchRequirementMapper, MatchRequirement> {
    /**
     * 插入赛事需求
     * @param dto
     * @return
     */
    public boolean addRequirement(MatchRequirementDTO dto) {
        MatchRequirement matchRequirement = new MatchRequirement();
        if (!StringUtils.isEmpty(dto.getId())) {
            matchRequirement.setId(dto.getId());
        }
        matchRequirement.setGroupId(dto.getGroupId());
        matchRequirement.setRequirementName(dto.getRequirementName());
        matchRequirement.setRequirementData(dto.getRequirementData());
        matchRequirement.setRequirementDesignation(dto.getRequirementDesignation());
        matchRequirement.setRequirementData(dto.getRequirementData());
        matchRequirement.setSort(dto.getSort());
        return insertOrUpdate(matchRequirement);
    }

    /**
     * 通过信息获取报名要求
     * @param matchRequirementDTO
     * @return
     */
    public MatchRequirement getMatchRequirement(MatchRequirementDTO matchRequirementDTO){
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("group_id = {0}", matchRequirementDTO.getGroupId())
                .and("requirement_name = {0}", matchRequirementDTO.getRequirementName());
        return selectOne(entityWrapper);
    }
    /**
     * 通过信息获取报名要求
     * @param groupId
     * @return
     */
    public boolean getIsHaveMatchRequirementByGroupId(String groupId){
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("group_id = {0}",groupId);
        Integer counts=selectCount(entityWrapper);
        if(counts>0){
            return true;
        }
        return false;
    }
    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deleteRequirement(String id) {
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }

    /**
     * 删除比赛的要求
     * @param groupId
     * @return
     */
    public boolean deleteRequirementByGroupId(String groupId) {
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("group_id = {0}", groupId);
        return delete(entityWrapper);
    }
    /**
     * 查询比赛要求
     * @param groupId
     * @return
     */
    public List<MatchRequirement> listMatchRequirement(String groupId) {
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("group_id = {0}", groupId).orderBy("sort",true);
        return selectList(entityWrapper);
    }

    /**
     * 更新
     * @param dto
     * @return
     */
    public boolean update(MatchRequirementDTO dto) {
        MatchRequirement matchRequirement = new MatchRequirement();
        matchRequirement.setGroupId(dto.getGroupId());
        matchRequirement.setRequirementName(dto.getRequirementName());
        matchRequirement.setRequirementData(dto.getRequirementData());
        matchRequirement.setRequirementDesignation(dto.getRequirementDesignation());
        EntityWrapper<MatchRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("group_id = {0}", dto.getGroupId())
                .and("requirement_name = {0}", dto.getRequirementName());
        return matchRequirement.update(entityWrapper);
    }
}
