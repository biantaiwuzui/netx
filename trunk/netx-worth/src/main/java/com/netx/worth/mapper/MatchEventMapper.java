package com.netx.worth.mapper;

import com.netx.worth.model.MatchEvent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchAllApplyInfoVO;
import com.netx.worth.vo.MatchEventNewest;
import com.netx.worth.vo.MatchEventSimpleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 比赛 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchEventMapper extends BaseMapper<MatchEvent> {
    List<MatchAllApplyInfoVO> getMatchAllZoneApplyInfo(String matchId);
    List<MatchAllApplyInfoVO> getMatchFirstZoneApplyInfo(String matchId);

    /**
     * 查看用户已通过审核的比赛
     * @param map
     * @return
     */
    List<MatchEventSimpleVo> selectSimpleMatchEventList(Map map);

    /**
     * 查看用户所有创建的的比赛
     * @param map
     * @return
     */
    List<MatchEventSimpleVo> selectAllMySimpleMatchEventList(Map<String,Object> map);

    List<MatchEventSimpleVo> selectParticipantMatch(String userId);

    List<MatchEventSimpleVo> selectApprovedMatchEvent(@Param(value = "start") int start, @Param(value = "size") int size);
    MatchEventSimpleVo selectOneMatchEvent(Map map);
    MatchEventNewest getNewestMatchByUserId(String userId);
}