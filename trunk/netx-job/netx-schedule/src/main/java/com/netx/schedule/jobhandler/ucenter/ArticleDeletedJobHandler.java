package com.netx.schedule.jobhandler.ucenter;

import com.netx.core.biz.model.ReturnT;
import com.netx.core.handler.IJobHandler;
import com.netx.core.handler.annotation.JobHandler;
import com.netx.core.log.XxlJobLogger;
import com.netx.ucenter.biz.user.ArticleAction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@JobHandler(value="ArticleDeletedJobHandler")
@Component
public class ArticleDeletedJobHandler extends IJobHandler {

    @Autowired
    private ArticleAction articleAction;

    @Override
    public ReturnT<String> execute(String articleId) throws Exception {
        XxlJobLogger.log(new Date()+"："+"定时删除24小时后未交押金的图文("+articleId+")启动");
        if(StringUtils.isNotBlank(articleId)){
            if(articleAction.deleteUnpaidArticle(articleId)){
                return SUCCESS;
            }
        }
        return FAIL;
    }
}
