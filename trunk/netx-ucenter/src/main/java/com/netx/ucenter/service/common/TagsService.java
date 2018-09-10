package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.common.*;
import com.netx.ucenter.mapper.common.CommonTagsMapper;
import com.netx.ucenter.model.common.CommonTags;
import com.netx.ucenter.model.user.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-1
 */
@Service
public class TagsService extends ServiceImpl<CommonTagsMapper, CommonTags>{
    private Logger logger = LoggerFactory.getLogger(TagsService.class);

    @Autowired
    CommonTagsMapper wzCommonTagsMapper;

    public CommonTagsMapper getWzCommonTagsMapper() {
        return wzCommonTagsMapper;
    }

    public void deleteTag(String userId) throws Exception{
        delete(new EntityWrapper<CommonTags>().eq("create_user_id",userId));
    }

    public List<CommonTags> getWzCommonTagsList(String value,String createUser,String typeCate){
        EntityWrapper entityWrapper = new EntityWrapper(new CommonTags());
        entityWrapper.andNew("value={0}", value)
                .andNew("create_user_id='0' or create_user_id={0}" ,createUser)
                .andNew("deleted=0")
                .andNew("type_cate={0}",typeCate);
        return this.selectList(entityWrapper);
    }

    public List<CommonTags> queryList(String createUser,Integer type,String typeCate) throws Exception {
        EntityWrapper entityWrapper = new EntityWrapper(new CommonTags());
        if (createUser.equals("0")) {//查询所有公用标签
            entityWrapper.where("create_user_id={0}", createUser)
                    .andNew("type={0}", type)
                    .andNew("deleted=0");
        } else if (createUser.equals("-1")) {//查询类目下的所有标签
            entityWrapper.where("type={0}", type);
            entityWrapper.andNew("deleted=0");
        } else {//查询私有标签
            entityWrapper.where("create_user_id={0}", createUser)
                    .andNew("type={0}", type);
        }
        if (!StringUtils.isEmpty(typeCate)) {
            entityWrapper.andNew("type_cate={0}", typeCate);
        }
        entityWrapper.orderBy("py", true);
        return this.selectList(entityWrapper);
    }

    public List<CommonTags> selectPrivate(Integer type,String typeCate) throws Exception{
    //public List<WzCommonTags> selectPrivate(TagsQueryRequestDto request) throws Exception {
        EntityWrapper entityWrapper = new EntityWrapper(new CommonTags());
        entityWrapper.setSqlSelect("DISTINCT(value) value,id,create_user_id as createUserId,type,type_cate as typeCate,py,cate_private as catePrivate");
        entityWrapper.where("create_user_id!='0'");
        if (!StringUtils.isEmpty(type)) {
            entityWrapper.where("type={0}", type);
        }
        if (!StringUtils.isEmpty(typeCate)) {
            entityWrapper.where("type_cate={0}", typeCate);
        }
        entityWrapper.orderBy("py").orderBy("value");
        return this.selectList(entityWrapper);
    }

    public List<Map<String, Object>> queryTypeList(TagsQueryRequestDto request) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("count(value) as `size`,type_cate `name`,min(cate_private) as bePrivate");
        wrapper.where("type = {0} and type_cate is not null AND deleted=0",request.getType());
        if(!request.getCreateUser().equals("-1")){
           wrapper.and("create_user_id = {0}",request.getCreateUser());
        }
        wrapper.groupBy("type_cate");
        return this.selectList(wrapper);
    }

    public boolean changeTypeScope(String typeCate,int toPublic) {
        if (toPublic == 0) {//降级
            return 0 != wzCommonTagsMapper.updateToPrivate(typeCate);
        } else {//升级
            return 0 != wzCommonTagsMapper.updateToPublic(typeCate);
        }
    }

    public Page<CommonTags> selectTags(GetInnerTagsRequestDto request){
        Page page = new Page();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrentPage());
        Wrapper wrapper = new EntityWrapper();

        wrapper.where("type={0}",request.getType());
        if(request.getValue()!=null){
            wrapper.like("value",request.getValue());
        }
        if(request.getTypeCate()!=null){
            wrapper.like("type_cate",request.getTypeCate());
        }
        return selectPage(page,wrapper);
    }

    /**
     * 根据标标签名集获取标签id集
     * @param tagNames
     * @param type
     * @return
     */
    public List<CommonTags> selectTagsByNames(String[] tagNames,String type){
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id");
        wrapper.where("type={0}",type);
        wrapper.in("value",tagNames);
        return this.selectList(wrapper);
    }


    public CommonTags selectTagByName(String tagName){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("value={0}",tagName);
        return this.selectOne(wrapper);
    }
}
