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
import java.io.Serializable;

/**
 * <p>
 * 需求报名表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("demand_register")
public class DemandRegister extends Model<DemandRegister> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 主表ID
     */
	@TableField("demand_id")
	private String demandId;
    /**
     * 报名人
     */
	@TableField("user_id")
	private String userId;
    /**
     * 建议开始时间
     */
	@TableField("start_at")
	private Date startAt;
    /**
     * 建议结束时间
     */
	@TableField("end_at")
	private Date endAt;
    /**
     * 建议时间要求：只有大概范围，如：50天内、仅限周末等，具体的时间待申请成功后再与发布者协商确定
     */
	private String about;
    /**
     * 描述
     */
	private String description;
	private String unit;
    /**
     * 希望的报酬
     */
	private Long wage;
    /**
     * 地址
     */
	private String address;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 是否已经支付报酬
     */
	@TableField("is_pay_wage")
	private Boolean isPayWage;
    /**
     * 报名状态：
            0：已报名，未入选
            1：已入选
            2：已取消
            3：未入选
            4：已启动需求
            5：放弃参与，即已入选放弃
            6：超时未启动需求
            7：发布者取消入选者的需求
     */
	private Integer status;
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
            2：验证失败
            
     */
	@TableField("validation_status")
	private Boolean validationStatus;
    /**
     * 邀请码
     */
	private Integer code;
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
     * 需求订单的ID，入选后，此ID便有值了。
     */
	@TableField("demand_order_id")
	private String demandOrderId;
    /**
     * 已结算的费用，即在申请退款时，如果提前支付了一部分钱，那么就提现在这里
     */
	@TableField("paied_fee")
	private Long paiedFee;
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

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getWage() {
		return wage;
	}

	public void setWage(Long wage) {
		this.wage = wage;
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

	public Boolean getPayWage() {
		return isPayWage;
	}

	public void setPayWage(Boolean isPayWage) {
		this.isPayWage = isPayWage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
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

	public String getDemandOrderId() {
		return demandOrderId;
	}

	public void setDemandOrderId(String demandOrderId) {
		this.demandOrderId = demandOrderId;
	}

	public Long getPaiedFee() {
		return paiedFee;
	}

	public void setPaiedFee(Long paiedFee) {
		this.paiedFee = paiedFee;
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
		return "DemandRegister{" +
			"id=" + id +
			", demandId=" + demandId +
			", userId=" + userId +
			", startAt=" + startAt +
			", endAt=" + endAt +
			", about=" + about +
			", description=" + description +
			", unit=" + unit +
			", wage=" + wage +
			", address=" + address +
			", lon=" + lon +
			", lat=" + lat +
			", isPayWage=" + isPayWage +
			", status=" + status +
			", isAnonymity=" + isAnonymity +
			", validationStatus=" + validationStatus +
			", code=" + code +
			", times=" + times +
			", isValidation=" + isValidation +
			", demandOrderId=" + demandOrderId +
			", paiedFee=" + paiedFee +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
