package com.netx.worth.mapper;

import com.netx.worth.model.MatchGroup;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchGroupZoneVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 赛组表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchGroupMapper extends BaseMapper<MatchGroup> {
    List<MatchGroupZoneVo> selectMatchGroupByZoneId(@Param(value = "zoneId") String zoneId);
    List<MatchGroupZoneVo> selectMatchGroupByMatchId(String matchId);
}