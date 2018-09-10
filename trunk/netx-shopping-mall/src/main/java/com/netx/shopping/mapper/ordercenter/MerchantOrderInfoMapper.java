package com.netx.shopping.mapper.ordercenter;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.common.user.model.StatData;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
public interface MerchantOrderInfoMapper extends BaseMapper<MerchantOrderInfo> {


    @MapKey("user_id")
    public List<StatData> queryShoppingStat();

}