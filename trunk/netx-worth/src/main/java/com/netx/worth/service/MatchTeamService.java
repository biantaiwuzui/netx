package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchTeamDTO;
import com.netx.worth.mapper.MatchTeamMapper;
import com.netx.worth.model.MatchTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 比赛团队管理
 * Created by Yawn on 2018/8/1
 */
@Service
public class MatchTeamService extends ServiceImpl<MatchTeamMapper, MatchTeam> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 创建一个团队
     * @param dto
     * @return
     */
    public boolean addTeam(MatchTeamDTO dto) {
        MatchTeam matchTeam = new MatchTeam();
        matchTeam.setTeamImageUrl(dto.getTeamImageUrl());
        matchTeam.setTeamIntroduction(dto.getTeamIntroduction());
        matchTeam.setTeamLeader(dto.getTeamLeader());
        matchTeam.setTeamName(dto.getTeamName());
        matchTeam.setTeamSlogan(dto.getTeamSlogan());
        matchTeam.setMatchId(dto.getMatchId());
        return insert(matchTeam);
    }

    /**
     * 删除指定团队
     * @param matchId
     * @param teamName
     * @return
     */
    public boolean deleteTeam(String matchId, String teamName) {
        EntityWrapper<MatchTeam> matchTeamEntityWrapper = new EntityWrapper<>();
        matchTeamEntityWrapper.where("match_id = {0}", matchId)
                .and("team_name = {0}", teamName);
        return delete(matchTeamEntityWrapper);
    }

    /**
     * 更新团队
     * @param dto
     * @return
     */
    public boolean updateTeam(MatchTeamDTO dto) {
        MatchTeam matchTeam = new MatchTeam();
        matchTeam.setTeamImageUrl(dto.getTeamImageUrl());
        matchTeam.setTeamIntroduction(dto.getTeamIntroduction());
        matchTeam.setTeamLeader(dto.getTeamLeader());
        matchTeam.setTeamName(dto.getTeamName());
        matchTeam.setTeamSlogan(dto.getTeamSlogan());

        EntityWrapper<MatchTeam> matchTeamEntityWrapper = new EntityWrapper<>();
        matchTeamEntityWrapper.where("match_id = {0}", dto.getMatchId());

        return update(matchTeam, matchTeamEntityWrapper);
    }

    /**
     * 根据比赛查询团队
     * @param matchId
     * @return
     */
    public List<MatchTeam> listMatchTeams(String matchId) {
        EntityWrapper<MatchTeam> matchTeamEntityWrapper = new EntityWrapper<>();
        matchTeamEntityWrapper.where("match_id = {0}", matchId);
        return selectList(matchTeamEntityWrapper);
    }
}
