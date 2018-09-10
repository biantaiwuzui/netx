package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class OperateSystemBlacklistRequestDto {

    @ApiModelProperty("被操作的用户id")
    @NotBlank
    private String userId;

    @ApiModelProperty("管理员的用户昵称")
    private String createUserName;

    @ApiModelProperty("拉黑或者解除的原因")
    @NotBlank
    private String reason;

    @ApiModelProperty("正在操作的类型:0.移除黑名单  1.加入黑名单")
    @NotNull
    private Integer operateType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
}

