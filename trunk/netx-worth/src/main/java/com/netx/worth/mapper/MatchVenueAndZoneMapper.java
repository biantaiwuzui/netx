package com.netx.worth.mapper;

import com.netx.worth.model.MatchVenueAndZone;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.TitleAndVenueIdVo;

import java.util.List;

/**
 * <p>
  * 场次和赛区的关系表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchVenueAndZoneMapper extends BaseMapper<MatchVenueAndZone> {
    List<TitleAndVenueIdVo> getMatchVunueBYMatchZoneId(String matchVenueId);
}