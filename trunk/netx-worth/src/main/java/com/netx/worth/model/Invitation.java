package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 邀请表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Invitation extends Model<Invitation> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 邀请人
     */
	@TableField("from_user_id")
	private String fromUserId;
    /**
     * 对象
     */
	@TableField("to_user_id")
	private String toUserId;
    /**
     * 邀请标题（主题）
     */
	private String title;
    /**
     * 地址
     */
	private String address;
    /**
     * 开始时间
     */
	@TableField("start_at")
	private Date startAt;
    /**
     * 结束时间
     */
	@TableField("end_at")
	private Date endAt;
    /**
     * 报酬
     */
	private Long amout;
    /**
     * 描述
     */
	private String description;
    /**
     * 是否匿名
     */
	@TableField("is_anonymity")
	private Boolean isAnonymity;
    /**
     * 是否确认（时间、地址）
     */
	@TableField("is_confirm")
	private Boolean isConfirm;
    /**
     * 邀请码
     */
	private Integer code;
    /**
     * 建议描述
     */
	private String suggestion;
    /**
     * 邀请码重试次数
     */
	private Integer times;
    /**
     * 验证码是否通过
     */
	@TableField("is_validation")
	private Boolean isValidation;
    /**
     * 状态：
            1：发出邀请
            2：接受邀请
            3：拒绝邀请
            4：超时取消，目前由用户自己拒绝
            
     */
	private Integer status;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 邀请人验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            2：验证失败
            
     */
	@TableField("from_validation_status")
	private Boolean fromValidationStatus;
    /**
     * 被邀请人验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            2：验证失败
            
     */
	@TableField("to_validation_status")
	private Boolean toValidationStatus;
    /**
     * 是否纯线上活动（无需地址）
     */
	@TableField("is_online")
	private Boolean isOnline;
    /**
     * 来源资讯id，即：从哪里点过来的
     */
	@TableField("article_id")
	private String articleId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Long getAmout() {
		return amout;
	}

	public void setAmout(Long amout) {
		this.amout = amout;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(Boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	public Boolean getConfirm() {
		return isConfirm;
	}

	public void setConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Boolean getValidation() {
		return isValidation;
	}

	public void setValidation(Boolean isValidation) {
		this.isValidation = isValidation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public Boolean getFromValidationStatus() {
		return fromValidationStatus;
	}

	public void setFromValidationStatus(Boolean fromValidationStatus) {
		this.fromValidationStatus = fromValidationStatus;
	}

	public Boolean getToValidationStatus() {
		return toValidationStatus;
	}

	public void setToValidationStatus(Boolean toValidationStatus) {
		this.toValidationStatus = toValidationStatus;
	}

	public Boolean getOnline() {
		return isOnline;
	}

	public void setOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
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
		return "Invitation{" +
			"id=" + id +
			", fromUserId=" + fromUserId +
			", toUserId=" + toUserId +
			", title=" + title +
			", address=" + address +
			", startAt=" + startAt +
			", endAt=" + endAt +
			", amout=" + amout +
			", description=" + description +
			", isAnonymity=" + isAnonymity +
			", isConfirm=" + isConfirm +
			", code=" + code +
			", suggestion=" + suggestion +
			", times=" + times +
			", isValidation=" + isValidation +
			", status=" + status +
			", lon=" + lon +
			", lat=" + lat +
			", fromValidationStatus=" + fromValidationStatus +
			", toValidationStatus=" + toValidationStatus +
			", isOnline=" + isOnline +
			", articleId=" + articleId +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
