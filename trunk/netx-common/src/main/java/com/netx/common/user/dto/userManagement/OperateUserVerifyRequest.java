package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ApiModel("审核用户认证内容")
public class OperateUserVerifyRequest {

    @NotBlank(message = "操作人的用户id不能为空")
    @ApiModelProperty("操作人的用户id")
    private String updateUserId;

    @NotBlank(message = "认证id不能为空")
    @ApiModelProperty("认证id")
    private String userVerifyId;

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @Max(value = 2, message = "认证状态值不在范围内")
    @Min(value = 1, message ="认证状态值不在范围内" )
    @ApiModelProperty("认证状态,值为：" +
            "1：认证通过\n" +
            "2：认证拒绝")
    private Integer status;

    @ApiModelProperty("原因")
    private String reason;

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUserVerifyId() {
        return userVerifyId;
    }

    public void setUserVerifyId(String userVerifyId) {
        this.userVerifyId = userVerifyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "OperateUserVerifyRequest{" +
                "updateUserId='" + updateUserId + '\'' +
                ", userVerifyId='" + userVerifyId + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
