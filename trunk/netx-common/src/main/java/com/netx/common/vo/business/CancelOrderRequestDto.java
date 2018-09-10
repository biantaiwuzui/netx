package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class CancelOrderRequestDto {
    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    @ApiModelProperty("商家userId或顾客的userId")
    private String userId;

    @ApiModelProperty("取消原因")
    @NotBlank(message = "取消原因不能为空")
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
