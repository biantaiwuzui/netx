package com.netx.shopping.mapper.merchantcenter;

import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 网商-收银人员表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
public interface MerchantManagerMapper extends BaseMapper<MerchantManager> {

    Boolean updateIsCurrentByMerchantId(@Param("merchantId") String merchantId, @Param("merchantUserType") String merchantUserType);

    Boolean updateIsCurrentById(@Param("id") String id);

}