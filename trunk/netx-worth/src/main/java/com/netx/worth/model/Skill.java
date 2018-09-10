package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import org.hibernate.validator.constraints.Range;


/**
 * <p>
 * 技能表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Skill extends Model<Skill> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 技能标签，逗号分隔
     */
	private String skill;
    /**
     * 水平标签，逗号分隔
     */
	private String level;
    /**
     * 描述
     */
	private String description;
    /**
     * 图片
     */
	@TableField("skill_images_url")
	private String skillImagesUrl;
    /**
     * 图片
     */
	@TableField("skill_detail_images_url")
	private String skillDetailImagesUrl;
    /**
     * 单位
     */
	private String unit;
    /**
     * 单价
     */
	private Long amount;
    /**
     * 价格说明
     */
	private String intr;
    /**
     * 预约对象：
            1：不限制。
            2：仅限线上交易
            3：仅接受附近预约
            4：仅限女性预约
            5：仅限男性预约
            6：仅限好友预约
     */
	private Integer obj;
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
     * 已预约人数
     */
	@TableField("register_count")
	private Integer registerCount;
    /**
     * 已成功人数
     */
	@TableField("success_count")
	private Integer successCount;
    /**
     * 状态：
            1：已发布
            2：已取消
            3：已结束
     */
	private Integer status;
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

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSkillImagesUrl() {
		return skillImagesUrl;
	}

	public void setSkillImagesUrl(String skillImagesUrl) {
		this.skillImagesUrl = skillImagesUrl;
	}

	public String getSkillDetailImagesUrl() {
		return skillDetailImagesUrl;
	}

	public void setSkillDetailImagesUrl(String skillDetailImagesUrl) {
		this.skillDetailImagesUrl = skillDetailImagesUrl;
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

	public String getIntr() {
		return intr;
	}

	public void setIntr(String intr) {
		this.intr = intr;
	}

	public Integer getObj() {
		return obj;
	}

	public void setObj(Integer obj) {
		this.obj = obj;
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

	public Integer getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		return "Skill{" +
			"id=" + id +
			", userId=" + userId +
			", skill=" + skill +
			", level=" + level +
			", description=" + description +
			", skillImagesUrl=" + skillImagesUrl +
			", skillDetailImagesUrl=" + skillDetailImagesUrl +
			", unit=" + unit +
			", amount=" + amount +
			", intr=" + intr +
			", obj=" + obj +
			", lon=" + lon +
			", lat=" + lat +
			", registerCount=" + registerCount +
			", successCount=" + successCount +
			", status=" + status +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
