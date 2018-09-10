package com.netx.common.vo.common;

import com.netx.common.user.enums.ArticleTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 资讯后台管理获取待审核咨询分页列表dto
 * hj.Mao
 * 2017-11-13
 */
@ApiModel
public class SelectArticleLockListRequestDto extends PageRequestDto {

    @ApiModelProperty("资讯类型:①图文     \t②.音视")
    @NotNull(message = "资讯类型不能为空")
    private ArticleTypeEnum articleTypeEnum;

    public ArticleTypeEnum getArticleTypeEnum() {
        return articleTypeEnum;
    }

    public void setArticleTypeEnum(ArticleTypeEnum articleTypeEnum) {
        this.articleTypeEnum = articleTypeEnum;
    }
}
