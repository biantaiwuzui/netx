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
 * 技能单表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("skill_order") //表名字
public class SkillOrder extends Model<SkillOrder> {//继承 数据库操作

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 主表id
     */
	@TableField("skill_register_id")
	private String skillRegisterId;
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
     * 状态：
            0：初始化
            1：已开始
            2：已取消
            3：已成功
            4：已失败
            
     */
	private Integer status;
    /**
     * 邀请码
     */
	private Integer code;
    /**
     * 验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            2：验证失败
            
     */
	@TableField("validation_status")
	private Boolean validationStatus;
	@TableField(value = "create_time", fill = FieldFill.INSERT)  //公共字段自动填充的功能
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

	public String getSkillRegisterId() {
		return skillRegisterId;
	}

	public void setSkillRegisterId(String skillRegisterId) {
		this.skillRegisterId = skillRegisterId;
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Boolean getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Boolean validationStatus) {
		this.validationStatus = validationStatus;
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

	@Override//判断方法名错误
	public String toString() {
		return "SkillOrder{" +
			"id=" + id +
			", skillRegisterId=" + skillRegisterId +
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
			", code=" + code +
			", validationStatus=" + validationStatus +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
