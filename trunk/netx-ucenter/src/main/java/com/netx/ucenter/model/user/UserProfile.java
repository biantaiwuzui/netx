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
 * 用户详情表
凡是标签，均以字符形式存，以逗号分隔
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("user_profile")
public class UserProfile extends Model<UserProfile> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 常驻
     */
	@TableField("often_in")
	private String oftenIn;
    /**
     * 家乡
     */
	@TableField("home_town")
	private String homeTown;
    /**
     * 去过
     */
	@TableField("already_to")
	private String alreadyTo;
    /**
     * 想去
     */
	@TableField("want_to")
	private String wantTo;
    /**
     * 地址
     */
	private String address;
    /**
     * 介绍
     */
	private String introduce;
    /**
     * 性格
     */
	private String disposition;
    /**
     * 外貌
     */
	private String appearance;
    /**
     * 收入
     */
	private Integer income;
    /**
     * 最大工资
     */
	@TableField("max_income")
	private Integer maxIncome;
    /**
     * 情感
     */
	private String emotion;
    /**
     * 身高
     */
	private Integer height;
    /**
     * 体重
     */
	private Integer weight;
    /**
     * 民族
     */
	private String nation;
    /**
     * 属相
     */
	@TableField("animal_signs")
	private String animalSigns;
    /**
     * 星座
     */
	@TableField("star_sign")
	private String starSign;
    /**
     * 血型
     */
	@TableField("blood_type")
	private String bloodType;
    /**
     * 图文详情
     */
	private String description;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOftenIn() {
		return oftenIn;
	}

	public void setOftenIn(String oftenIn) {
		this.oftenIn = oftenIn;
	}

	public String getHomeTown() {
		return homeTown;
	}

	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}

	public String getAlreadyTo() {
		return alreadyTo;
	}

	public void setAlreadyTo(String alreadyTo) {
		this.alreadyTo = alreadyTo;
	}

	public String getWantTo() {
		return wantTo;
	}

	public void setWantTo(String wantTo) {
		this.wantTo = wantTo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getAppearance() {
		return appearance;
	}

	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}

	public Integer getIncome() {
		return income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	public Integer getMaxIncome() {
		return maxIncome;
	}

	public void setMaxIncome(Integer maxIncome) {
		this.maxIncome = maxIncome;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAnimalSigns() {
		return animalSigns;
	}

	public void setAnimalSigns(String animalSigns) {
		this.animalSigns = animalSigns;
	}

	public String getStarSign() {
		return starSign;
	}

	public void setStarSign(String starSign) {
		this.starSign = starSign;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "UserProfile{" +
			"id=" + id +
			", userId=" + userId +
			", oftenIn=" + oftenIn +
			", homeTown=" + homeTown +
			", alreadyTo=" + alreadyTo +
			", wantTo=" + wantTo +
			", address=" + address +
			", introduce=" + introduce +
			", disposition=" + disposition +
			", appearance=" + appearance +
			", income=" + income +
			", maxIncome=" + maxIncome +
			", emotion=" + emotion +
			", height=" + height +
			", weight=" + weight +
			", nation=" + nation +
			", animalSigns=" + animalSigns +
			", starSign=" + starSign +
			", bloodType=" + bloodType +
			", description=" + description +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
