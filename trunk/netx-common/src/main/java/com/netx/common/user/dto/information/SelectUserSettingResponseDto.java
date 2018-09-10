package com.netx.common.user.dto.information;

import io.swagger.annotations.ApiModelProperty;

public class SelectUserSettingResponseDto {

    @ApiModelProperty("礼物设置，0:不接受礼物  1：接受好友礼物  2：接受我关注的人的礼物")
    private Integer giftSetting;

    @ApiModelProperty("邀请设置，0：不接受邀请  1：接受好友的邀请  2：接受我关注的人的邀请")
    private Integer invitationSetting;

    @ApiModelProperty("咨讯设置，0：不起任何作用  1：仅限好友查看")
    private Integer articleSetting;

    @ApiModelProperty("附近设置，0：显示我的信息  1：不显示我的信息")
    private Integer nearlySetting;

    @ApiModelProperty("语音设置，0：关闭  1：开启")
    private Integer voiceSetting;

    @ApiModelProperty("震动设置，0：关闭  1：开启")
    private Integer shockSetting;

    public Integer getGiftSetting() {
        return giftSetting;
    }

    public void setGiftSetting(Integer giftSetting) {
        this.giftSetting = giftSetting;
    }

    public Integer getInvitationSetting() {
        return invitationSetting;
    }

    public void setInvitationSetting(Integer invitationSetting) {
        this.invitationSetting = invitationSetting;
    }

    public Integer getArticleSetting() {
        return articleSetting;
    }

    public void setArticleSetting(Integer articleSetting) {
        this.articleSetting = articleSetting;
    }

    public Integer getNearlySetting() {
        return nearlySetting;
    }

    public void setNearlySetting(Integer nearlySetting) {
        this.nearlySetting = nearlySetting;
    }

    public Integer getVoiceSetting() {
        return voiceSetting;
    }

    public void setVoiceSetting(Integer voiceSetting) {
        this.voiceSetting = voiceSetting;
    }

    public Integer getShockSetting() {
        return shockSetting;
    }

    public void setShockSetting(Integer shockSetting) {
        this.shockSetting = shockSetting;
    }

    @Override
    public String toString() {
        return "SelectUserSettingResponseDto{" +
                "giftSetting=" + giftSetting +
                ", invitationSetting=" + invitationSetting +
                ", articleSetting=" + articleSetting +
                ", nearlySetting=" + nearlySetting +
                ", voiceSetting=" + voiceSetting +
                ", shockSetting=" + shockSetting +
                '}';
    }
}
