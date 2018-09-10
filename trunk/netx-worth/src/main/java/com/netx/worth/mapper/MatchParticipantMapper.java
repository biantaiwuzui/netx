package com.netx.worth.mapper;

import com.netx.common.user.model.UserMacthInfo;
import com.netx.worth.model.MatchParticipant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchParticipantAllVo;
import com.netx.worth.vo.MatchParticipantVo;
import com.netx.worth.vo.UserForMatchVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 参赛报名 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-17
 */
public interface MatchParticipantMapper extends BaseMapper<MatchParticipant> {
    List<MatchParticipantAllVo> getJionMatchListByMatchId(Map map);

    MatchParticipantVo selectMatchParticipantVo(Map map);

    UserMacthInfo getUserInfo(@Param(value = "userId") String userId);

    UserForMatchVo selectUserInfoForMatch(@Param(value = "userId")String userId);

}