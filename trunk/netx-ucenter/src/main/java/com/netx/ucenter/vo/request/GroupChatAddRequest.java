package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create on 17-11-14
 *
 * @author wongloong
 */
@ApiModel
public class GroupChatAddRequest {
    @ApiModelProperty(value = "群组id", required = true)
    @NotNull
    private Long groupId;
    @ApiModelProperty(value = "群组名称")
    private String groupName;
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull
    private String userId;
    @ApiModelProperty(value = "是否是管理员", required = true)
    @NotNull
    private Integer admin;
    @ApiModelProperty(value = "群密码")
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

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

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }
}
