package com.netx.common.common.vo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-22
 */
@ApiModel
public class GroupMembersMultipleDto {

    @ApiModelProperty(value = "用户Id")
    @NotEmpty
    private String[] userId;

    @ApiModelProperty(value = "组")
    @NotNull
    private Long groupId;

    public String[] getUserId() {
        return userId;
    }

    public void setUserId(String[] userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
