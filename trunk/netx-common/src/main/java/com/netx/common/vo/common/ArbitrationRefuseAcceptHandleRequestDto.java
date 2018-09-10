package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class ArbitrationRefuseAcceptHandleRequestDto {

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty("仲裁管理表对应ID")
    private String id;

    @NotBlank(message = "操作者ID不能为空")
    @ApiModelProperty("操作者ID")
    private String opUserId;


    @ApiModelProperty("拒绝描述")
    @NotBlank(message = "拒绝描述不能为空")
    private String descriptions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
