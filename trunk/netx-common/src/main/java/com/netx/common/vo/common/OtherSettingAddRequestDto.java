package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Create by wongloong on 17-9-6
 */
@ApiModel
public class OtherSettingAddRequestDto {
    @ApiModelProperty(notes = "是否已审核")
    private Integer canUse;
    @ApiModelProperty(notes = "审核人id")
    private String disposeUserId;

    @ApiModelProperty("1超级管理员，2普通管理员")
    private Integer type;

    private Long createTime;

    private Long updateTime;

    private String createUser;

    private String updateUser;

    private Integer delTag;
    @ApiModelProperty(notes = "需求活动技能限制类别，0人员名单，1限制条件")
    private Integer skillLimitType;

    @ApiModelProperty(notes = "需求活动技能限制分数")
    private Integer skillLimitPoint;

    @ApiModelProperty(notes = "需求活动技能允许用户id,String，逗号分隔")
    private String skillLimitUserIds;

    @ApiModelProperty(notes = "需求活动技能限制条件,0,信用值不低与skillLimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String skillLimitCondition;

    @ApiModelProperty(notes = "图文音视限制类别，0人员名单，1限制条件")
    private Integer picLimitType;
    @ApiModelProperty(notes = "图文音视限制分数")
    private Integer picLimitPoint;
    @ApiModelProperty(notes = "图文音视允许用户id,String，逗号分隔")
    private String picLimitUserIds;
    @ApiModelProperty(notes = "图文音视0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String picLimitCondition;

    @ApiModelProperty(notes = "心愿限制类别，0人员名单，1限制条件")
    private Integer wishLimitType;
    @ApiModelProperty(notes = "心愿限制分数")
    private Integer wishLimitPoint;
    @ApiModelProperty(notes = "心愿允许用户id,String，逗号分隔")
    private String wishLimitUserIds;
    @ApiModelProperty(notes = "心愿,0:信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,4:推荐者与发布者同等资格，String类型,用逗号分隔")
    private String wishLimitCondition;

    @ApiModelProperty(notes = "商家注册限制类别，0人员名单，1限制条件")
    private Integer regMerchantLimitType;
    @ApiModelProperty(notes = "商家注册限制分数")
    private Integer regMerchantLimitPoint;
    @ApiModelProperty(notes = "商家注册允许用户id,String，逗号分隔")
    private String regMerchantLimitUserIds;
    @ApiModelProperty(notes = "商家注册限制条件0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String regMerchantLimitCondition;

    @ApiModelProperty(notes = "发行网币注册限制类别，0人员名单，1限制条件")
    private Integer creditLimitType;

    @ApiModelProperty(notes = "发行网币限制分数")
    private Integer creditLimitPoint;

    @ApiModelProperty(notes = "发行网币允许用户id,String，逗号分隔")
    private String creditLimitUserIds;
    @ApiModelProperty(notes = "发行网币条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证4:上月业绩不低于creditLimitMoreThen5:上月发行网币余额小于creditLimitBalance6,达到了上期网币发型时的业绩增长承诺,String类型,用逗号分隔")
    private String creditLimitCondition;
    @ApiModelProperty(notes = "上月业绩不低于")
    private Integer creditLimitMoreThen;
    @ApiModelProperty(notes = "上月发行网币小于")
    private Integer creditLimitBalance;

    @ApiModelProperty(notes = "允许接受礼物,邀请及被分享的限制类别，限制类别，0人员名单，1限制条件")
    private Integer shareLimitType;
    @ApiModelProperty(notes = "允许接受礼物限制分数")
    private Integer shareLimitPoint;
    @ApiModelProperty(notes = "允许接受礼物条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔")
    private String shareLimitCondition;

    private String bak1;

    private String bak2;

    private String bak3;

    private String bak4;

    private String bak5;

    public Integer getCanUse() {
        return canUse;
    }

    public void setCanUse(Integer canUse) {
        this.canUse = canUse;
    }

    public String getDisposeUserId() {
        return disposeUserId;
    }

    public void setDisposeUserId(String disposeUserId) {
        this.disposeUserId = disposeUserId == null ? null : disposeUserId.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getDelTag() {
        return delTag;
    }

    public void setDelTag(Integer delTag) {
        this.delTag = delTag;
    }

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
        this.skillLimitUserIds = skillLimitUserIds == null ? null : skillLimitUserIds.trim();
    }

    public String getSkillLimitCondition() {
        return skillLimitCondition;
    }

    public void setSkillLimitCondition(String skillLimitCondition) {
        this.skillLimitCondition = skillLimitCondition == null ? null : skillLimitCondition.trim();
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
        this.picLimitUserIds = picLimitUserIds == null ? null : picLimitUserIds.trim();
    }

    public String getPicLimitCondition() {
        return picLimitCondition;
    }

    public void setPicLimitCondition(String picLimitCondition) {
        this.picLimitCondition = picLimitCondition == null ? null : picLimitCondition.trim();
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
        this.wishLimitUserIds = wishLimitUserIds == null ? null : wishLimitUserIds.trim();
    }

    public String getWishLimitCondition() {
        return wishLimitCondition;
    }

    public void setWishLimitCondition(String wishLimitCondition) {
        this.wishLimitCondition = wishLimitCondition == null ? null : wishLimitCondition.trim();
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
        this.regMerchantLimitUserIds = regMerchantLimitUserIds == null ? null : regMerchantLimitUserIds.trim();
    }

    public String getRegMerchantLimitCondition() {
        return regMerchantLimitCondition;
    }

    public void setRegMerchantLimitCondition(String regMerchantLimitCondition) {
        this.regMerchantLimitCondition = regMerchantLimitCondition == null ? null : regMerchantLimitCondition.trim();
    }

    public Integer getCreditLimitType() {
        return creditLimitType;
    }

    public void setCreditLimitType(Integer creditLimitType) {
        this.creditLimitType = creditLimitType;
    }

    public Integer getCreditLimitPoint() {
        return creditLimitPoint;
    }

    public void setCreditLimitPoint(Integer creditLimitPoint) {
        this.creditLimitPoint = creditLimitPoint;
    }

    public String getCreditLimitUserIds() {
        return creditLimitUserIds;
    }

    public void setCreditLimitUserIds(String creditLimitUserIds) {
        this.creditLimitUserIds = creditLimitUserIds == null ? null : creditLimitUserIds.trim();
    }

    public String getCreditLimitCondition() {
        return creditLimitCondition;
    }

    public void setCreditLimitCondition(String creditLimitCondition) {
        this.creditLimitCondition = creditLimitCondition == null ? null : creditLimitCondition.trim();
    }

    public Integer getCreditLimitMoreThen() {
        return creditLimitMoreThen;
    }

    public void setCreditLimitMoreThen(Integer creditLimitMoreThen) {
        this.creditLimitMoreThen = creditLimitMoreThen;
    }

    public Integer getCreditLimitBalance() {
        return creditLimitBalance;
    }

    public void setCreditLimitBalance(Integer creditLimitBalance) {
        this.creditLimitBalance = creditLimitBalance;
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
        this.shareLimitCondition = shareLimitCondition == null ? null : shareLimitCondition.trim();
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4 == null ? null : bak4.trim();
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5 == null ? null : bak5.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
