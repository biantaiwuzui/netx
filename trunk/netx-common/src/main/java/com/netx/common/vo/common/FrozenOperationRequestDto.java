package com.netx.common.vo.common;

import com.netx.common.common.enums.FrozenTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-10-10
 */
@ApiModel
public class FrozenOperationRequestDto {
    @ApiModelProperty(value = "参与事件id,类似活动id或购买商品的订单id,与冻结时id一样", required = true)
    @NotBlank
    private String typeId;
    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank
    private String userId;
    @ApiModelProperty(value = "事件类型", required = true)
    @NotNull
    private FrozenTypeEnum type;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FrozenTypeEnum getType() {
        return type;
    }

    public void setType(FrozenTypeEnum type) {
        this.type = type;
    }
}
