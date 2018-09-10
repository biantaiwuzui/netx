package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * hj.Mao
 * 2017-11-4
 */
@ApiModel
public class ArticleCommandUpdateRequestDto {

    @ApiModelProperty("咨询ID数组")
    @NotEmpty(message = "咨询ID数组不能为空")
    private List<String> articleIds;

    public List<String> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }
}
