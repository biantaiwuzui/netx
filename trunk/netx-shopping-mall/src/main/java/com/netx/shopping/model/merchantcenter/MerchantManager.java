package com.netx.shopping.model.merchantcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-收银人员表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("merchant_manager")
public class MerchantManager extends Model<MerchantManager> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
	private String id;
    /**
     * 商家id
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 商家注册者用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 商家管理人员类型：法人代表，收银人员，业务主管
     */
	@TableField("merchant_user_type")
	private String merchantUserType;
    /**
     * 收银人员姓名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 收银人手机号
     */
	@TableField("user_phone")
	private String userPhone;
    /**
     * 收银人网号
     */
	@TableField("user_network_num")
	private String userNetworkNum;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMerchantUserType() {
		return merchantUserType;
	}

	public void setMerchantUserType(String merchantUserType) {
		this.merchantUserType = merchantUserType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNetworkNum() {
		return userNetworkNum;
	}

	public void setUserNetworkNum(String userNetworkNum) {
		this.userNetworkNum = userNetworkNum;
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
		return this.id;
	}

	@Override
	public String toString() {
		return "MerchantManager{" +
			"id=" + id +
			", merchantId=" + merchantId +
			", userId=" + userId +
			", merchantUserType=" + merchantUserType +
			", userName=" + userName +
			", userPhone=" + userPhone +
			", userNetworkNum=" + userNetworkNum +
			", isCurrent=" + isCurrent +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
