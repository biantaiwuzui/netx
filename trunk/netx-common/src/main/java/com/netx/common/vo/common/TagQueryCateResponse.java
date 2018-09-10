package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Create on 17-11-16
 *
 * @author wongloong
 */
@ApiModel("标签项")
public class TagQueryCateResponse {
    private String name;
    private Long size;
    @ApiModelProperty("0公开1私有")
    private Integer bePrivate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getBePrivate() {
        return bePrivate;
    }

    public void setBePrivate(Integer bePrivate) {
        this.bePrivate = bePrivate;
    }
}
