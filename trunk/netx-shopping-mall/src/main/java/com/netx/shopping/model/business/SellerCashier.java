package com.netx.shopping.model.business;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

/**
 * <p>
 * 网商-收银人员表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("seller_cashier")
public class SellerCashier extends Model<SellerCashier> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
	private String id;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 收银人员姓名
     */
	@TableField("money_name")
	private String moneyName;
    /**
     * 收银人手机号
     */
	@TableField("money_phone")
	private String moneyPhone;
	/**
	 * 收银人网号
	 */
	@TableField("money_network_num")
	private String moneyNetworkNum;
	/**
	 * 商家id
	 */
	@TableField("seller_id")
	private String sellerId;
	/**
	 * 是否当前商家适用中： 0：不是   1：是
	 */
	@TableField("is_current")
	private Integer isCurrent;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user_id")
	private String createUserId;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user_id")
	private String updateUserId;
    /**
     * 删除标识
     */
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMoneyName() {
		return moneyName;
	}

	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	public String getMoneyPhone() {
		return moneyPhone;
	}

	public void setMoneyPhone(String moneyPhone) {
		this.moneyPhone = moneyPhone;
	}

	public String getMoneyNetworkNum() {
		return moneyNetworkNum;
	}

	public void setMoneyNetworkNum(String moneyNetworkNum) {
		this.moneyNetworkNum = moneyNetworkNum;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
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
		return "SellerCashier{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", moneyName='" + moneyName + '\'' +
				", moneyPhone='" + moneyPhone + '\'' +
				", moneyNetworkNum='" + moneyNetworkNum + '\'' +
				", sellerId='" + sellerId + '\'' +
				", isCurrent=" + isCurrent +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
