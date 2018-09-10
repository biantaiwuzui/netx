package com.netx.worth.mapper;

import com.netx.worth.model.MatchReview;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchReviewVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 比赛审核人 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
public interface MatchReviewMapper extends BaseMapper<MatchReview> {
    List<MatchReviewVo> getReviewListBYMatchId(Map map);
    List<String> getOneTags(String merchantId);
}