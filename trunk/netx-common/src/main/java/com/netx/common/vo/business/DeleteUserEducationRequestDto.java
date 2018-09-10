package com.netx.common.vo.business;

import org.hibernate.validator.constraints.NotBlank;

import javax.crypto.Mac;
import javax.validation.constraints.NotNull;

public class DeleteUserEducationRequestDto {

    @NotBlank(message = "教育背景id不能空")
    private String id;
    @NotBlank(message = "管理员姓名不能为空")
    private String updateUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
