package com.netx.common.user.dto.article;

import com.netx.common.user.enums.ArticleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class SelectArticleCountRequestDto {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("是否查询软文")
    private Boolean isArticleType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsArticleType() {
        return isArticleType;
    }

    public void setIsArticleType(Boolean isArticleType) {
        this.isArticleType = isArticleType;
    }
}
