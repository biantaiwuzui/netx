package com.netx.common.common.vo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-22
 */
@ApiModel
public class GroupDto {

    @ApiModelProperty(value = "用户Id")
    @NotBlank(message = "用户Id不能为空")
    private String userId;

    @ApiModelProperty(value = "群名")
    @NotBlank(message = "群名不能为空")
    private String groupName;

    @ApiModelProperty(value = "密码/标识")
    @NotBlank(message = "密码/标识不能为空")
    private String tag;

    @ApiModelProperty(value = "类型(面对面建群：faceCreate，用户建群：userCreate)")
    @NotBlank(message = "类型不能为空")
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
