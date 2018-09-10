package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class QueryManagerRequestDto {

    @ApiModelProperty("主管id")
    @NotBlank(message = "主管id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
