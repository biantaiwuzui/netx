package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.model.user.ArticleCollect;
import com.netx.ucenter.service.user.ArticleCollectService;
import com.netx.ucenter.service.user.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleCollectAction {

    private Logger logger = LoggerFactory.getLogger(ArticleCollectAction.class);

    @Autowired
    private ArticleCollectService articleCollectService;

    @Autowired
    private ArticleService articleService;

    @Transactional(rollbackFor = Exception.class)
    public String addOnCencelCollect(String articleId,String userId){
        ArticleCollect articleCollect = articleCollectService.queryArticleCollect(userId,articleId);
        if(articleCollect==null){
            Article article = articleService.selectById(articleId);
            if(article==null){
                return "此图文可能已关闭";
            }
            if(article.equals(userId)){
                return "不能收藏自己的图文";
            }
            articleCollect = new ArticleCollect();
            articleCollect.setArticleId(articleId);
            articleCollect.setCollect(true);
            articleCollect.setUserId(userId);
            articleCollect.setCreateUserId(userId);
            return articleCollectService.insert(articleCollect)?null:"操作失败";
        }else{
            articleCollect.setCollect(!articleCollect.getCollect());
            articleCollect.setUserId(userId);
            return articleCollectService.updateById(articleCollect)?null:"操作失败";
        }
    }
}
