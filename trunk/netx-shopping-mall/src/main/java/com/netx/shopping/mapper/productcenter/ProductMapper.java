package com.netx.shopping.mapper.productcenter;

import com.netx.shopping.model.productcenter.Product;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 网商-商品表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-11
 */
@Repository("newProductMapper")
public interface ProductMapper extends BaseMapper<Product> {

    List<Map<String,String>> selectGoodsList();

}