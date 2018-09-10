package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class DeleteUserAdminRequestDto {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "userName")
    @NotBlank(message = "userName不能为空")
    private String userName;

    @ApiModelProperty(value = "禁用理由")
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DeleteUserAdminRequestDto{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
