package com.netx.worth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.common.wz.dto.demand.DemandRegisterDto;
import com.netx.worth.model.DemandRegister;

/**
 * <p>
  * 需求报名表 Mapper 接口
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
public interface DemandRegisterMapper extends BaseMapper<DemandRegister> {
    boolean updateDate(DemandRegisterDto demandRegisterDto);
}