package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchVoteDTO;
import com.netx.worth.mapper.MatchVoteMapper;
import com.netx.worth.model.MatchVote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投票模块
 * Created by Yawn on 2018/8/2 0002.
 */
@Service
public class MatchVoteService extends ServiceImpl<MatchVoteMapper, MatchVote> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 增加投票队伍
     * @param dto
     * @return
     */
    public boolean addMatchVote(MatchVoteDTO dto) {
        MatchVote matchVote = new MatchVote();
        matchVote.setMatchId(dto.getMatchId());
        matchVote.setProjectId(dto.getProjectId());
        matchVote.setProjectIntroduct(dto.getProjectIntroduct());
        matchVote.setProjectImagesUrl(dto.getProjectImagesUrl());
        matchVote.setProjectVote(dto.getProjectVote());
        return insert(matchVote);
    }

    /**
     * 删除指定投票队伍
     * @param dto
     * @return
     */
    public boolean deleteMatchVote(MatchVoteDTO dto) {
        EntityWrapper<MatchVote> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("project_id = {0}", dto.getProjectId());
        return delete(entityWrapper);
    }

    /**
     * 通过id删除
     * @param matchVoteId
     * @return
     */
    public boolean deleteMatchVoteById(String matchVoteId){
        return deleteById(matchVoteId);
    }

    /**
     * 更新投票信息
     * @param dto
     * @return
     */
    public boolean updateMatchVote(MatchVoteDTO dto) {
        MatchVote matchVote = new MatchVote();
        matchVote.setMatchId(dto.getMatchId());
        matchVote.setProjectId(dto.getProjectId());
        matchVote.setProjectIntroduct(dto.getProjectIntroduct());
        matchVote.setProjectImagesUrl(dto.getProjectImagesUrl());
        matchVote.setProjectVote(dto.getProjectVote());

        EntityWrapper<MatchVote> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("project_id = {0}", dto.getProjectId());
        return update(matchVote, entityWrapper);
    }

    /**
     * 查看投票队伍
     * @param matchId
     * @return
     */
    public List<MatchVote> listMatchVote(String matchId) {
        EntityWrapper<MatchVote> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        return selectList(entityWrapper);
    }

    /**
     * 投票动作
     * @param dto
     * @return
     */
    public boolean Vote(MatchVoteDTO dto) {
        MatchVote matchVote = new MatchVote();
        long votes = dto.getProjectVote();
        votes += 1;
        matchVote.setProjectVote(votes);
        EntityWrapper<MatchVote> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("project_id = {0}", dto.getProjectId());
        return update(matchVote, entityWrapper);
    }

}
