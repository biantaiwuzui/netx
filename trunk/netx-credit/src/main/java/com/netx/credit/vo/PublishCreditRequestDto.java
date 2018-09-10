package com.netx.credit.vo;

import com.netx.credit.model.CreditDividendSetting;
import com.netx.credit.model.CreditLevelDiscountSetting;
import com.netx.credit.model.CreditScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("发行网信")
public class PublishCreditRequestDto {

    @ApiModelProperty("网信id(编辑网信")
    private String creditId;

    @ApiModelProperty("网信图标")
    private String pictureUrl;

    @NotBlank(message = "网信名称不能为空")
    @ApiModelProperty("网信名称")
    @Length(min = 3, max = 10,message = "网信名称不能小于3或者大于10")
    private String name;

    @ApiModelProperty("网信类别id")
    private List<String> categoryId;

    @ApiModelProperty("网信标签")
    private List<String> tagIds;

    @NotBlank(message = "网信商家ID不能为空")
    @ApiModelProperty("网信商家ID")
    private String merchantId;
    /**
     * 适用范围 ，JSON.toJSONString方法
     */
    @NotNull(message = "网信适用范围不能为空")
    @ApiModelProperty("网信适用范围")
    private CreditScope creditScope;

    @NotNull(message = "网信预售金额上限不能为空")
    @ApiModelProperty("网信预售金额上限，第一次预售网信金额不能大于36900元")
    private int presaleUpperLimit;

    @NotBlank(message = "网信描述不能为空")
    @ApiModelProperty("网信描述")
    @Length(min = 10 ,max=200,message = "网信描述字数不能小于10或者大于200")
    private String description;

    @NotNull(message = "网信发行份数不能为空")
    @ApiModelProperty("网信发行份数")
    private int issueNumber;
    /**
     *  网信分红，JSON.toJSONString方法
     */
    @ApiModelProperty("网信分红")
    private List<CreditDividendSetting> creditDividendSettings;

    @ApiModelProperty("申购折扣")
    private List<CreditLevelDiscountSetting> creditLevelDiscountSettings;

    @NotBlank(message = "收银人员网号不能为空")
    @ApiModelProperty("收银人员网号")
    private String cashierNetworkNum;

    @NotBlank(message = "收银人员真实姓名不能为空")
    @ApiModelProperty("收银人员真实姓名")
    private String cashierName;

    @NotBlank(message = "收银人员手机号码不能为空")
    @ApiModelProperty("收银人员手机号码")
    private String cashierPhone;

    @NotBlank(message = "收银人员身份证号不能为空")
    @ApiModelProperty("收银人员身份证号")
    private String cashierIdNumber;

    @NotNull(message = "保底交易额不能为空")
    @ApiModelProperty("保底交易额")
    private Integer ensureDeal;

    @NotNull(message = "内购期限不能为空")
    @ApiModelProperty(value = "内购期限，UNIX时间戳，精确到毫秒")
    private Long innerPurchaseDate;

    @ApiModelProperty(value = "受邀好友id, 多个用逗号隔开（英文标点符号)")
    private String toUserId;

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public CreditScope getCreditScope() {
        return creditScope;
    }

    public void setCreditScope(CreditScope creditScope) {
        this.creditScope = creditScope;
    }

    public int getPresaleUpperLimit() {
        return presaleUpperLimit;
    }

    public void setPresaleUpperLimit(int presaleUpperLimit) {
        this.presaleUpperLimit = presaleUpperLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public List<CreditDividendSetting> getCreditDividendSettings() {
        return creditDividendSettings;
    }

    public void setCreditDividendSettings(List<CreditDividendSetting> creditDividendSettings) {
        this.creditDividendSettings = creditDividendSettings;
    }

    public List<CreditLevelDiscountSetting> getCreditLevelDiscountSettings() {
        return creditLevelDiscountSettings;
    }

    public void setCreditLevelDiscountSettings(List<CreditLevelDiscountSetting> creditLevelDiscountSettings) {
        this.creditLevelDiscountSettings = creditLevelDiscountSettings;
    }

    public String getCashierNetworkNum() {
        return cashierNetworkNum;
    }

    public void setCashierNetworkNum(String cashierNetworkNum) {
        this.cashierNetworkNum = cashierNetworkNum;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierPhone() {
        return cashierPhone;
    }

    public void setCashierPhone(String cashierPhone) {
        this.cashierPhone = cashierPhone;
    }

    public String getCashierIdNumber() {
        return cashierIdNumber;
    }

    public void setCashierIdNumber(String cashierIdNumber) {
        this.cashierIdNumber = cashierIdNumber;
    }

    public Integer getEnsureDeal() {
        return ensureDeal;
    }

    public void setEnsureDeal(Integer ensureDeal) {
        this.ensureDeal = ensureDeal;
    }

    public Long getInnerPurchaseDate() {
        return innerPurchaseDate;
    }

    public void setInnerPurchaseDate(Long innerPurchaseDate) {
        this.innerPurchaseDate = innerPurchaseDate;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
