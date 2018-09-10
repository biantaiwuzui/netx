package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 其他设置远程vo类
 */
public class WzCommonOtherSetResponseDto implements Serializable {

    @ApiModelProperty("需求活动技能限制类别，0人员名单，1限制条件")
    private Integer skillLimitType;

    @ApiModelProperty("需求活动技能限制分数")
    private Integer skillLimitPoint;

    @ApiModelProperty("需求活动技能限制允许用户id,String，逗号分隔")
    private String skillLimitUserIds;

    @ApiModelProperty("需求活动技能限制条件,0,信用值不低与skillLimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String skillLimitCondition;

    @ApiModelProperty("图文音视限制类别，0人员名单，1限制条件")
    private Integer picLimitType;

    @ApiModelProperty("图文音视限制分数")
    private Integer picLimitPoint;

    @ApiModelProperty("图文音视限制允许用户id,String，逗号分隔")
    private String picLimitUserIds;

    @ApiModelProperty("图文音视限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String picLimitCondition;

    @ApiModelProperty("心愿限制类别，0人员名单，1限制条件")
    private Integer wishLimitType;

    @ApiModelProperty("心愿限制分数")
    private Integer wishLimitPoint;

    @ApiModelProperty("心愿限制允许用户id,String，逗号分隔")
    private String wishLimitUserIds;

    @ApiModelProperty("心愿限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,4:推荐者与发布者同等资格，String类型,用逗号分隔")
    private String wishLimitCondition;

    @ApiModelProperty("注册商家限制类别，0人员名单，1限制条件")
    private Integer regMerchantLimitType;

    @ApiModelProperty("注册商家限制分数")
    private Integer regMerchantLimitPoint;

    @ApiModelProperty("注册商家制允许用户id,String，逗号分隔")
    private String regMerchantLimitUserIds;

    @ApiModelProperty("注册商家条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String regMerchantLimitCondition;

    @ApiModelProperty("发行网币限制类别，0人员名单，1限制条件")
    private Integer currencyLimitType;

    @ApiModelProperty("发行网币限制分数")
    private Integer currencyLimitPoint;

    @ApiModelProperty("发行网币允许用户id,String，逗号分隔")
    private String currencyLimitUserIds;

    @ApiModelProperty("发行网币条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证4:上月业绩不低于currencylimitMoney,5:上月发行网币余额小于currencyLimitTotal,6,达到了上期网币发型时的业绩增长承诺,String类型,用逗号分隔")
    private String currencyLimitCondition;

    @ApiModelProperty("发行网币上月业绩不低于")
    private Integer currencyLimitMoreThen;

    @ApiModelProperty("发行网币余额小于")
    private Integer currencyLimitBalance;

    @ApiModelProperty("允许接受礼物,邀请及被分享的限制类别，0人员名单，1限制条件")
    private Integer shareLimitType;

    @ApiModelProperty("允许接受礼物,邀请及被分享的限制分数")
    private Integer shareLimitPoint;

    @ApiModelProperty("允许接受礼物,邀请及被分享的限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String shareLimitCondition;

    @ApiModelProperty("备用1")
    private String bak1;

    @ApiModelProperty("备用2")
    private String bak2;

    @ApiModelProperty("备用3")
    private String bak3;

    @ApiModelProperty("备用4")
    private String bak4;

    @ApiModelProperty("备用5")
    private String bak5;

    public Integer getSkillLimitType() {
        return skillLimitType;
    }

    public void setSkillLimitType(Integer skillLimitType) {
        this.skillLimitType = skillLimitType;
    }

    public Integer getSkillLimitPoint() {
        return skillLimitPoint;
    }

    public void setSkillLimitPoint(Integer skillLimitPoint) {
        this.skillLimitPoint = skillLimitPoint;
    }

    public String getSkillLimitUserIds() {
        return skillLimitUserIds;
    }

    public void setSkillLimitUserIds(String skillLimitUserIds) {
        this.skillLimitUserIds = skillLimitUserIds;
    }

    public String getSkillLimitCondition() {
        return skillLimitCondition;
    }

    public void setSkillLimitCondition(String skillLimitCondition) {
        this.skillLimitCondition = skillLimitCondition;
    }

    public Integer getPicLimitType() {
        return picLimitType;
    }

    public void setPicLimitType(Integer picLimitType) {
        this.picLimitType = picLimitType;
    }

    public Integer getPicLimitPoint() {
        return picLimitPoint;
    }

    public void setPicLimitPoint(Integer picLimitPoint) {
        this.picLimitPoint = picLimitPoint;
    }

    public String getPicLimitUserIds() {
        return picLimitUserIds;
    }

    public void setPicLimitUserIds(String picLimitUserIds) {
        this.picLimitUserIds = picLimitUserIds;
    }

    public String getPicLimitCondition() {
        return picLimitCondition;
    }

    public void setPicLimitCondition(String picLimitCondition) {
        this.picLimitCondition = picLimitCondition;
    }

    public Integer getWishLimitType() {
        return wishLimitType;
    }

    public void setWishLimitType(Integer wishLimitType) {
        this.wishLimitType = wishLimitType;
    }

    public Integer getWishLimitPoint() {
        return wishLimitPoint;
    }

    public void setWishLimitPoint(Integer wishLimitPoint) {
        this.wishLimitPoint = wishLimitPoint;
    }

    public String getWishLimitUserIds() {
        return wishLimitUserIds;
    }

    public void setWishLimitUserIds(String wishLimitUserIds) {
        this.wishLimitUserIds = wishLimitUserIds;
    }

    public String getWishLimitCondition() {
        return wishLimitCondition;
    }

    public void setWishLimitCondition(String wishLimitCondition) {
        this.wishLimitCondition = wishLimitCondition;
    }

    public Integer getRegMerchantLimitType() {
        return regMerchantLimitType;
    }

    public void setRegMerchantLimitType(Integer regMerchantLimitType) {
        this.regMerchantLimitType = regMerchantLimitType;
    }

    public Integer getRegMerchantLimitPoint() {
        return regMerchantLimitPoint;
    }

    public void setRegMerchantLimitPoint(Integer regMerchantLimitPoint) {
        this.regMerchantLimitPoint = regMerchantLimitPoint;
    }

    public String getRegMerchantLimitUserIds() {
        return regMerchantLimitUserIds;
    }

    public void setRegMerchantLimitUserIds(String regMerchantLimitUserIds) {
        this.regMerchantLimitUserIds = regMerchantLimitUserIds;
    }

    public String getRegMerchantLimitCondition() {
        return regMerchantLimitCondition;
    }

    public void setRegMerchantLimitCondition(String regMerchantLimitCondition) {
        this.regMerchantLimitCondition = regMerchantLimitCondition;
    }

    public Integer getCurrencyLimitType() {
        return currencyLimitType;
    }

    public void setCurrencyLimitType(Integer currencyLimitType) {
        this.currencyLimitType = currencyLimitType;
    }

    public Integer getCurrencyLimitPoint() {
        return currencyLimitPoint;
    }

    public void setCurrencyLimitPoint(Integer currencyLimitPoint) {
        this.currencyLimitPoint = currencyLimitPoint;
    }

    public String getCurrencyLimitUserIds() {
        return currencyLimitUserIds;
    }

    public void setCurrencyLimitUserIds(String currencyLimitUserIds) {
        this.currencyLimitUserIds = currencyLimitUserIds;
    }

    public String getCurrencyLimitCondition() {
        return currencyLimitCondition;
    }

    public void setCurrencyLimitCondition(String currencyLimitCondition) {
        this.currencyLimitCondition = currencyLimitCondition;
    }

    public Integer getCurrencyLimitMoreThen() {
        return currencyLimitMoreThen;
    }

    public void setCurrencyLimitMoreThen(Integer currencyLimitMoreThen) {
        this.currencyLimitMoreThen = currencyLimitMoreThen;
    }

    public Integer getCurrencyLimitBalance() {
        return currencyLimitBalance;
    }

    public void setCurrencyLimitBalance(Integer currencyLimitBalance) {
        this.currencyLimitBalance = currencyLimitBalance;
    }

    public Integer getShareLimitType() {
        return shareLimitType;
    }

    public void setShareLimitType(Integer shareLimitType) {
        this.shareLimitType = shareLimitType;
    }

    public Integer getShareLimitPoint() {
        return shareLimitPoint;
    }

    public void setShareLimitPoint(Integer shareLimitPoint) {
        this.shareLimitPoint = shareLimitPoint;
    }

    public String getShareLimitCondition() {
        return shareLimitCondition;
    }

    public void setShareLimitCondition(String shareLimitCondition) {
        this.shareLimitCondition = shareLimitCondition;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4;
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5;
    }

    @Override
    public String toString() {
        return "WzCommonOtherSetResponseDto{" +
                "skillLimitType=" + skillLimitType +
                ", skillLimitPoint=" + skillLimitPoint +
                ", skillLimitUserIds='" + skillLimitUserIds + '\'' +
                ", skillLimitCondition='" + skillLimitCondition + '\'' +
                ", picLimitType=" + picLimitType +
                ", picLimitPoint=" + picLimitPoint +
                ", picLimitUserIds='" + picLimitUserIds + '\'' +
                ", picLimitCondition='" + picLimitCondition + '\'' +
                ", wishLimitType=" + wishLimitType +
                ", wishLimitPoint=" + wishLimitPoint +
                ", wishLimitUserIds='" + wishLimitUserIds + '\'' +
                ", wishLimitCondition='" + wishLimitCondition + '\'' +
                ", regMerchantLimitType=" + regMerchantLimitType +
                ", regMerchantLimitPoint=" + regMerchantLimitPoint +
                ", regMerchantLimitUserIds='" + regMerchantLimitUserIds + '\'' +
                ", regMerchantLimitCondition='" + regMerchantLimitCondition + '\'' +
                ", currencyLimitType=" + currencyLimitType +
                ", currencyLimitPoint=" + currencyLimitPoint +
                ", currencyLimitUserIds='" + currencyLimitUserIds + '\'' +
                ", currencyLimitCondition='" + currencyLimitCondition + '\'' +
                ", currencyLimitMoreThen=" + currencyLimitMoreThen +
                ", currencyLimitBalance=" + currencyLimitBalance +
                ", shareLimitType=" + shareLimitType +
                ", shareLimitPoint=" + shareLimitPoint +
                ", shareLimitCondition='" + shareLimitCondition + '\'' +
                ", bak1='" + bak1 + '\'' +
                ", bak2='" + bak2 + '\'' +
                ", bak3='" + bak3 + '\'' +
                ", bak4='" + bak4 + '\'' +
                ", bak5='" + bak5 + '\'' +
                '}';
    }
}
