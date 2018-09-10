package com.netx.shopping.mapper.marketing;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.marketing.SellerArea;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public interface SellerAreaMapper extends BaseMapper<SellerArea> {
    SellerArea selectAreaByAreaName(@Param("areaName") String areaName);
}