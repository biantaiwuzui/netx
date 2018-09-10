package com.netx.shopping.biz.business;

import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.business.AddCategoryRequestDto;
import com.netx.common.vo.business.DeleteCategoryRequestDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.model.business.Category;
import com.netx.shopping.service.business.CategoryService;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("CategoryAction")
@Transactional(rollbackFor = Exception.class)
public class CategoryAction {

//    @Autowired
//    CategoryService categoryService;
//
//    public CategoryService getCategoryService(){
//        return this.categoryService;
//    }
//
//    public Category addOrUpdate(AddCategoryRequestDto request){
//
//        Category category = new Category();
//        BeanUtils.copyProperties(request, category);
//        if(StringUtils.isNotBlank(request.getId())){
//            category.setUpdateUserId(request.getUserId());
//        }else {
//            category.setDeleted(0);
//            category.setCreateUserId(request.getUserId());
//        }
//        categoryService.insertOrUpdate(category);
//        return category;
//    }
//
//    public List<Category> selectParentList(CommonListDto request){
//        return categoryService.selectProductTags(request).getRecords();
//    }
//
//
//    public List<Category> selectParentListByPy(){
//        return categoryService.selectList();
//    }
//
//    public List<Category> selectKidList(GetKidTagsRequestDto request){
//        return categoryService.selectKidTags(request);
//    }
//
//    public List<Category> selectKidListByPy(GetKidTagsRequestDto request){
//        return categoryService.selectKidTagListByPy(request);
//    }
//
//    public List<Category> selectKidList(List<String> ids){
//        return categoryService.selectkidTags(ids);
//    }
//    public List<Category> selectParentList(List<String> ids){
//        return categoryService.selectPerentTags(ids);
//    }
//
//    public List<Category> selectCategoryByName(String name){
//        return categoryService.selectCategoryByName(name);
//    }
//
//    public boolean delete(DeleteCategoryRequestDto request){
//        Category category = categoryService.selectById(request.getId());
//        if (category != null && category.getCreateUserId() == request.getUserId()){
//            return categoryService.deleteById(request.getId());
//        }
//        return false;
//    }
//
//    public void updateUsedCount(String id){
//        Category category = categoryService.selectById(id);
//        if (category != null) {
//            long count = category.getUsedCount() + 1;
//            category.setUsedCount(count);
//            categoryService.updateById(category);
//        }
//    }
//
//    public boolean setPy(String pid) throws Exception{
//
//        List<Category> categorys = categoryService.selectAll();
//        if (categorys != null){
//            for (Category category1 : categorys){
//                category1.setPy(PinyinHelper.getShortPinyin(category1.getName()).substring(0, 1).toUpperCase());
//                categoryService.updateById(category1);
//            }
//        }
//        return true;
//    }

}
