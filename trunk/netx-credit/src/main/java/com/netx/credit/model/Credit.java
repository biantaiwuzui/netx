package com.netx.credit.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 网信发行表
 * @author FRWIN
 * @since 2018-07-06
 */
@TableName("credit")
public class Credit extends Model<Credit>{

    private static final long serialVersionUID = 1L;

    /**
     * 标识id
     */
    @TableField("id")
    private String id;
    /**
     * 图片url
     */
    @TableField("picture_url")
    private String pictureUrl;
    /**
     * 用户名
     */
    @TableField("name")
    private String name;
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 商家id
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 发行适用范围,json结构
     */
    @TableField("scope")
    private String scope;
    /**
     * 预售金额上限
     */
    @TableField("presale_upper_limit")
    private Integer presaleUpperLimit;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 网信预售份数
     */
    @TableField("issue_number")
    private Integer issueNumber;
    /**
     * 分红设置,json结构
     */
    @TableField("dividend_setting")
    private String dividendSetting;
    /**
     * 收银员id
     */
    @TableField("cashier_id")
    private String cashierId;
    /**
     * 保底交易额
     */
    @TableField("ensure_deal")
    private Integer ensureDeal;
    /**
     * 内购日期
     */
    @TableField("inner_purchase_date")
    private Date innerPurchaseDate;
    /**
     * 网信单价
     */
    @TableField("credit_price")
    private Integer creditPrice;
    /**
     * 申购折扣
     */
    @TableField("subscription_discount")
    private String subscriptionDiscount;

    /**
     * 网信状态
     * 0.正在预售
     * 1.开放认购
     * 2.不在认购
     */
    @TableField("credit_status")
    private int creditStatus;
    /**
     * 删除状态
     * 1表示已删除
     * 0表示未删除
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

    public String getDividendSetting() {
        return dividendSetting;
    }

    public void setDividendSetting(String dividendSetting) {
        this.dividendSetting = dividendSetting;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getInnerPurchaseDate() {
        return innerPurchaseDate;
    }

    public void setInnerPurchaseDate(Date innerPurchaseDate) {
        this.innerPurchaseDate = innerPurchaseDate;
    }

    public Integer getEnsureDeal() {
        return ensureDeal;
    }

    public void setEnsureDeal(Integer ensureDeal) {
        this.ensureDeal = ensureDeal;
    }

    public Integer getCreditPrice() {
        return creditPrice;
    }

    public void setCreditPrice(Integer creditPrice) {
        this.creditPrice = creditPrice;
    }

    public String getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(String subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public int getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(int creditStatus) {
        this.creditStatus = creditStatus;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id='" + id + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", scope='" + scope + '\'' +
                ", presaleUpperLimit=" + presaleUpperLimit +
                ", description='" + description + '\'' +
                ", issueNumber=" + issueNumber +
                ", dividendSetting='" + dividendSetting + '\'' +
                ", cashierId='" + cashierId + '\'' +
                ", ensureDeal=" + ensureDeal +
                ", innerPurchaseDate=" + innerPurchaseDate +
                ", creditPrice=" + creditPrice +
                ", subscriptionDiscount='" + subscriptionDiscount + '\'' +
                ", creditStatus=" + creditStatus +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
