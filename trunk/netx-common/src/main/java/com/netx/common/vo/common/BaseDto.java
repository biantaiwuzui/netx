package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Create on 17-11-5
 *
 * @author wongloong
 */
@ApiModel
public class BaseDto {
    @ApiModelProperty(required = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
