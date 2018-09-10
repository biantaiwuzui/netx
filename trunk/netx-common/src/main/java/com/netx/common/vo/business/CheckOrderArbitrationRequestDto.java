package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class CheckOrderArbitrationRequestDto {

    @ApiModelProperty("投诉的类型ID:例如如果type是订单的话,那typeId就是这个订单ID号,\n普通仲裁类型可不填,\n如果type填了，次字段必填")
    @NotBlank(message = "事件ID不能为空")
    private String typeId;

    @ApiModelProperty(value = "仲裁id")
    @NotBlank(message = "仲裁id不能为空")
    private String arbitrationId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getArbitrationId() {
        return arbitrationId;
    }

    public void setArbitrationId(String arbitrationId) {
        this.arbitrationId = arbitrationId;
    }
}
