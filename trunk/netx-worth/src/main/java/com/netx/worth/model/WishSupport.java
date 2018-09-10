package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 心愿支持表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("wish_support")
public class WishSupport extends Model<WishSupport> {

    private static final long serialVersionUID = 1L;
	/**
	 * 主表ID
	 */
	private String id;
	/**
	 * 心愿ID
	 */
	@TableField("wish_id")
	private String wishId;
	/**
	 * 用户ID
	 */
	@TableField("user_id")
	private String userId;
    /**
     * 是否支付
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 支持金额
     */
	private Long amount;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 创建用户id
	 */
	@TableField("create_user_id")
	private String createUserId;
	/**
	 * 更新时间 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 更新用户id
	 */
	@TableField("update_user_id")
	private String updateUserId;

	@TableField(fill = FieldFill.INSERT)
    @TableLogic
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
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
		return "WishSupport{" +
			"id=" + id +
			", wishId=" + wishId +
			", userId=" + userId +
			", isPay=" + isPay +
			", amount=" + amount +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
