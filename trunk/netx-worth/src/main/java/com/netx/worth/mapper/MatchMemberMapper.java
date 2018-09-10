package com.netx.worth.mapper;

import com.netx.worth.model.MatchMember;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchUserInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 比赛工作人员 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchMemberMapper extends BaseMapper<MatchMember> {
    List<MatchUserInfoVo> getWorkPeopleList(Map map);
}