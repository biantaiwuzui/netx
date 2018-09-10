package com.netx.worth.mapper;

import com.netx.worth.model.MatchGroupAndZone;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchApplyInfoVo;
import com.netx.worth.vo.MatchGroupAndTimeVo;
import com.netx.worth.vo.MatchGroupAndZoneVo;
import com.netx.worth.vo.MatchGroupVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
  * 赛组和赛区表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchGroupAndZoneMapper extends BaseMapper<MatchGroupAndZone> {
    List<MatchGroupAndZoneVo> getAllMacthGroupAndZoneByMatchId(String matchId);
    List<MatchApplyInfoVo> getApplyInfoByZoneId(String zoneId);
    List<MatchGroupAndTimeVo> getMatchGroupByZoneId(String zoneId);
    Date getEndApplyTimeByMatchId(String matchId);
}