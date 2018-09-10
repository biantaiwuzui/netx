package com.netx.worth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.worth.model.DemandOrder;

/**
 * <p>
  * 需求订单表，每次入选就生成一张，以应对长期的需求，长期需求只能有1人入选。 Mapper 接口
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-11
 */
public interface DemandOrderMapper extends BaseMapper<DemandOrder> {

}