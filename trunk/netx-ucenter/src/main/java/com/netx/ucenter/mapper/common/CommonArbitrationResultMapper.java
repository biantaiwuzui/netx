package com.netx.ucenter.mapper.common;

import com.netx.common.vo.common.ArbitrationSelectResponseVo;
import com.netx.ucenter.model.common.CommonArbitrationResult;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonArbitrationResultMapper extends BaseMapper<CommonArbitrationResult> {

	public ArbitrationSelectResponseVo selectByOpUserId(@Param("opUserId") String opUserId) throws Exception;
}