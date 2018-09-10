package com.netx.shopping.mapper.marketingcenter;

import com.netx.shopping.model.marketingcenter.MerchantAddCustomeragent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 网商-添加客服代理请求表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
public interface MerchantAddCustomeragentMapper extends BaseMapper<MerchantAddCustomeragent> {

    Boolean updateState(@Param("state") Integer state, @Param("pid") String pid, @Param("merchantId") String merchantId);

}