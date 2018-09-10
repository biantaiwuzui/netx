package com.netx.worth.mapper;

import com.netx.worth.model.MatchVenue;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Date;

/**
 * <p>
  * 比赛场次 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-16
 */
public interface MatchVenueMapper extends BaseMapper<MatchVenue> {
    Date getLastEndTimeBtMatchId(String matchId);
}