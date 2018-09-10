package com.netx.shopping.mapper.business;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.business.SellerCashier;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 网商-收银人员表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */


public interface SellerCashierMapper extends BaseMapper<SellerCashier> {

    List<SellerCashier> selectListByUserId(@Param("userId")String userId);
}