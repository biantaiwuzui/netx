package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.model.productcenter.CategoryProduct;
import com.netx.shopping.mapper.productcenter.CategoryProductMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品类目关系表 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newCategoryProductService")
public class CategoryProductService extends ServiceImpl<CategoryProductMapper, CategoryProduct>{

    /**
     * 根据商品productId查询类目categoryId
     * @param productId
     * @return
     */
    public List<String> getCategoryIdByProductId(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id").where("product_id = {0} AND deleted = {1}", productId, 0);
        return this.selectObjs(wrapper);
    }

    /**
     * 根据商品productId查询类目关联表
     * @param productIds
     * @return
     */
    public List<CategoryProduct> getCategoryProductByProductId(List<String> productIds){
        EntityWrapper<CategoryProduct> wrapper = new EntityWrapper();
        wrapper.in("product_id", productIds).where("deleted = {0}", 0);
        return this.selectList(wrapper);
    }

    /**
     * 根据productId删除商品类目关联表
     * @param productId
     * @return
     */
    public boolean deleteByProductId(String productId){
        EntityWrapper<CategoryProduct> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0}", productId);
        return this.delete(wrapper);
    }

    /**
     * 根据类目id查询商品类目关联表
     * @param categoryIds
     * @return
     */
    public List<CategoryProduct> getCategoryProductByCategoryIds(List<String> categoryIds, Integer deleted){
        EntityWrapper<CategoryProduct> wrapper = new EntityWrapper();
        wrapper.in("category_id", categoryIds).where("deleted = {0}", deleted);
        return this.selectList(wrapper);
    }
	
}
