package com.netx.shopping.mapper.productcenter;

import com.netx.shopping.model.productcenter.Sku;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

/**
 * <p>
  * 商品中心-SKU Mapper 接口
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-05
 */
public interface SkuMapper extends BaseMapper<Sku> {

    /**
     * 获取商品总销量
     * @param productId
     * @return
     */
    Long getSumSellNums(@Param("productId") String productId);

}