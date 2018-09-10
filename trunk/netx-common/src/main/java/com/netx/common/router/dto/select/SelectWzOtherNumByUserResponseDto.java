package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 跨模块使用的保留礼物数量、邀请数量类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectWzOtherNumByUserResponseDto {
    @ApiModelProperty("礼物数量")
    private int giftNum;

    @ApiModelProperty("邀请数量")
    private int invitationNum;

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getInvitationNum() {
        return invitationNum;
    }

    public void setInvitationNum(int invitationNum) {
        this.invitationNum = invitationNum;
    }
}
