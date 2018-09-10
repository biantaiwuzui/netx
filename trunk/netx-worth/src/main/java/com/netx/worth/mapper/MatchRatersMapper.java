package com.netx.worth.mapper;

import com.netx.worth.model.MatchRaters;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchParticipantScoreVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 比赛评分 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchRatersMapper extends BaseMapper<MatchRaters> {
    List<MatchParticipantScoreVo>listScoreByGroupId(Map map);
}