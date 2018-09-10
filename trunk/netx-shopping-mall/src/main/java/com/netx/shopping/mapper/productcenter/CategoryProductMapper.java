package com.netx.shopping.mapper.productcenter;

import com.netx.shopping.model.productcenter.CategoryProduct;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 商品类目关系表 Mapper 接口
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Repository("newCategoryProductMapper")
public interface CategoryProductMapper extends BaseMapper<CategoryProduct> {

}