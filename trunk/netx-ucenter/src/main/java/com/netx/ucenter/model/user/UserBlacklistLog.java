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
 * 黑名单操作日志流水表(关联主表，取最新的一条，即为拉黑或释放理由)
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("user_blacklist_log")
public class UserBlacklistLog extends Model<UserBlacklistLog> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 拉黑或释放理由
     */
	private String reason;
	@TableField("system_black_list_id")
	private String systemBlackListId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_name")
	private String createUserName;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSystemBlackListId() {
		return systemBlackListId;
	}

	public void setSystemBlackListId(String systemBlackListId) {
		this.systemBlackListId = systemBlackListId;
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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "UserBlacklistLog{" +
			"id=" + id +
			", reason=" + reason +
			", systemBlackListId=" + systemBlackListId +
			", userId=" + createUserName+
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
