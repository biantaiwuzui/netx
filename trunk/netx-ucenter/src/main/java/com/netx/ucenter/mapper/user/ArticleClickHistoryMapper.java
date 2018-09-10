package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.ArticleClickHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.ucenter.model.user.queryArticleClickHistoryCountData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;
import java.util.HashMap;



/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-04-19
 */
public interface ArticleClickHistoryMapper extends BaseMapper<ArticleClickHistory> {

    public List<queryArticleClickHistoryCountData> queryArticleClickHistoryCount();
}