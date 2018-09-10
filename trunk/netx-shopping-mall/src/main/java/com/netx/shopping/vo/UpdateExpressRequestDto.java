package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class UpdateExpressRequestDto {

    @ApiModelProperty("标识id")
    @NotBlank(message = "标识id不能为空")
    private String id;

    @ApiModelProperty("热门度")
    @NotNull(message = "热门度不能为空")
    private Integer hot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }
}
