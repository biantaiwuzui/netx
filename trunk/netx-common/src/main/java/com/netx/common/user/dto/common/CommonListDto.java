package com.netx.common.user.dto.common;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

public class CommonListDto {

    @ApiModelProperty(value = "当前页")
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示数量")
    @NotNull(message = "每页显示数量不能为空")
    @Max(value = 50, message = "每页显示数量最多为50")
    private Integer size;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
