package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class OrtherSetRequestDto {

    @ApiModelProperty("查询类型1.已审核 0.等待审核")
    @NotNull(message = "查询类型不能为空")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
