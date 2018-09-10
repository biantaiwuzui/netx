package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.ArticleTagsMapper;
import com.netx.ucenter.model.user.ArticleTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  图文-标签表 服务实现类
 * </p>
 *
 * @author BXT
 * @since 2018-06-29
 */
@Service
public class ArticleTagsService extends ServiceImpl<ArticleTagsMapper,ArticleTags> {

    @Autowired
    ArticleTagsMapper articleTagsMapper;

    public ArticleTagsMapper getUserLoginHistoryMapper() {
        return articleTagsMapper;
    }

    public Boolean insertArticleTagsAll(String articleId,String tagsId,String userId) {
        ArticleTags articleTags = new ArticleTags();
        articleTags.setCreateUserId(userId);
        articleTags.setArticleId(articleId);
        articleTags.setTagsId(tagsId);
        articleTags.setUpdateUserId(userId);
        return this.insert(articleTags);
    }
}
