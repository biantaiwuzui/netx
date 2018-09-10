package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchRatersDTO;
import com.netx.worth.mapper.MatchRatersMapper;
import com.netx.worth.model.MatchParticipant;
import com.netx.worth.model.MatchRaters;
import com.netx.worth.vo.MatchParticipantScoreVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评委评分
 * Created by Yawn on 2018/8/2 0002.
 */
@Service
public class MatchRatesService extends ServiceImpl<MatchRatersMapper, MatchRaters> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 增加比赛评分
     * @param dto
     * @return
     */
    public boolean addMatchRater(MatchRatersDTO dto, String userId) {
        MatchRaters matchRaters = new MatchRaters();
        matchRaters.setRatersId(userId);
        matchRaters.setRatersName(dto.getRatersName());
        matchRaters.setProgressId(dto.getProgressId());
        matchRaters.setParticipantId(dto.getParticipantId());
        matchRaters.setScore(dto.getScore());
        return insert(matchRaters);
    }

    public boolean isHadRater(MatchRatersDTO dto, String userId) {
        EntityWrapper<MatchRaters> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("raters_id = {0}", userId)
                .and("progress_id={0}", dto.getProgressId())
                .and("participant_id={0}",dto.getParticipantId());
        MatchRaters m = selectOne(entityWrapper);
        if(m != null) {
            return true;
        }
        return false;
    }

    /**
     * 删除一个比分
     * @param id
     * @return
     */
    public boolean deleteMatchRaters(String id) {
        EntityWrapper<MatchRaters> entityWrapper = new EntityWrapper();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }

    /**
     * 返回比赛每个项目的所有得分
     * @param progressId 赛程
     * @param participantId 参赛者
     * @return
     */
    public List<MatchRaters> listScore(String progressId, String participantId) {
        EntityWrapper<MatchRaters> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("score")
                .where("progress_id = {0}", progressId)
                .and("participant_id = {0}", participantId);
        return selectList(entityWrapper);
    }

    /**
     * 返回平均分
     * @param matchId
     * @param projectId
     * @return
     */
    public int selectavgScore(String matchId, String projectId) {
        EntityWrapper<MatchRaters> entityWrapper = new EntityWrapper();
        entityWrapper.setSqlSelect("avg(score)")
                .where("match_id = {0}", matchId)
                .and("project_id = {0}", projectId);
        return selectCount(entityWrapper);
    }

    /**
     * 返回参赛选手的平均分列表
     * @param groupId
     * @return
     */
    public List<MatchParticipantScoreVo> listScoreByGroupId(String groupId) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        return baseMapper.listScoreByGroupId(map);
    }
}
