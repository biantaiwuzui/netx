package com.netx.worth.mapper;

import com.netx.worth.model.MatchTicket;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchBuyTicketVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 参赛门票详情 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
public interface MatchTicketMapper extends BaseMapper<MatchTicket> {
    Date getEndTicketTimeByMatchId(String matchId);
    List<MatchTicket> getMatchTicketByProgress(String progressId);

    List<MatchBuyTicketVo> getBuyTicketByUserId(Map map);
}