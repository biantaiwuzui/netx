package com.netx.ucenter.model.user;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hj.Mao
 * @since 2018-03-17
 */
@TableName("user_pay_account")
public class UserPayAccount extends Model<UserPayAccount> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private String id;
    /**
     * 第三方返回获取得到的账号
     */
	@TableField("account_display")
	private String accountDisplay;
    /**
     * 第三方返回的唯一用户id
     */
	@TableField("account_identity")
	private String accountIdentity;
    /**
     * 999表示当前使用    1.表示非当前使用
     */
	private Integer priority;
    /**
     * 微信还是支付宝:   1.微信    2.支付宝
     */
	@TableField("account_type")
	private Integer accountType;
    /**
     * 所属用户id
     */
	@TableField("user_id")
	private String userId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountDisplay() {
		return accountDisplay;
	}

	public void setAccountDisplay(String accountDisplay) {
		this.accountDisplay = accountDisplay;
	}

	public String getAccountIdentity() {
		return accountIdentity;
	}

	public void setAccountIdentity(String accountIdentity) {
		this.accountIdentity = accountIdentity;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		return "UserPayAccount{" +
			"id=" + id +
			", accountDisplay=" + accountDisplay +
			", accountIdentity=" + accountIdentity +
			", priority=" + priority +
			", accountType=" + accountType +
			", userId=" + userId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
