package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(description = "红包设置查看Dto")
public class SelectPacketSetRequestDto {
    @ApiModelProperty("红包ID")
    @NotBlank(message = "红包不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
