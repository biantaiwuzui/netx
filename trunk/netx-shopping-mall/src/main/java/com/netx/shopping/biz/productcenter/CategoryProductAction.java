package com.netx.shopping.biz.productcenter;


import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.model.productcenter.CategoryProduct;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.service.productcenter.CategoryProductService;
import com.netx.shopping.service.productcenter.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品类目关系表
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newCategoryProductAction")
public class CategoryProductAction {

    @Autowired
    private CategoryProductService categoryProductService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryAction categoryAction;

    /**
     * 添加/更新类目
     * @param flag
     * @param productId
     * @param merchantId
     * @param categoryId
     * @param tagIds
     * @return
     */
    @Transactional
    public boolean insertOrUpdate(boolean flag, String merchantId, String productId, String categoryId, String tagIds){
        if (flag) {
            categoryProductService.deleteByProductId(productId);
        }
        addCategory(tagIds.split(","), productId, merchantId);
        addCategory(categoryId.split(","), productId, merchantId);
        return true;
    }

    @Transactional
    public void addCategory(String[] array, String productId, String merchantId){
        for(String categoryId : array){
            CategoryProduct categoryProduct = new CategoryProduct();
            Category category = categoryService.selectById(categoryId);
            if(category != null){
                categoryProduct.setCategoryId(categoryId);
                categoryProduct.setMerchantId(merchantId);
                categoryProduct.setProductId(productId);
                categoryProductService.insert(categoryProduct);
                categoryAction.updateUsedCount(categoryId, 1);
            }
        }

    }

    /**
     * 根据商品productId查询类目categoryId
     * @param productId
     * @return
     */
    public List<String> getCategoryIdByProductId(String productId){
        return categoryProductService.getCategoryIdByProductId(productId);
    }

    /**
     * 删除商品类目关联表、更新类目被使用数量-删除商品使使用
     * @param productIds
     * @return
     */
    @Transactional
    public boolean delete(List<String> productIds){
        List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByProductId(productIds);
        List<String> categoryIds = new ArrayList<>();
        for(CategoryProduct categoryProduct : categoryProducts){
            categoryIds.add(categoryProduct.getCategoryId());
            categoryProduct.setDeleted(1);
        }
        if(categoryProducts != null && categoryProducts.size() > 0){
            //更新类目被使用数量
            categoryAction.updateUsedCount(categoryIds, -1);
            return categoryProductService.updateBatchById(categoryProducts);
        }
        return false;
    }

    /**
     * 删除商家类目关联表-删除类目调用
     * @param categoryIds
     * @return
     */
    public Boolean deleteByCategoryIds(List<String> categoryIds, Integer delete, Integer deleted){
        List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByCategoryIds(categoryIds, deleted);
        if(categoryProducts != null && categoryProducts.size() > 0) {
            for (CategoryProduct categoryProduct : categoryProducts){
                categoryProduct.setDeleted(deleted);
            }
            return categoryProductService.updateBatchById(categoryProducts);
        }
        return false;
    }

}
