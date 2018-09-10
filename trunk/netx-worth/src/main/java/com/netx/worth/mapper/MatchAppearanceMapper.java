package com.netx.worth.mapper;

import com.netx.worth.model.MatchAppearance;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.vo.MatchAppearanceOrderVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 比赛演出次序表 Mapper 接口
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
public interface MatchAppearanceMapper extends BaseMapper<MatchAppearance> {
    List<MatchAppearanceOrderVo> listMatchAppearanceVo(Map<String, String> map);
    List<MatchAppearanceOrderVo> listMatchAppearanceVoByProgress(Map<String, String> map);

}