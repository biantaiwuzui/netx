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
 * 活动聚会报名表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("meeting_register")
public class MeetingRegister extends Model<MeetingRegister> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 主表ID
     */
	@TableField("meeting_id")
	private String meetingId;
    /**
     * 报名人
     */
	@TableField("user_id")
	private String userId;
    /**
     * 邀请的好友
     */
	private String friends;
    /**
     * 是否匿名
     */
	@TableField("is_anonymity")
	private Boolean isAnonymity;
    /**
     * 报名数量
     */
	private Integer amount;
    /**
     * 报名总费用（单价*数量）
     */
	private Long fee;
    /**
     * 报名状态：
            1：已报名
            2：已入选
            3：未入选
            4：已取消
            5：确认出席，准备校验验证码
     */
	private Integer status;
    /**
     * 验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            2：验证失败
            
     */
	@TableField("validation_status")
	private Integer validationStatus;
    /**
     * 邀请码重试次数
     */
	private Integer times;
    /**
     * 是否支付
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 邀请人
     */
	@TableField("parent_id")
	private String parentId;
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

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public Boolean getAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(Boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Integer validationStatus) {
		this.validationStatus = validationStatus;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
		return "MeetingRegister{" +
			"id=" + id +
			", meetingId=" + meetingId +
			", userId=" + userId +
			", friends=" + friends +
			", isAnonymity=" + isAnonymity +
			", amount=" + amount +
			", fee=" + fee +
			", status=" + status +
			", validationStatus=" + validationStatus +
			", times=" + times +
			", isPay=" + isPay +
			", parentId=" + parentId +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
