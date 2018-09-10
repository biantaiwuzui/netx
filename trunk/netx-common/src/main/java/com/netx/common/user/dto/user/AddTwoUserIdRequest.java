package com.netx.common.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(value = "用户关系vo类")
public class AddTwoUserIdRequest {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "被建立关系目标id")
    @NotBlank(message = "被建立关系目标id不能为空")
    private String toUserId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String targetId) {
        this.toUserId = targetId;
    }

    @Override
    public String toString() {
        return "AddTwoUserIdRequest{" +
                "userId='" + userId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }
}
