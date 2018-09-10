package com.netx.common.common.vo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-22
 */
@ApiModel
public class GroupInfoDto {

    @ApiModelProperty(value = "群组Id")
    @NotNull
    private Long groupId;

    @ApiModelProperty(value = "群名")
    private String groupName;

    @ApiModelProperty(value = "管理员id")
    private String userId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
