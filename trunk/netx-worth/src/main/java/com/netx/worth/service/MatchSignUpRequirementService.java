package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchSignUpReqDTO;
import com.netx.worth.mapper.MatchSignUpRequirementMapper;
import com.netx.worth.model.MatchSignUpRequirement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 比赛要求
 * Created by Yawn on 2018/8/13 0013.
 */
@Service
public class MatchSignUpRequirementService extends ServiceImpl<MatchSignUpRequirementMapper, MatchSignUpRequirement>{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    /**
     * 添加或更新
     * @param dto
     * @return
     */
    public boolean addMatchSignUpReq(MatchSignUpReqDTO dto) {
        MatchSignUpRequirement matchSignUpRequirement = new MatchSignUpRequirement();
        if (StringUtils.isNoneBlank(dto.getId()))
            matchSignUpRequirement.setId(dto.getId());
        matchSignUpRequirement.setMatchId(dto.getMatchId());
        matchSignUpRequirement.setGroupName(dto.getGroupName());
        matchSignUpRequirement.setVenueId(dto.getVenueId());
        matchSignUpRequirement.setBeginTime(dto.getBeginTime());
        matchSignUpRequirement.setEndTime(dto.getEndTime());
        matchSignUpRequirement.setDecription(dto.getDecription());
        matchSignUpRequirement.setQuota(dto.getQuota());
        matchSignUpRequirement.setFree(dto.getFree());
        matchSignUpRequirement.setAutoSelect(dto.getAutoSelect());
        return insertOrUpdate(matchSignUpRequirement);
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public boolean deleteMatchSignReq(String id) {
        EntityWrapper<MatchSignUpRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }

    /**
     * 列出该比赛的要求
     * @return
     */
    public List<MatchSignUpRequirement> listMatchSignUpReq(String matchId) {
        EntityWrapper<MatchSignUpRequirement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        return selectList(entityWrapper);
    }
}
