package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 未成年人表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-29
 */
@TableName("match_child_info")
public class MatchChildInfo extends Model<MatchChildInfo> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 监护人ID
     */
	@TableField("participant_id")
	private String participantId;
    /**
     * 姓名
     */
	private String name;
	@TableField("head_image")
	private String headImage;
    /**
     * 年龄
     */
	private Integer age;
    /**
     * 性别
     */
	private String sex;
    /**
     * 孩子介绍
     */
	private String introduction;
    /**
     * 照片
     */
	@TableField("images_url")
	private String imagesUrl;
    /**
     * 其他要求
     */
	@TableField("other_requirement")
	private String otherRequirement;

	@TableField("user_id")
	private String userId;

	private String mobile;




	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImagesUrl() {
		return imagesUrl;
	}

	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}

	public String getOtherRequirement() {
		return otherRequirement;
	}

	public void setOtherRequirement(String otherRequirement) {
		this.otherRequirement = otherRequirement;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchChildInfo{" +
			"id=" + id +
			", participantId=" + participantId +
			", name=" + name +
			", headImage=" + headImage +
			", age=" + age +
			", sex=" + sex +
			", introduction=" + introduction +
			", imagesUrl=" + imagesUrl +
			", otherRequirement=" + otherRequirement +
			"}";
	}
}
