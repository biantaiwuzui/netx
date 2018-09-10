package com.netx.worth.mapper;

import com.netx.worth.model.MatchProgress;
import com.netx.worth.model.MatchProgressParticipant;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
public interface MatchProgressParticipantMapper extends BaseMapper<MatchProgressParticipant> {
    List<MatchProgress> getPassParticipant(Map map);
}