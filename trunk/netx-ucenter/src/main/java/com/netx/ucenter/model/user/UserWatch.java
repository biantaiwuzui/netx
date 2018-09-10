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
 * 用户关注表
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("user_watch")
public class UserWatch extends Model<UserWatch> {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 发起人
	 */
	@TableField("from_user_id")
	private String fromUserId;
	/**
	 * 关注对象
	 */
	@TableField("to_user_id")
	private String toUserId;
	/**
	 * 关注时间
	 */
	@TableField("watch_at")
	private Date watchAt;
	/**
	 * 类型：
	 1：主动发起
	 其他待定
	 */
	@TableField("wach_type")
	private Integer wachType;
	/**
	 * 是否是关注（取消关注时更改此字段）
	 */
	@TableField("is_watch")
	private Boolean isWatch;
	/**
	 * 关联主键，没有就是0.
	 */
	@TableField("relatable_id")
	private String relatableId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Date getWatchAt() {
		return watchAt;
	}

	public void setWatchAt(Date watchAt) {
		this.watchAt = watchAt;
	}

	public Integer getWachType() {
		return wachType;
	}

	public void setWachType(Integer wachType) {
		this.wachType = wachType;
	}

	public Boolean getWatch() {
		return isWatch;
	}

	public void setWatch(Boolean isWatch) {
		this.isWatch = isWatch;
	}

	public String getRelatableId() {
		return relatableId;
	}

	public void setRelatableId(String relatableId) {
		this.relatableId = relatableId;
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
		return "UserWatch{" +
				"id=" + id +
				", fromUserId=" + fromUserId +
				", toUserId=" + toUserId +
				", watchAt=" + watchAt +
				", wachType=" + wachType +
				", isWatch=" + isWatch +
				", relatableId=" + relatableId +
				", createTime=" + createTime +
				", createUserId=" + createUserId +
				", updateTime=" + updateTime +
				", updateUserId=" + updateUserId +
				", deleted=" + deleted +
				"}";
	}
}
