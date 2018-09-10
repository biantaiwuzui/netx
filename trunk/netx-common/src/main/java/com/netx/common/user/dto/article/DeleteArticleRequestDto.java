package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class DeleteArticleRequestDto {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
