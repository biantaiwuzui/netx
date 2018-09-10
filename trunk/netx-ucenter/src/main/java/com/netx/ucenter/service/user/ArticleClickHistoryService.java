package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.ArticleClickHistoryMapper;
import com.netx.ucenter.model.user.ArticleClickHistory;
import com.netx.ucenter.model.user.queryArticleClickHistoryCountData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ArticleClickHistoryService extends ServiceImpl<ArticleClickHistoryMapper,ArticleClickHistory> {

    @Autowired
    ArticleClickHistoryMapper articleClickHistoryMapper;

    public ArticleClickHistory getClickHistoryByUserIdAndArticleId(String articleId,String userId){
        Wrapper<ArticleClickHistory> wrapper = new EntityWrapper<>();
        wrapper.where("article_id={0} and user_id={1}",articleId,userId);
        return selectOne(wrapper);
    }

    public int getClickCountByArticleId(String articleId){
        Wrapper<ArticleClickHistory> wrapper = new EntityWrapper<>();
        wrapper.where("article_id={0}",articleId);
        return selectCount(wrapper);
    }

    public int getClickCountByArticleId(List<String> articleId){
        Wrapper<ArticleClickHistory> wrapper = new EntityWrapper<>();
        wrapper.in("article_id",articleId);
        return selectCount(wrapper);
    }

    public boolean deleteClickHistory(String userId,List<String> articlesId){
        Wrapper<ArticleClickHistory> wrapper = new EntityWrapper<>();
        if(articlesId!=null && articlesId.size()>0){
            wrapper.in("article_id",articlesId);
        }
        wrapper.orNew("user_id={0}",userId);
        return delete(wrapper);
    }
    public List<queryArticleClickHistoryCountData> queryArticleClickHistoryCount(){
            return articleClickHistoryMapper.queryArticleClickHistoryCount();
    }


}
