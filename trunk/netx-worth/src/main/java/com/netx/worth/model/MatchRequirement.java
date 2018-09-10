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
 * 参赛要求
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_requirement")
public class MatchRequirement extends Model<MatchRequirement> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 赛组ID
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 要求名称
     */
	@TableField("requirement_name")
	private String requirementName;
    /**
     * 是否需要上传资料
     */
	@TableField("is_requirement_data")
	private Boolean isRequirementData;
    /**
     * 指定特征
     */
	@TableField("requirement_designation")
	private String requirementDesignation;
    /**
     * 要求上限
     */
	@TableField("requirement_upper_limit")
	private String requirementUpperLimit;
    /**
     * 要求下限
     */
	@TableField("requirement_lower_limit")
	private String requirementLowerLimit;
	private Integer sort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRequirementName() {
		return requirementName;
	}

	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}

	public Boolean getRequirementData() {
		return isRequirementData;
	}

	public void setRequirementData(Boolean isRequirementData) {
		this.isRequirementData = isRequirementData;
	}

	public String getRequirementDesignation() {
		return requirementDesignation;
	}

	public void setRequirementDesignation(String requirementDesignation) {
		this.requirementDesignation = requirementDesignation;
	}

	public String getRequirementUpperLimit() {
		return requirementUpperLimit;
	}

	public void setRequirementUpperLimit(String requirementUpperLimit) {
		this.requirementUpperLimit = requirementUpperLimit;
	}

	public String getRequirementLowerLimit() {
		return requirementLowerLimit;
	}

	public void setRequirementLowerLimit(String requirementLowerLimit) {
		this.requirementLowerLimit = requirementLowerLimit;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchRequirement{" +
			"id=" + id +
			", groupId=" + groupId +
			", requirementName=" + requirementName +
			", isRequirementData=" + isRequirementData +
			", requirementDesignation=" + requirementDesignation +
			", requirementUpperLimit=" + requirementUpperLimit +
			", requirementLowerLimit=" + requirementLowerLimit +
			", sort=" + sort +
			"}";
	}
}
