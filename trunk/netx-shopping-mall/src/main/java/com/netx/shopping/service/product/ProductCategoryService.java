package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.ProductCategoryMapper;
import com.netx.shopping.model.product.ProductCategory;
import org.springframework.data.convert.EntityWriter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 4/3/2018.
 */
@Service
public class ProductCategoryService extends ServiceImpl<ProductCategoryMapper, ProductCategory> {

    public List<String> selectProductCategoryList(String productId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("category_id").in("product_id",productId);
        return this.selectObjs(wrapper);
    }

    public boolean deleteByProductId(String productId){
        EntityWrapper<ProductCategory> wrapper = new EntityWrapper<>();
        wrapper.in("product_id",productId);
        return this.delete(wrapper);
    }

    public List<String> getCategoryIdByProductId(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id");
        wrapper.where("product_id = {0} AND deleted = 0",productId);
        return this.selectObjs(wrapper);
    }

}
