package com.netx.shopping.model.business;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-业务主管表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("seller_manage")
public class SellerManage extends Model<SellerManage> {

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
     * 主管姓名
     */
	@TableField("manage_name")
	private String manageName;
    /**
     * 主管手机号
     */
	@TableField("manage_phone")
	private String managePhone;
    /**
     * 主管网号
     */
	@TableField("manage_network_num")
	private String manageNetworkNum;
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

	public String getManageName() {
		return manageName;
	}

	public void setManageName(String manageName) {
		this.manageName = manageName;
	}

	public String getManagePhone() {
		return managePhone;
	}

	public void setManagePhone(String managePhone) {
		this.managePhone = managePhone;
	}

	public String getManageNetworkNum() {
		return manageNetworkNum;
	}

	public void setManageNetworkNum(String manageNetworkNum) {
		this.manageNetworkNum = manageNetworkNum;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SellerManage{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", manageName='" + manageName + '\'' +
				", managePhone='" + managePhone + '\'' +
				", manageNetworkNum='" + manageNetworkNum + '\'' +
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
