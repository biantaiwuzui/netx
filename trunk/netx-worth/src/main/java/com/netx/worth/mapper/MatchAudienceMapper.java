package com.netx.worth.mapper;

import com.netx.common.user.model.UserMacthInfo;
import com.netx.worth.model.MatchAudience;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 门票支付表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchAudienceMapper extends BaseMapper<MatchAudience> {
    List<UserMacthInfo> listAudienceByMatchId(Map<String, String> map);

}