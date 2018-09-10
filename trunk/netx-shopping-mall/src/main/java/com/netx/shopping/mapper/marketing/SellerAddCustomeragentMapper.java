package com.netx.shopping.mapper.marketing;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.marketing.SellerAddCustomeragent;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public interface SellerAddCustomeragentMapper extends BaseMapper<SellerAddCustomeragent> {
    Boolean updateState(@Param("state") Integer state, @Param("pid") String pid, @Param("sellerId") String sellerId);

    Boolean updateTrueState(@Param("pid") String pid,@Param("sellerId") String sellerId);
}