package com.netx.shopping.vo;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

public class QueryCategoryRequestDto extends PageRequestDto{

    @ApiModelProperty("父标签id")
    private String parentId;

    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("标签状态")
    private Integer deleted;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "QueryCategoryRequestDto{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
