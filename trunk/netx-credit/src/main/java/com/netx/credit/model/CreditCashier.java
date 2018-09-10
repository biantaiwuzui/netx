package com.netx.credit.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 收银人员信息表
 * @梓
 */
@TableName("credit_cashier")
public class CreditCashier extends Model<CreditCashier> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 商家id
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 商家用户id
     */
    @TableField("merchant_user_id")
    private String merchantUserId;
    /**
     * 收银人员姓名
     */
    @TableField("cashier_name")
    private String cashierName;
    /**
     * 收银人员手机
     */
    @TableField("cashier_phone")
    private String cashierPhone;
    /**
     * 收银人员网号
     */
    @TableField("cashier_network_num")
    private String cashierNetworkNum;
    /**
     * 是否商家使用中： 0：不是   1：是
     */
    @TableField("is_current")
    private Integer isCurrent;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标识
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
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

    public String getCashierNetworkNum() {
        return cashierNetworkNum;
    }

    public void setCashierNetworkNum(String cashierNetworkNum) {
        this.cashierNetworkNum = cashierNetworkNum;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
