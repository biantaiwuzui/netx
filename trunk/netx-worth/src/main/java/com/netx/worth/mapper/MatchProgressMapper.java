package com.netx.worth.mapper;

import com.netx.common.user.model.UserMacthInfo;
import com.netx.worth.model.MatchProgress;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 赛制表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchProgressMapper extends BaseMapper<MatchProgress> {
    List<UserMacthInfo> getJionMatchListByMatchId(Map map);
}