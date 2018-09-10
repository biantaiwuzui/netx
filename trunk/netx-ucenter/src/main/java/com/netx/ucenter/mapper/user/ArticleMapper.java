package com.netx.ucenter.mapper.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.common.user.dto.article.EditArticleRequestDto;
import com.netx.ucenter.model.user.Article;

/**
 * <p>
  * 资讯表（图文、音视） Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface ArticleMapper extends BaseMapper<Article> {

    public boolean updateArticleContent(EditArticleRequestDto dto);

}