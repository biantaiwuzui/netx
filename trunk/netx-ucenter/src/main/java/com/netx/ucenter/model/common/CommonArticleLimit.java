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
@TableName("common_article_limit")
public class CommonArticleLimit extends Model<CommonArticleLimit> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
	@TableField("user_network_num")
	private String userNetworkNum;
	@TableField("operator_nickname")
	private String operatorNickname;
	@TableField("user_nickname")
	private String userNickname;
	@TableField("user_level")
	private Integer userLevel;
	@TableField("user_age")
	private Integer userAge;
	@TableField("user_sex")
	private Integer userSex;
	private String reason;
	private Date date;
	@TableField("limit_measure")
	private Integer limitMeasure;
	@TableField("release_time")
	private Date releaseTime;
	@TableField("limit_value")
	private Integer limitValue;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNetworkNum() {
		return userNetworkNum;
	}

	public void setUserNetworkNum(String userNetworkNum) {
		this.userNetworkNum = userNetworkNum;
	}

	public String getOperatorNickname() {
		return operatorNickname;
	}

	public void setOperatorNickname(String operatorNickname) {
		this.operatorNickname = operatorNickname;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getLimitMeasure() {
		return limitMeasure;
	}

	public void setLimitMeasure(Integer limitMeasure) {
		this.limitMeasure = limitMeasure;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getLimitValue() {
		return limitValue;
	}

	public void setLimitValue(Integer limitValue) {
		this.limitValue = limitValue;
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
		return "CommonArticleLimit{" +
			"id=" + id +
			", userId=" + userId +
			", userNetworkNum=" + userNetworkNum +
			", operatorNickname=" + operatorNickname +
			", userNickname=" + userNickname +
			", userLevel=" + userLevel +
			", userAge=" + userAge +
			", userSex=" + userSex +
			", reason=" + reason +
			", date=" + date +
			", limitMeasure=" + limitMeasure +
			", releaseTime=" + releaseTime +
			", limitValue=" + limitValue +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
