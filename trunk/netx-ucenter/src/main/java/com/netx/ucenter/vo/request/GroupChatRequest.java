package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create on 17-11-14
 *
 * @author wongloong
 */
@ApiModel("群组用户")
public class GroupChatRequest {
    @ApiModelProperty(required = true, value = "用户id")
    @NotBlank
    private String userId;
    @ApiModelProperty(required = true, value = "群组id")
    @NotNull
    private Long groupId;

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
}
