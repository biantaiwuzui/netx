package com.netx.common.wz.dto.common;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CommonPageDto {
    @ApiModelProperty(value = "业务ID")
    private String id;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "当前页")
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示数量")
    @NotNull(message = "每页显示数量不能为空")
    @Max(value = 99999, message = "每页显示数量最多为50")
    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
