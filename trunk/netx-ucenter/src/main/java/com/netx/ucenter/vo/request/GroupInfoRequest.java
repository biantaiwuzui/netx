package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class GroupInfoRequest {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("群id")
    @NotNull(message = "群id不能为空")
    private Long groupId;

    @ApiModelProperty("名字")
    @NotBlank(message = "修改的名字不能为空")
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
