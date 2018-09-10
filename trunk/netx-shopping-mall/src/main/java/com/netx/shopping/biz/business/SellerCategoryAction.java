package com.netx.shopping.biz.business;


import com.netx.shopping.model.business.Category;
import com.netx.shopping.model.business.SellerCategory;
import com.netx.shopping.model.merchantcenter.MerchantCategory;
import com.netx.shopping.service.business.SellerCategoryService;
import com.netx.shopping.service.merchantcenter.MerchantCategoryService;
import com.netx.shopping.vo.ProductCategoryVo;
import com.netx.shopping.vo.SellerCategoryVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 网商-商家类别表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("sellerCategoryAction")
@Transactional(rollbackFor = Exception.class)
public class SellerCategoryAction {

//    @Autowired
//    SellerCategoryService sellerCategoryService;
//
//    @Autowired
//    CategoryAction categoryAction;
//
//    public boolean insertOrUpdate(boolean flag,String sellerId,String categoryId,String tagIds,String userId){
//        if (flag) {
//            sellerCategoryService.deleteBySellerId(sellerId);
//        }
//        updateCategory(tagIds.split(","),sellerId,userId);
//        updateCategory(categoryId.split(","),sellerId,userId);
//        return true;
//    }
//
//
//    private void updateCategory(String[] array,String sellerId,String userId){
//        if (array != null){
//            for(String categoryid : array){
//                SellerCategory sellerCategory = new SellerCategory();
//                sellerCategory.setCategoryId(categoryid);
//                sellerCategory.setSellerId(sellerId);
//                sellerCategory.setCreateUserId(userId);
//                sellerCategoryService.insert(sellerCategory);
//                categoryAction.updateUsedCount(categoryid);
//            }
//        }
//    }
//
//
//    public SellerCategoryVo getSellerCategoryVo(String sellerId){
//        SellerCategoryVo sellerCategoryVo =new SellerCategoryVo();
//        List<String> categoryIds = sellerCategoryService.selectSellerCategoryList(sellerId);
//        if (categoryIds != null){
//            sellerCategoryVo.setTages(categoryAction.selectKidList(categoryIds));
//            sellerCategoryVo.setCategories(categoryAction.selectParentList(categoryIds));
//        }
//        return sellerCategoryVo;
//    }
//
//    public String getCategoryName(String sellerId){
//        //List<String> list = new ArrayList<>();
//        List<String> categoryIds = sellerCategoryService.selectSellerCategoryList(sellerId);
//        if (categoryIds != null){
//            return categoryAction.getCategoryService().selectkidTagNames(categoryIds);
//        }
//        return null;
//    }
//
//    public String getTagesName(String sellerId){
//        List<String> list = new ArrayList<>();
//        SellerCategoryVo sellerCategoryVo = this.getSellerCategoryVo(sellerId);
//        if (sellerCategoryVo != null && sellerCategoryVo.getTages() != null){
//            for (Category category : sellerCategoryVo.getTages()){
//                list.add(category.getName());
//            }
//            return StringUtils.join(list.toArray(), ",");
//        }
//        return null;
//    }
}