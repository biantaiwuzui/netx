package com.netx.ucenter.model.common;

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
 * @since 2018-03-09
 */
@TableName("common_manage_arbitration")
public class CommonManageArbitration extends Model<CommonManageArbitration> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("from_nickname")
	private String fromNickname;
	@TableField("to_nickname")
	private String toNickname;
	@TableField("from_user_level")
	private Integer fromUserLevel;
	@TableField("to_user_level")
	private Integer toUserLevel;
	@TableField("from_user_sex")
	private String fromUserSex;
	@TableField("to_user_sex")
	private String toUserSex;
	@TableField("from_user_age")
	private Integer fromUserAge;
	@TableField("to_user_age")
	private Integer toUserAge;
	@TableField("status_code")
	private Integer statusCode;
    /**
     * 投诉类型
     */
	private Integer type;
    /**
     * 投诉事件ID
     */
	@TableField("type_id")
	private String typeId;
	private String theme;
	@TableField("from_user_credit_value")
	private Integer fromUserCreditValue;
	@TableField("to_user_credit_value")
	private Integer toUserCreditValue;
	private Date time;
	private String reason;
	@TableField("from_user_id")
	private String fromUserId;
	@TableField("to_user_id")
	private String toUserId;
	private String descriptions;
	@TableField("from_src_url")
	private String fromSrcUrl;
	@TableField("appeal_src_url")
	private String appealSrcUrl;
	@TableField("appeal_date")
	private Date appealDate;
	@TableField("result_id")
	private String resultId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("create_user_id")
	private String createUserId;
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

	public String getFromNickname() {
		return fromNickname;
	}

	public void setFromNickname(String fromNickname) {
		this.fromNickname = fromNickname;
	}

	public String getToNickname() {
		return toNickname;
	}

	public void setToNickname(String toNickname) {
		this.toNickname = toNickname;
	}

	public Integer getFromUserLevel() {
		return fromUserLevel;
	}

	public void setFromUserLevel(Integer fromUserLevel) {
		this.fromUserLevel = fromUserLevel;
	}

	public Integer getToUserLevel() {
		return toUserLevel;
	}

	public void setToUserLevel(Integer toUserLevel) {
		this.toUserLevel = toUserLevel;
	}

	public String getFromUserSex() {
		return fromUserSex;
	}

	public void setFromUserSex(String fromUserSex) {
		this.fromUserSex = fromUserSex;
	}

	public String getToUserSex() {
		return toUserSex;
	}

	public void setToUserSex(String toUserSex) {
		this.toUserSex = toUserSex;
	}

	public Integer getFromUserAge() {
		return fromUserAge;
	}

	public void setFromUserAge(Integer fromUserAge) {
		this.fromUserAge = fromUserAge;
	}

	public Integer getToUserAge() {
		return toUserAge;
	}

	public void setToUserAge(Integer toUserAge) {
		this.toUserAge = toUserAge;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Integer getFromUserCreditValue() {
		return fromUserCreditValue;
	}

	public void setFromUserCreditValue(Integer fromUserCreditValue) {
		this.fromUserCreditValue = fromUserCreditValue;
	}

	public Integer getToUserCreditValue() {
		return toUserCreditValue;
	}

	public void setToUserCreditValue(Integer toUserCreditValue) {
		this.toUserCreditValue = toUserCreditValue;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getFromSrcUrl() {
		return fromSrcUrl;
	}

	public void setFromSrcUrl(String fromSrcUrl) {
		this.fromSrcUrl = fromSrcUrl;
	}

	public String getAppealSrcUrl() {
		return appealSrcUrl;
	}

	public void setAppealSrcUrl(String appealSrcUrl) {
		this.appealSrcUrl = appealSrcUrl;
	}

	public Date getAppealDate() {
		return appealDate;
	}

	public void setAppealDate(Date appealDate) {
		this.appealDate = appealDate;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
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

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
		return "CommonManageArbitration{" +
				"id='" + id + '\'' +
				", fromNickname='" + fromNickname + '\'' +
				", toNickname='" + toNickname + '\'' +
				", fromUserLevel=" + fromUserLevel +
				", toUserLevel=" + toUserLevel +
				", fromUserSex='" + fromUserSex + '\'' +
				", toUserSex='" + toUserSex + '\'' +
				", fromUserAge=" + fromUserAge +
				", toUserAge=" + toUserAge +
				", statusCode=" + statusCode +
				", type=" + type +
				", typeId='" + typeId + '\'' +
				", theme='" + theme + '\'' +
				", fromUserCreditValue=" + fromUserCreditValue +
				", toUserCreditValue=" + toUserCreditValue +
				", time=" + time +
				", reason='" + reason + '\'' +
				", fromUserId='" + fromUserId + '\'' +
				", toUserId='" + toUserId + '\'' +
				", descriptions='" + descriptions + '\'' +
				", fromSrcUrl='" + fromSrcUrl + '\'' +
				", appealSrcUrl='" + appealSrcUrl + '\'' +
				", appealDate=" + appealDate +
				", resultId='" + resultId + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", createUserId='" + createUserId + '\'' +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
