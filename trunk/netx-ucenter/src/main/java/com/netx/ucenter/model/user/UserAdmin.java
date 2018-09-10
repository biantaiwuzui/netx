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
 * @author 黎子安
 * @since 2018-06-11
 */
@TableName("user_admin")
public class UserAdmin extends Model<UserAdmin> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 登录名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 密码
     */
	private String password;
    /**
     * 真实姓名
     */
	@TableField("real_name")
	private String realName;
    /**
     * 手机号码
     */
	private String mobile;
    /**
     * 是否是超级管理员
     */
	@TableField("is_super_admin")
	private Boolean isSuperAdmin;
	/**
	 * 禁用理由
	 */
	private String reason;
	@TableField("create_user_name")
	private String createUserName;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("update_user_name")
	private String updateUserName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Boolean getSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
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
		return "UserAdmin{" +
				"id='" + id + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", realName='" + realName + '\'' +
				", mobile='" + mobile + '\'' +
				", isSuperAdmin=" + isSuperAdmin +
				", reason='" + reason + '\'' +
				", createUserName='" + createUserName + '\'' +
				", createTime=" + createTime +
				", updateUserName='" + updateUserName + '\'' +
				", updateTime=" + updateTime +
				", deleted=" + deleted +
				'}';
	}
}
