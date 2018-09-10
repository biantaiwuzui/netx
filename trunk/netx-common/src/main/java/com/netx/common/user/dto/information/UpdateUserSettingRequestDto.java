package com.netx.common.user.dto.information;

import com.netx.common.user.enums.ArticleSettingEnum;
import com.netx.common.user.enums.GiftSettingEnum;
import com.netx.common.user.enums.InvitationSettingEnum;
import com.netx.common.user.enums.NearlySettingEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class UpdateUserSettingRequestDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("礼物设置")
    private GiftSettingEnum giftSettingEnum;

    @ApiModelProperty("邀请设置")
    private InvitationSettingEnum invitationSettingEnum;

    @ApiModelProperty("咨讯设置")
    private ArticleSettingEnum articleSettingEnum;

    @ApiModelProperty("附近设置")
    private NearlySettingEnum nearlySettingEnum;

    @ApiModelProperty("语音设置，false：关闭  true：开启")
    private Boolean voiceSetting;

    @ApiModelProperty("震动设置，false：关闭  true：开启")
    private Boolean shockSetting;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GiftSettingEnum getGiftSettingEnum() {
        return giftSettingEnum;
    }

    public void setGiftSettingEnum(GiftSettingEnum giftSettingEnum) {
        this.giftSettingEnum = giftSettingEnum;
    }

    public InvitationSettingEnum getInvitationSettingEnum() {
        return invitationSettingEnum;
    }

    public void setInvitationSettingEnum(InvitationSettingEnum invitationSettingEnum) {
        this.invitationSettingEnum = invitationSettingEnum;
    }

    public ArticleSettingEnum getArticleSettingEnum() {
        return articleSettingEnum;
    }

    public void setArticleSettingEnum(ArticleSettingEnum articleSettingEnum) {
        this.articleSettingEnum = articleSettingEnum;
    }

    public NearlySettingEnum getNearlySettingEnum() {
        return nearlySettingEnum;
    }

    public void setNearlySettingEnum(NearlySettingEnum nearlySettingEnum) {
        this.nearlySettingEnum = nearlySettingEnum;
    }

    public Boolean getVoiceSetting() {
        return voiceSetting;
    }

    public void setVoiceSetting(Boolean voiceSetting) {
        this.voiceSetting = voiceSetting;
    }

    public Boolean getShockSetting() {
        return shockSetting;
    }

    public void setShockSetting(Boolean shockSetting) {
        this.shockSetting = shockSetting;
    }
}
