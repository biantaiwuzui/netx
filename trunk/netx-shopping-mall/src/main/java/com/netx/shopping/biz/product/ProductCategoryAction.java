package com.netx.shopping.biz.product;

import com.netx.shopping.biz.business.CategoryAction;
import com.netx.shopping.model.business.Category;
import com.netx.shopping.model.product.ProductCategory;
import com.netx.shopping.service.product.ProductCategoryService;
import com.netx.shopping.vo.ProductCategoryVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 网商-商品类目表 服务实现类
 * </p>
 *
 * @author liwei
 * @since 2017-4-3
 */
@Service("productCategoryAction")
@Transactional(rollbackFor = Exception.class)
public class ProductCategoryAction {

//    @Autowired
//    ProductCategoryService productCategoryService;
//
//    @Autowired
//    CategoryAction categoryAction;
//
//    public boolean insertOrUpdate(boolean flag,String productId,String categoryId,String tagIds,String userId){
//        if (flag) {
//            productCategoryService.deleteByProductId(productId);
//        }
//        insertProductCategories(tagIds,productId,userId);
//        insertProductCategories(categoryId,productId,userId);
//        return true;
//    }
//
//    private void insertProductCategories(String ids,String productId,String userId){
//        String[] idArray = ids.split(",");
//        for(String id:idArray){
//            insertProductCategory(id,productId,userId);
//        }
//    }
//
//    private void insertProductCategory(String categoryid,String productId,String userId){
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryId(categoryid);
//        productCategory.setProductId(productId);
//        productCategory.setCreateUserId(userId);
//        productCategoryService.insert(productCategory);
//        categoryAction.updateUsedCount(categoryid);
//    }
//
//    public ProductCategoryVo getProductCategoryVo(String productId){
//        ProductCategoryVo productCategoryVo =new ProductCategoryVo();
//        List<String> categoryIds = productCategoryService.selectProductCategoryList(productId);
//        if (categoryIds != null){
//            productCategoryVo.setTages(categoryAction.selectKidList(categoryIds)==null?null:categoryAction.selectKidList(categoryIds));
//            productCategoryVo.setCategory(categoryAction.selectParentList(categoryIds)==null?null:categoryAction.selectParentList(categoryIds));
//        }
//        return productCategoryVo;
//    }
//
//    public String getCategoryName(String productId){
//        ProductCategoryVo productCategoryVo = this.getProductCategoryVo(productId);
//        if (productCategoryVo != null && productCategoryVo.getCategory() != null){
//            return productCategoryVo.getCategory().get(0).getName();
//        }
//        return null;
//    }
//
//    public String getTagesName(String productId){
//        List<String> list = new ArrayList<>();
//        ProductCategoryVo productCategoryVo = this.getProductCategoryVo(productId);
//        if (productCategoryVo != null && productCategoryVo.getTages() != null){
//            for (Category category : productCategoryVo.getTages()){
//                list.add(category.getName());
//            }
//            return StringUtils.join(list.toArray(), ",");
//        }
//        return null;
//    }
}
