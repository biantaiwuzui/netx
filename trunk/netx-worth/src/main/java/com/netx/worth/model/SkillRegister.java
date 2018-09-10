package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * <p>
 * 技能预约表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("skill_register")
public class SkillRegister extends Model<SkillRegister> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 主表id
     */
	@TableField("skill_id")
	private String skillId;
    /**
     * 建议的开始时间
     */
	@TableField("start_at")
	private Date startAt;
    /**
     * 建议的结束时间
     */
	@TableField("end_at")
	private Date endAt;
    /**
     * 单位
     */
	private String unit;
    /**
     * 单价
     */
	private Long amount;
    /**
     * 数量
     */
	private Integer number;
    /**
     * 总价（单价*数量）
     */
	private Long fee;


    /**
     * 描述
     */
	private String description;
    /**
     * 地址
     */
	private String address;
	/**距离*/
    /**
     * 经度
     */
	@Range(min = -180,max = 180,message = "经度输入不在范围内")
	private BigDecimal lon;
    /**
     * 纬度
     */
	@Range(min = -90,max = 90,message = "纬度输入不在范围内")
	private BigDecimal lat;
    /**
     * 报名状态：
            0：待入选
            1：已入选
            2：已拒绝
            3：已过期
     */
	private Integer status;
    /**
     * 是否支付给发布方
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 已托管的费用
     */
	private Long bail;
    /**
     * 托管的付款方式：0：网币，1：平台垫付
     */
	@TableField("pay_way")
	private Integer payWay;
    /**
     * 是否匿名
     */
	@TableField("is_anonymity")
	private Boolean isAnonymity;
    /**
     * 验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            
            
     */
	@TableField("validation_status")
	private Boolean validationStatus;
    /**
     * 验证码是否通过
     */
	@TableField("is_validation")
	private Boolean isValidation;
    /**
     * 邀请码重试次数
     */
	private Integer times;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSkillId() {
		return skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public Long getBail() {
		return bail;
	}

	public void setBail(Long bail) {
		this.bail = bail;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Boolean getAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(Boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	public Boolean getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Boolean validationStatus) {
		this.validationStatus = validationStatus;
	}

	public Boolean getValidation() {
		return isValidation;
	}

	public void setValidation(Boolean isValidation) {
		this.isValidation = isValidation;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SkillRegister{" +
			"id=" + id +
			", userId=" + userId +
			", skillId=" + skillId +
			", startAt=" + startAt +
			", endAt=" + endAt +
			", unit=" + unit +
			", amount=" + amount +
			", number=" + number +
			", fee=" + fee +
			", description=" + description +
			", address=" + address +
			", lon=" + lon +
			", lat=" + lat +
			", status=" + status +
			", isPay=" + isPay +
			", bail=" + bail +
			", payWay=" + payWay +
			", isAnonymity=" + isAnonymity +
			", validationStatus=" + validationStatus +
			", isValidation=" + isValidation +
			", times=" + times +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
