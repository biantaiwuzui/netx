package com.netx.ucenter.mapper.common;

import com.netx.common.vo.common.BillStatisticsRequestDto;
import com.netx.ucenter.model.common.CommonBill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonBillMapper extends BaseMapper<CommonBill> {
	public BigDecimal statisticBill(BillStatisticsRequestDto dto);

	BigDecimal countThisDayOutcome(@Param ( "userId" )String userId,@Param ( "type" ) int type);
}