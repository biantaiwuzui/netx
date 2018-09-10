package com.netx.ucenter.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户其他信息统计数量Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
@ApiModel("用户其他信息统计")
public class SelectUserOtherNumResponse {
    @ApiModelProperty("图文数量")
    private int imageWriteNum;

    @ApiModelProperty("音视数量")
    private int vedioNum;

    @ApiModelProperty("礼物数量")
    private int giftNum;

    @ApiModelProperty("邀请数量")
    private int invitationNum;

    public int getImageWriteNum() {
        return imageWriteNum;
    }

    public void setImageWriteNum(int imageWriteNum) {
        this.imageWriteNum = imageWriteNum;
    }

    public int getVedioNum() {
        return vedioNum;
    }

    public void setVedioNum(int vedioNum) {
        this.vedioNum = vedioNum;
    }

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

    @Override
    public String toString() {
        return "SelectUserOtherNumResponse{" +
                "imageWriteNum=" + imageWriteNum +
                ", vedioNum=" + vedioNum +
                ", giftNum=" + giftNum +
                ", invitationNum=" + invitationNum +
                '}';
    }
}
