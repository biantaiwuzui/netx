package com.netx.shopping.biz.productcenter;


import com.baomidou.mybatisplus.plugins.Page;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.business.DeleteCategoryRequestDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.biz.BaseAction;
import com.netx.shopping.biz.merchantcenter.MerchantCategoryAction;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.model.productcenter.constants.CategoryIconEnum;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.shopping.vo.AddCategoryRequestDto;
import com.netx.shopping.vo.QueryCategoryRequestDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商家类目表
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newCategoryAction")
public class CategoryAction extends BaseAction{

    private Logger logger = LoggerFactory.getLogger(CategoryAction.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MerchantCategoryAction merchantCategoryAction;
    @Autowired
    private CategoryProductAction categoryProductAction;

    public CategoryService getCategoryService(){
        return this.categoryService;
    }

    /**
     * 添加/修改单个类目
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Category addOrUpdate(AddCategoryRequestDto requestDto){
        Category category = createCategory(requestDto);
        //更新优先级
        categoryService.updatePriority(requestDto.getPriority(), requestDto.getParentId(), 1);
        return categoryService.insertOrUpdate(category)?category:null;
    }

    /**
     * 批量添加或修改类目
     * @param requestDtos
     * @return
     */
    public Boolean addOrUpdateList(List<AddCategoryRequestDto> requestDtos){
        for(AddCategoryRequestDto requestDto : requestDtos){
            addOrUpdate(requestDto);
        }
        return true;
    }

    public Category createCategory(AddCategoryRequestDto requestDto){
        Category category = new Category();
        if(StringUtils.isNotBlank(requestDto.getId())){
            category = categoryService.selectById(requestDto.getId());
        } else {
            category.setUsedCount(0l);
        }
        if(requestDto.getIcon() == 1){
            category.setIcon(CategoryIconEnum.RECEPTION.getName());
        }else{
            category.setIcon(CategoryIconEnum.BACKSTAGE.getName());
        }
        category.setName(requestDto.getName());
        category.setParentId(requestDto.getParentId());
        category.setPriority(requestDto.getPriority());
        try {
            category.setPy(PinyinHelper.getShortPinyin(category.getName()).substring(0, 1).toUpperCase());
        } catch (PinyinException e) {
            logger.warn("拼音转换出错", e);
        }
        return category;
    }

    /**
     * 根据categoryIds获取一级类目
     * @param categoryIds
     * @return
     */
    public List<Category> getParentCategory(List<String> categoryIds){
        return categoryService.getParentCategory(categoryIds);
    }

    /**
     * 根据categoryIds获取二级类目
     * @param categoryIds
     * @return
     */
    public List<Category> getKidCategory(List<String> categoryIds){
        return categoryService.getKidCategory(categoryIds);
    }

    /**
     * 更新类目被使用数量
     * @param ids
     * @param usedCount
     * @return
     */
    public boolean updateUsedCount(List<String> ids, Integer usedCount){
        List<Category> categories = categoryService.selectBatchIds(ids);
        for(Category category : categories){
            category.setUsedCount(category.getUsedCount() + usedCount);
        }
        if(categories != null && categories.size() > 0){
            return categoryService.updateBatchById(categories);
        }
        return false;
    }

    /**
     * 更新类目被使用数量
     * @param id
     * @param usedCount
     * @return
     */
    public boolean updateUsedCount(String id, Integer usedCount){
        Category category = categoryService.selectById(id);
        if(category != null){
            category.setUsedCount(category.getUsedCount() + usedCount);
            return categoryService.updateById(category);
        }
        return false;
    }

    /**
     * 分页查询所有一级标签
     * @param request
     * @return
     */
    public List<Category> selectParentList(CommonListDto request){
        return categoryService.selectParentListOrderByPriority(request).getRecords();
    }

    /**
     * 根据拼音排序查询所有一级标签
     * @return
     */
    public List<Category> selectParentListByPy(){
        return categoryService.selectList();
    }

    /**
     * 分页查询所有二级标签
     * @param request
     * @return
     */
    public List<Category> selectKidList(GetKidTagsRequestDto request){
        return categoryService.selectKidTags(request);
    }

    /**
     * 根据拼音排序查询所有二级标签
     * @param request
     * @return
     */
    public List<Category> selectKidListByPy(GetKidTagsRequestDto request){
        return categoryService.selectKidTagListByPy(request.getPid());
    }

    /**
     * 模糊查询商家类目
     * @param name
     * @return
     */
    public List<Category> selectCategoryByName(String name){
        return categoryService.selectCategoryByName(name);
    }


    /**
     * 设置拼音首字母
     * @param pid
     * @return
     * @throws Exception
     */
    public boolean setPy(String pid) throws Exception{
        List<Category> categorys = categoryService.selectAll();
        if (categorys != null){
            for (Category category1 : categorys){
                category1.setPy(PinyinHelper.getShortPinyin(category1.getName()).substring(0, 1).toUpperCase());
                categoryService.updateById(category1);
            }
        }
        return true;
    }

    /**
     * 删除类目
     * @param categoryId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(String categoryId){
        List<String> categoryIds = new ArrayList<>();
        Category category = categoryService.selectById(categoryId);
        if(category != null){
            category.setDeleted(1);
            if(categoryService.updateById(category)) {
                //更新优先级
                categoryService.updatePriority(category.getPriority(), category.getParentId(), -1);
                categoryIds.add(category.getId());
                //判断是否是一级标签
                if ("0".equals(category.getParentId())) {
                    List<Category> categories = categoryService.selectKidTagListByPy(categoryId);
                    if(categories != null && categories.size() > 0){
                        for(Category category1 : categories){
                            categoryIds.add(category1.getId());
                        }
                        //删除二级标签
                        categoryService.deleteByParentId(categoryId, 1, 0);
                    }
                }
                //删除商家/商品关联表
                merchantCategoryAction.deleteByCategoryIds(categoryIds, 1, 0);
                categoryProductAction.deleteByCategoryIds(categoryIds, 1, 0);
                return true;
            }
        }
        return false;
    }

    /**
     * 恢复类目
     * @param categoryId
     * @return
     */
    public String recoveryById(String categoryId){
        List<String> categoryIds = new ArrayList<>();
        Category category = categoryService.selectById(categoryId);
        if(category != null){
            if(!"0".equals(category.getParentId())){
                if(category.getDeleted() != 0){
                    return "请先恢复启用该二级类目所属的一级类目！";
                }
            }
            category.setDeleted(0);
            if(categoryService.updateById(category)) {
                //更新优先级
                categoryService.updatePriority(category.getPriority(), category.getParentId(), 1);
                categoryIds.add(category.getId());
                //判断是否是一级标签
                if ("0".equals(category.getParentId())) {
                    List<Category> categories = categoryService.selectKidTagListByPy(categoryId);
                    if(categories != null && categories.size() > 0){
                        for(Category category1 : categories){
                            categoryIds.add(category1.getId());
                        }
                        //恢复二级标签
                        categoryService.deleteByParentId(categoryId, 0, 1);
                    }
                }
                //恢复商家/商品关联表
                merchantCategoryAction.deleteByCategoryIds(categoryIds, 0, 1);
                categoryProductAction.deleteByCategoryIds(categoryIds, 0, 1);
                return null;
            }
        }
        return "恢复网商类目失败！";
    }

    /**
     * -boss系统-
     * 获取标签列表
     * @param requestDto
     * @return
     */
    public Map<String, Object> queryCategroyList(QueryCategoryRequestDto requestDto){
        Page page = new Page(requestDto.getCurrent(), requestDto.getSize());
        Page<Category> categoryPage = categoryService.getCategroyList(requestDto.getParentId(), requestDto.getName(), requestDto.getDeleted(), page);
        Map<String, Object> map = new HashMap<>();
        map.put("total", categoryPage.getTotal());
        map.put("list", categoryPage.getRecords());
        return map;
    }


}
