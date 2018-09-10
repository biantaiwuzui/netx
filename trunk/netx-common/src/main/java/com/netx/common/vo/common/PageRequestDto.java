package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-26
 */
@ApiModel
public class PageRequestDto {
    @ApiModelProperty(value = "每页记录数", required = true, example = "10")
    @NotNull(message = "分页属性不能为空")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull(message = "分页属性不能为空")
    private Integer current = 1;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size.intValue() > 0) {
            this.size = size;
        }
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current.intValue() > 0) {
            this.current = current;
        }
    }
}
