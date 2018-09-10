package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-8-25
 */
@ApiModel
public class WzCommonAddFriendAddRequestDto {
    @ApiModelProperty("发送人id")
    @NotBlank
    private String fromUserId;
    @ApiModelProperty("接受人id")
    @NotBlank
    private String toUserId;
    @NotBlank
    @ApiModelProperty("发送人名字")
    private String fromUserName;
    @NotBlank
    @ApiModelProperty("接受人名字")
    private String toUserName;
    @ApiModelProperty("接受人网号")
    private String toUserNumber;
    @ApiModelProperty("发送人网号")
    private String fromUserNumber;

    public String getToUserNumber() {
        return toUserNumber;
    }

    public void setToUserNumber(String toUserNumber) {
        this.toUserNumber = toUserNumber;
    }

    public String getFromUserNumber() {
        return fromUserNumber;
    }

    public void setFromUserNumber(String fromUserNumber) {
        this.fromUserNumber = fromUserNumber;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    @Override
    public String toString() {
        return "AddWzCommonAddFriendRequest{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", toUserName='" + toUserName + '\'' +
                '}';
    }
}
