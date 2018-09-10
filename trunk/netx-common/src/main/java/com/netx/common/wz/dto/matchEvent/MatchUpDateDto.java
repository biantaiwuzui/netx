package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

public class MatchUpDateDto {
    @ApiModelProperty("更新的入选者id")
    private String id;
    @ApiModelProperty("是否被选中")
    private Boolean isSelected;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
