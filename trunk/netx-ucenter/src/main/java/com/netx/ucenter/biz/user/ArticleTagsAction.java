package com.netx.ucenter.biz.user;

import com.netx.ucenter.biz.common.TagsAction;
import com.netx.ucenter.model.common.CommonTags;
import com.netx.ucenter.service.user.ArticleTagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListResourceBundle;

/**
 * 图文
 * BXT
 */
@Service
public class ArticleTagsAction {

    private Logger logger= LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    ArticleTagsService articleTagsService;
    @Autowired
    TagsAction tagsAction;

    public Boolean insertArticleTagsAll(String articleId,String tagsId,String userId){
        return articleTagsService.insertArticleTagsAll(articleId,tagsId,userId);
    }

    public List<CommonTags> queryArticleTags(String[] tagNames, String type){
        return tagsAction.getTagsService().selectTagsByNames(tagNames,type);
    }
}
