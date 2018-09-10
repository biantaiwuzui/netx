package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class UserTypeRequest {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("类型")
    @NotBlank(message = "类型不能为空")
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
