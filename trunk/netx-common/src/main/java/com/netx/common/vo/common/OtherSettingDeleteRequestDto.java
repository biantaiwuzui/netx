package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * by lcx
 * */
@ApiModel
public class OtherSettingDeleteRequestDto {
    @ApiModelProperty(notes = "id")
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
