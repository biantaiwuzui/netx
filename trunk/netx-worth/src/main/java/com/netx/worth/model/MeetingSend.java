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
 * 活动聚会表发起表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("meeting_send")
public class MeetingSend extends Model<MeetingSend> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 主表ID
     */
	@TableField("meeting_id")
	private String meetingId;
	@TableField("user_id")
	private String userId;
    /**
     * 是否主发起人
     */
	@TableField("is_default")
	private Boolean isDefault;
    /**
     * 状态：
            0：待同意
            1：同意（已发起）
            2：拒绝
            3：已取消
     */
	private Integer status;
    /**
     * 同意时间
     */
	@TableField("accept_at")
	private Date acceptAt;
    /**
     * 拒绝时间
     */
	@TableField("refuse_at")
	private Date refuseAt;
    /**
     * 过期时间
     */
	@TableField("expired_at")
	private Date expiredAt;
    /**
     * 距离验证状态：
            0：未验证
            1：通过验证
            2：验证失败
     */
	@TableField("validation_status")
	private Integer validationStatus;
    /**
     * 邀请码
     */
	private Integer code;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
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

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAcceptAt() {
		return acceptAt;
	}

	public void setAcceptAt(Date acceptAt) {
		this.acceptAt = acceptAt;
	}

	public Date getRefuseAt() {
		return refuseAt;
	}

	public void setRefuseAt(Date refuseAt) {
		this.refuseAt = refuseAt;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Integer getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Integer validationStatus) {
		this.validationStatus = validationStatus;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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
		return "MeetingSend{" +
			"id=" + id +
			", meetingId=" + meetingId +
			", userId=" + userId +
			", isDefault=" + isDefault +
			", status=" + status +
			", acceptAt=" + acceptAt +
			", refuseAt=" + refuseAt +
			", expiredAt=" + expiredAt +
			", validationStatus=" + validationStatus +
			", code=" + code +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
