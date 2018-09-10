package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@ApiModel
public class AuditExamineFinanceDto {


    @ApiModelProperty(value = "记录id", required = true)
    @NotNull
    private String id;
    @ApiModelProperty(value = "审批状态1通过2不通过", required = true)
    @NotNull
    private Integer status;
    @ApiModelProperty(value = "审批用户id", required = true)
    @NotBlank
    private String updateUser;
    @ApiModelProperty(value = "理由", required = true)
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}