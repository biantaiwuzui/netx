package com.netx.shopping.mapper.merchantcenter;

import com.netx.shopping.model.merchantcenter.Merchant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
public interface MerchantMapper extends BaseMapper<Merchant> {

    boolean updateDayNum();

    List<SellerAgentDto> selectMerchantsAndRandNo(@Param("userId") String userId);

    Long selectMerchantAndRandNo(@Param("id") String id,@Param("provinceCode") String provinceCode,@Param("cityCode") String cityCode);

}