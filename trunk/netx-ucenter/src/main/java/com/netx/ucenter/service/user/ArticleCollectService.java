package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.ArticleCollectMapper;
import com.netx.ucenter.model.user.ArticleCollect;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCollectService extends ServiceImpl<ArticleCollectMapper, ArticleCollect> {

    public boolean deleteCollectByUserId(String userId,List<String> articleIds){
        Wrapper<ArticleCollect> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0");
        if(articleIds!=null && articleIds.size()>0){
            wrapper.in("article_id",articleIds);
        }
        wrapper.orNew("user_id={0}",userId);
        return delete(wrapper);
    }

    public ArticleCollect queryArticleCollect(String userId,String articleId){
        Wrapper<ArticleCollect> wrapper = new EntityWrapper<>();
        wrapper.where("article_id = {0} and user_id = {1}",articleId,userId);
        return selectOne(wrapper);
    }

    public List<Object> selectArticleIdByUserId(String userId) throws Exception{
        Wrapper<ArticleCollect> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("article_id").where("user_id = {0} and is_collect = {1}", userId, true);
        return selectObjs(wrapper);
    }

    public boolean checkCollect(String articleId,String userId){
        Wrapper<ArticleCollect> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("is_collect");
        wrapper.where("user_id = {0} and article_id = {1}",userId,articleId);
        Boolean flag = (Boolean) selectObj(wrapper);
        return flag==null?false:flag;
    }

    public int countNum(String articleId,Boolean isCollect){
        Wrapper<ArticleCollect> wrapper = new EntityWrapper<>();
        wrapper.where("article_id = {0}",articleId);
        if(isCollect!=null){
            wrapper.and("is_collect = {0}",isCollect);
        }
        return selectCount(wrapper);
    }
}
