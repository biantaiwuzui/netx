package com.netx.worth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.model.Demand;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 需求表 Mapper 接口
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
public interface DemandMapper extends BaseMapper<Demand> {

    public boolean updateOrderId(@Param("id") String id,@Param("orderIds") String orderIds);
}