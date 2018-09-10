package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.mapper.business.CategoryMapper;
import com.netx.shopping.model.business.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

//    @Autowired
//    CategoryMapper categoryMapper;
//
//    /**分页查询所有一级标签*/
//    public Page<Category> selectProductTags(CommonListDto request){
//        Page page = new Page();
//        page.setSize(request.getSize());
//        page.setCurrent(request.getCurrentPage());
//        Wrapper wrapper = new EntityWrapper();
//        wrapper.where("pid = '0'").orderBy("sort");
//        return selectPage(page,wrapper);
//    }
//
//    public List selectList(){
//        Wrapper wrapper = new EntityWrapper();
//        wrapper.where("pid = '0'").orderBy("py");
//        return selectList(wrapper);
//    }
//
//    public List<Category> selectKidTags(GetKidTagsRequestDto request){
//        Integer current = (request.getCurrentPage() - 1) * request.getSize();
//        return categoryMapper.selectKidTags(request.getPid(),current,request.getSize());
//    }
//
//    public List<Category> selectKidTagListByPy(GetKidTagsRequestDto request){
//        EntityWrapper<Category> wrapper = new EntityWrapper<>();
//        wrapper.where("pid = {0}",request.getPid()).orderBy("py");
//        return selectList(wrapper);
//    }
//
//    public Integer selectKidTagsCount(GetKidTagsRequestDto request){
//        Wrapper wrapper = new EntityWrapper();
//        wrapper.where("pid={0}",request.getPid());
//        return selectCount(wrapper);
//    }
//
//    public List<Category> selectCategoryByName(String name){
//        EntityWrapper<Category> wrapper = new EntityWrapper<>();
//        wrapper.like("name",name);
//        return this.selectList(wrapper);
//    }
//
//    public List<Category> selectkidTags(List<String> ids){
//        return this.selectList(createEntity(ids));
//    }
//
//    private EntityWrapper<Category> createEntity(List<String> ids){
//        EntityWrapper<Category> wrapper = new EntityWrapper<>();
//        wrapper.where("pid != '0'").in("id",ids);
//        return wrapper;
//    }
//
//    public String selectkidTagNames(List<String> ids){
//        EntityWrapper<Category> wrapper = createEntity(ids);
//        wrapper.setSqlSelect("GROUP_CONCAT(name)");
//        return (String) this.selectObj(wrapper);
//    }
//
//    public List<Category> selectPerentTags(List<String> ids){
//        EntityWrapper<Category> wrapper = new EntityWrapper<>();
//        wrapper.where("pid = '0'").in("id",ids);
//        return this.selectList(wrapper);
//    }
//
//    public List<String> getTagesByProductId(List<String> categoryIds,Boolean type){
//        EntityWrapper wrapper = new EntityWrapper();
//        String sql = "pid "+(type?"":"!")+"= '0' AND deleted = 0";
//        wrapper.setSqlSelect("name AS tages").in("id", categoryIds).where(sql);
//        return this.selectObjs(wrapper);
//    }
//
//    public List<String> getPidByCategoryIds(List<String> categoryIds){
//        EntityWrapper wrapper = new EntityWrapper();
//        wrapper.setSqlSelect("pid").in("id", categoryIds).where("deleted = 0");
//        return this.selectObjs(wrapper);
//    }
//
//    public List<String> getPidById(List<String> ids){
//        EntityWrapper wrapper = new EntityWrapper();
//        wrapper.setSqlSelect("name").in("id",ids).where("deleted = 0");
//        return this.selectObjs(wrapper);
//    }
//
//    public List<Category> selectAll(){
//        EntityWrapper<Category> wrapper = new EntityWrapper();
//        wrapper.where("deleted != 1");
//        return this.selectList(wrapper);
//    }

}
