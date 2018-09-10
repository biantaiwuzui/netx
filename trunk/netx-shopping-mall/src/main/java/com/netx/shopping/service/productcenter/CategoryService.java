package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.mapper.productcenter.CategoryMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商家类目表 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newCategoryService")
public class CategoryService extends ServiceImpl<CategoryMapper, Category>{

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 根据categoryIds获取一级类目
     * @param categoryIds
     * @return
     */
    public List<Category> getParentCategory(List<String> categoryIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id", categoryIds);
        wrapper.where("parent_id = '0' AND deleted = 0");
        return this.selectList(wrapper);
    }

    /**
     * 根据categoryIds获取二级类目
     * @param categoryIds
     * @return
     */
    public List<Category> getKidCategory(List<String> categoryIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id", categoryIds);
        wrapper.where("parent_id != '0' AND deleted = 0");
        return this.selectList(wrapper);
    }

    /**
     * 根据categoryIds获取一级类目名
     * @param categoryIds
     * @return
     */
    public List<String> getParentCategoryName(List<String> categoryIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name");
        wrapper.in("id", categoryIds);
        wrapper.where("parent_id = '0' AND deleted = 0");
        return this.selectObjs(wrapper);
    }


    /**
     * 根据categoryIds获取二级类目名
     * @param categoryIds
     * @return
     */
    public List<String> getKidCategoryName(List<String> categoryIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name");
        wrapper.in("id", categoryIds);
        wrapper.where("parent_id != '0' AND deleted = 0");
        return this.selectObjs(wrapper);
    }

    /**
     * 根据优先级分页查询所有一级标签
     * @param request
     * @return
     */
    public Page<Category> selectParentListOrderByPriority(CommonListDto request){
        Page page = new Page();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrentPage());
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("parent_id = '0' AND deleted = 0").orderBy("priority");
        return selectPage(page,wrapper);
    }

    /**
     * 根据拼音排序查询所有一级标签
     * @return
     */
    public List selectList(){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("parent_id = '0' AND deleted = 0").orderBy("py");
        return selectList(wrapper);
    }
    /**
     * 根据拼音排序分页查询二级标签
     * @param request
     * @return
     */
    public List<Category> selectKidTags(GetKidTagsRequestDto request){
        Page page = new Page();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrentPage());
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("parent_id = {0} AND deleted = 0", request.getPid()).orderBy("py");
        return selectPage(page,wrapper).getRecords();
    }

    /**
     * 根据拼音排序查询所有二级标签
     * @param parentId
     * @return
     */
    public List<Category> selectKidTagListByPy(String parentId){
        EntityWrapper<Category> wrapper = new EntityWrapper<>();
        wrapper.where("parent_id = {0} AND deleted = 0", parentId).orderBy("py");
        return selectList(wrapper);
    }

    /**
     * 根据一级类目id查询二级类目数量
     * @param request
     * @return
     */
    public Integer selectKidTagsCount(GetKidTagsRequestDto request){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("parent_id={0} AND deleted = 0",request.getPid());
        return selectCount(wrapper);
    }

    /**
     * 模糊查询商家类目
     * @param name
     * @return
     */
    public List<Category> selectCategoryByName(String name){
        EntityWrapper<Category> wrapper = new EntityWrapper<>();
        wrapper.like("name", name);
        return this.selectList(wrapper);
    }

    public List<Category> selectAll(){
        EntityWrapper<Category> wrapper = new EntityWrapper();
        wrapper.where("deleted = 0");
        return this.selectList(wrapper);
    }

    /**
     * 更新优先级
     * @param priority
     * @param parentId
     * @param num
     * @return
     */
    public Boolean updatePriority(Long priority, String parentId, Integer num){
        return categoryMapper.updatePriority(priority, parentId, num);
    }

    /**
     * 根据一级标签删除二级标签
     * @param parentId
     * @return
     */
    public Boolean deleteByParentId(String parentId, Integer delete, Integer deleted){
        return categoryMapper.deleteByParentId(parentId, delete, deleted);
    }

    /**
     * 获取类目列表-boss系统
     * @param parentId
     * @param name
     * @param deleted
     * @param page
     * @return
     */
    public Page<Category> getCategroyList(String parentId, String name, Integer deleted, Page page){
        EntityWrapper<Category> wrapper = new EntityWrapper();
        if(StringUtils.isNotBlank(parentId)){
            wrapper.and("parent_id = {0}", parentId);
        }
        if(deleted != null){
            wrapper.and("deleted = {0}", deleted);
        }
        if(StringUtils.isNotBlank(name)){
            wrapper.like("name", name);
        }
        wrapper.orderBy("priority", true);
        return selectPage(page, wrapper);
    }

}
