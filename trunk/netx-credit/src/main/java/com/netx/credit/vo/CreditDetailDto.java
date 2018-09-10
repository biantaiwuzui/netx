package com.netx.credit.vo;

import com.netx.credit.model.CreditDividendSetting;
import com.netx.credit.model.CreditLevelDiscountSetting;
import com.netx.credit.model.CreditScope;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


public class CreditDetailDto {

    @ApiModelProperty(value = "网信Id")
    private String id;

    @ApiModelProperty(value = "网信图标")
    private String pictureUrl;

    @ApiModelProperty(value = "网信名称")
    private String name;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "商家Id")
    private String merchantId;

    @ApiModelProperty(value = "发行适用范围")
    private CreditScope creditScope;

    /**
     * 网信分类标签id 和名称
     */
    private List<CreditCategoryVo> creditCategoryVos;

    @ApiModelProperty(value = "网信标签")
    private List<String> tagIds;

    @ApiModelProperty(value = "分类")
    private List<String> categoryId;

    @ApiModelProperty(value = "网信单价")
    private Integer creditPrice;

    @ApiModelProperty(value = "已售数额")
    private Double sellAmount;

    @ApiModelProperty(value = "预售上限")
    private Integer presaleUpperLimit;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "总共金额")
    private Integer totalNumber;

    @ApiModelProperty(value = "发行份数")
    private Integer issueNumber;

    @ApiModelProperty(value = "收银员Id")
    private String cashierId;

    @ApiModelProperty(value = "内购期限")
    private Long innerPurchaseDate;

    @ApiModelProperty(value = "保底交易额")
    private Integer ensureDeal;

    @ApiModelProperty(value = "活动数量")
    private Integer meetingNumber;

    @ApiModelProperty(value = "技能数量")
    private Integer skillNumber;

    @ApiModelProperty(value = "音视数量")
    private Integer audioVisualNumber;

    @ApiModelProperty(value = "需求数量")
    private Integer demandNumber;

    @ApiModelProperty(value = "图文数量")
    private Integer graphicNumber;

    @ApiModelProperty(value = "商家数量")
    private Integer merchantNumber;

    @ApiModelProperty(value = "申购折扣")
    private List<CreditLevelDiscountSetting> creditLevelDiscountSettings;

    @ApiModelProperty(value = "网信分红")
    private List<CreditDividendSetting> creditDividendSettings;

    @ApiModelProperty(value = "邀请好友列表")
    private List<CreditInviteFriendsVo> creditInviteFriendsVos;

    @ApiModelProperty(value = "内部认购")
    private List<CreditInnerDetailVo> creditInnerDetailVos;

    @ApiModelProperty(value = "发布者详情")
    private CreditPublisherInformationDto creditPublisherInformationDto;

    @ApiModelProperty(value = "回滚到编辑网信信息（预售者点击编辑网信用)")
    private PublishCreditRequestDto publishCreditRequestDto;

    @ApiModelProperty(value = "持有者列表(开放认购详情用)")
    private List<CreditHolderVo> creditHolderVos;

    /**
     * 网信状态
     * 0.正在预售
     * 1.开放认购
     * 2.不在认购
     */
    private int creditStatus;


    /**
     * 用户认购状态
     * 认购状态(0.未响应, 1.认购成功, 2.拒绝内购, 3.非内购人员)
     */

    private int userCreditStatus;

    // 0.发布者 1.商家内购人员或内购好友 2.用户好友 3.普通用户
    @ApiModelProperty(value = "用户身份")
    private Integer userType;

    /**
     * 发行日期
     */
    private Long createTime;

    public CreditPublisherInformationDto getCreditPublisherInformationDto() {
        return creditPublisherInformationDto;
    }

    public void setCreditPublisherInformationDto(CreditPublisherInformationDto creditPublisherInformationDto) {
        this.creditPublisherInformationDto = creditPublisherInformationDto;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPresaleUpperLimit() {
        return presaleUpperLimit;
    }

    public void setPresaleUpperLimit(Integer presaleUpperLimit) {
        this.presaleUpperLimit = presaleUpperLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public Integer getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(Integer meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Integer getSkillNumber() {
        return skillNumber;
    }

    public void setSkillNumber(Integer skillNumber) {
        this.skillNumber = skillNumber;
    }

    public Integer getAudioVisualNumber() {
        return audioVisualNumber;
    }

    public void setAudioVisualNumber(Integer audioVisualNumber) {
        this.audioVisualNumber = audioVisualNumber;
    }

    public Integer getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(Integer demandNumber) {
        this.demandNumber = demandNumber;
    }

    public Integer getGraphicNumber() {
        return graphicNumber;
    }

    public void setGraphicNumber(Integer graphicNumber) {
        this.graphicNumber = graphicNumber;
    }

    public Integer getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(Integer merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(Integer creditPrice) {
        this.creditPrice = creditPrice;
    }

    public Double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(Double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public List<CreditLevelDiscountSetting> getCreditLevelDiscountSettings() {
        return creditLevelDiscountSettings;
    }

    public void setCreditLevelDiscountSettings(List<CreditLevelDiscountSetting> creditLevelDiscountSettings) {
        this.creditLevelDiscountSettings = creditLevelDiscountSettings;
    }

    public List<CreditDividendSetting> getCreditDividendSettings() {
        return creditDividendSettings;
    }

    public void setCreditDividendSettings(List<CreditDividendSetting> creditDividendSettings) {
        this.creditDividendSettings = creditDividendSettings;
    }

    public Long getInnerPurchaseDate() {
        return innerPurchaseDate;
    }

    public void setInnerPurchaseDate(Long innerPurchaseDate) {
        this.innerPurchaseDate = innerPurchaseDate;
    }

    public Integer getEnsureDeal() {
        return ensureDeal;
    }

    public void setEnsureDeal(Integer ensureDeal) {
        this.ensureDeal = ensureDeal;
    }

    public CreditScope getScope() {
        return creditScope;
    }

    public void setCreditScope(CreditScope creditScope) {
        this.creditScope = creditScope;
    }

    public CreditScope getCreditScope() {
        return creditScope;
    }

    public List<CreditInviteFriendsVo> getCreditInviteFriendsVos() {
        return creditInviteFriendsVos;
    }

    public void setCreditInviteFriendsVos(List<CreditInviteFriendsVo> creditInviteFriendsVos) {
        this.creditInviteFriendsVos = creditInviteFriendsVos;
    }

    public List<CreditInnerDetailVo> getCreditInnerDetailVos() {
        return creditInnerDetailVos;
    }

    public void setCreditInnerDetailVos(List<CreditInnerDetailVo> creditInnerDetailVos) {
        this.creditInnerDetailVos = creditInnerDetailVos;
    }

    public PublishCreditRequestDto getPublishCreditRequestDto() {
        return publishCreditRequestDto;
    }

    public void setPublishCreditRequestDto(PublishCreditRequestDto publishCreditRequestDto) {
        this.publishCreditRequestDto = publishCreditRequestDto;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<CreditHolderVo> getCreditHolderVos() {
        return creditHolderVos;
    }

    public void setCreditHolderVos(List<CreditHolderVo> creditHolderVos) {
        this.creditHolderVos = creditHolderVos;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public List<CreditCategoryVo> getCreditCategoryVos() {
        return creditCategoryVos;
    }

    public void setCreditCategoryVos(List<CreditCategoryVo> creditCategoryVos) {
        this.creditCategoryVos = creditCategoryVos;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(int creditStatus) {
        this.creditStatus = creditStatus;
    }

    public int getUserCreditStatus() {
        return userCreditStatus;
    }

    public void setUserCreditStatus(int userCreditStatus) {
        this.userCreditStatus = userCreditStatus;
    }
}
