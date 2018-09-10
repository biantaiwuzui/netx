package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class FrozenQueryRequestDto extends PageRequestDto {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "事件id")
    private String typeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
