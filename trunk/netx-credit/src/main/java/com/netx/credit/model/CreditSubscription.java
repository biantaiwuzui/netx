package com.netx.credit.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 网信-认购表
 * @author 梓
 */

@TableName("credit_subscription")
public class CreditSubscription extends Model<CreditSubscription> {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    /**
     * 网信ID
     */
    @TableField("credit_id")
    private String creditId;
    /**
     * 商家id
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 认购用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 用户等级ID
     */
    @TableField("credit_stage_id")
    private String creditStageId;
    /**
     * 用户认购身份( 商家各级人员，内购好友，普通好友，普通用户)
     */
    @TableField("type")
    private String type;
    /**
     * 网信认购额
     */
    @TableField("subscription_number")
    private Double subscriptionNumber;
    /**
     * 用户认购状态
     */
    @TableField("status")
    private int status;
    /**
     * 邀请消息
     */
    @TableField("message_pyload")
    private String messagePyload;
    @TableField(value = "send_time", fill = FieldFill.INSERT)
    private Date sendTime;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreditStageId() {
        return creditStageId;
    }

    public void setCreditStageId(String creditStageId) {
        this.creditStageId = creditStageId;
    }

    public Double getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(Double subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessagePyload() {
        return messagePyload;
    }

    public void setMessagePyload(String messagePyload) {
        this.messagePyload = messagePyload;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
