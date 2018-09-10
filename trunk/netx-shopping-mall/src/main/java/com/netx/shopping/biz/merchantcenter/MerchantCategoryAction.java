package com.netx.shopping.biz.merchantcenter;


import com.netx.shopping.biz.productcenter.CategoryAction;
import com.netx.shopping.model.merchantcenter.MerchantCategory;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.service.merchantcenter.MerchantCategoryService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.shopping.vo.MerchantCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网商-商家类别表 服务action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantCategoryAction {

    @Autowired
    private CategoryAction categoryAction;

    @Autowired
    private MerchantCategoryService merchantCategoryService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询一级类目和二级类目类目
     * @param merchantId
     * @return
     */
    public MerchantCategoryVo getMerchantCategoryVo(String merchantId){
        MerchantCategoryVo merchantCategoryVo =new MerchantCategoryVo();
        List<String> categoryIds = merchantCategoryService.getMerchantCategoryIds(merchantId);
        if (categoryIds != null){
            merchantCategoryVo.setTages(categoryAction.getKidCategory(categoryIds));
            merchantCategoryVo.setCategories(categoryAction.getParentCategory(categoryIds));
        }
        return merchantCategoryVo;
    }

    /**
     * 添加商家-类目关联
     * @param flag
     * @param merchantId
     * @param categoryId
     * @param tagIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdate(boolean flag, String merchantId, String categoryId, String tagIds){
        if (flag) {
            merchantCategoryService.deleteByMerchantId(merchantId);
        }
        addCategory(tagIds.split(","), merchantId);
        addCategory(categoryId.split(","), merchantId);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCategory(String[] array, String merchantId){
        for(String categoryId : array){
            MerchantCategory merchantCategory = new MerchantCategory();
            Category category = categoryService.selectById(categoryId);
            if(category != null){
                merchantCategory.setCategoryId(categoryId);
                merchantCategory.setMerchantId(merchantId);
                merchantCategoryService.insert(merchantCategory);
                categoryAction.updateUsedCount(categoryId, 1);
            }
        }

    }

    public List<String> getCategoryIdByMerchantId(String merchantId){
        return merchantCategoryService.getCategoryIdByMerchantId(merchantId);
    }

    /**
     * 删除商家类目关联表-删除商家调用使用
     * @param merchantId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByMerchantId(String merchantId){
        List<MerchantCategory> merchantCategories = merchantCategoryService.getMerchantCategoryByMerchantId(merchantId);
        List<String> categoryIds = new ArrayList<>();
        for(MerchantCategory merchantCategory : merchantCategories){
            categoryIds.add(merchantCategory.getCategoryId());
            merchantCategory.setDeleted(1);
        }
        if(merchantCategories != null && merchantCategories.size() > 0){
            //更新类目被使用数量
            categoryAction.updateUsedCount(categoryIds, -1);
            return merchantCategoryService.updateBatchById(merchantCategories);
        }
        return false;
    }

    /**
     * 删除商家类目关联表-删除类目调用
     * @param categoryIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByCategoryIds(List<String> categoryIds, Integer delete, Integer deleted){
        List<MerchantCategory> merchantCategories = merchantCategoryService.getMerchantCategoryByCategoryIds(categoryIds, deleted);
        if(merchantCategories != null && merchantCategories.size() > 0) {
            for (MerchantCategory merchantCategory : merchantCategories){
                merchantCategory.setDeleted(delete);
            }
            return merchantCategoryService.updateBatchById(merchantCategories);
        }
        return false;
    }

    /**
     * 网信 - 发行网信 - 商家 -获取一级类目
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> getParentMerchantCategory(String merchantId) {
        List<String> merchantCategoryIds = merchantCategoryService.getMerchantCategoryIdsByMerchantId(merchantId);
        return categoryService.getParentCategoryName(merchantCategoryIds);
    }
}
