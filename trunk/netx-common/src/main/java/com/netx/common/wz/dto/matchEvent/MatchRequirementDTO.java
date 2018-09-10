package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * 参赛要求
 * Created by Yawn on 2018/8/4 0004.
 */
public class MatchRequirementDTO {
    @ApiModelProperty(value = "有id值情况表示更新，没有的话表示插入")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "赛组ID")
    private String groupId;
    /**
     * 要求名称
     */
    @ApiModelProperty(value = "要求名称")
    private String requirementName;
    /**
     * 是否需要上传资料
     */
    @ApiModelProperty(value = "是否需要上传资料")
    private Boolean isRequirementData;
    /**
     * 指定特征
     */
    @ApiModelProperty(value = "指定特征，要求写这里，不管下限，上限这些")
    private String requirementDesignation;
    /**
     * 要求上限
     */
    @ApiModelProperty(value = "要求上限")
    private String requirementUpperLimit;
    /**
     * 要求下限
     */
    @ApiModelProperty(value = "要求下限")
    private String requirementLowerLimit;
    /**
     * 排序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
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

    public void setRequirementData(Boolean requirementData) {
        isRequirementData = requirementData;
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
}
