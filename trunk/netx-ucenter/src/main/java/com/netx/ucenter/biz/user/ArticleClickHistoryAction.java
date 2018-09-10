package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.ArticleClickHistory;
import com.netx.ucenter.model.user.queryArticleClickHistoryCountData;
import com.netx.ucenter.service.user.ArticleClickHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ArticleClickHistoryAction {

    @Autowired
    private ArticleClickHistoryService articleClickHistoryService;


    public ArticleClickHistoryService getArticleClickHistoryService() {
        return articleClickHistoryService;
    }

    public Boolean addArticleClickHistory(String articleId, String userId){
        ArticleClickHistory articleClickHistory = articleClickHistoryService.getClickHistoryByUserIdAndArticleId(articleId, userId);
        if(articleClickHistory==null){
            articleClickHistory = new ArticleClickHistory();
            articleClickHistory.setArticleId(articleId);
            articleClickHistory.setUserId(userId);
            articleClickHistory.setCreateUserId(userId);
            return articleClickHistoryService.insert(articleClickHistory);
        }
        return false;
    }

    public int getClickCount(String articleId){
        return articleClickHistoryService.getClickCountByArticleId(articleId);
    }

    public List<queryArticleClickHistoryCountData> queryArticleClickHistoryCount(){
        return articleClickHistoryService.queryArticleClickHistoryCount();
    }

}
