package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class FriendsPageRequestDto {
    @ApiModelProperty(value = "每页记录数", required = true, example = "10")
    @NotNull(message = "分页属性不能为空")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull(message = "分页属性不能为空")
    private Integer current = 1;
    @ApiModelProperty("是否好友认证")
    private boolean friendVerify;
    @ApiModelProperty("网信值取值范围")
    private int credit;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size.intValue() > 0) {
            this.size = size;
        }
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current.intValue() > 0) {
            this.current = current;
        }
    }

    public boolean isFriendVerify() {
        return friendVerify;
    }

    public void setFriendVerify(boolean friendVerify) {
        this.friendVerify = friendVerify;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}