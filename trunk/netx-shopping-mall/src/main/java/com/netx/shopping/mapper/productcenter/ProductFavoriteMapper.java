package com.netx.shopping.mapper.productcenter;

import com.netx.shopping.model.productcenter.ProductFavorite;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 网商-商品关注表 Mapper 接口
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Repository("newProductFavoriteMapper")
public interface ProductFavoriteMapper extends BaseMapper<ProductFavorite> {

}