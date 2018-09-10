package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * 赛组
 * Created by Yawn on 2018/8/13 0013.
 */
public class MatchGroupDTO {
    @ApiModelProperty(value = "没有ID就插入，有就更新")
    private String id;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛id")
    private String matchId;
    /**
     * 赛组名称
     */
    @ApiModelProperty(value = "赛组名称")
    private String matchGroupName;

    @ApiModelProperty(value = "顺序")
    private Integer sort;
    /**
     * 整个大赛各区的参赛人数
     */
    @ApiModelProperty(value = "人数")
    private Integer quota;
    /**
     * 比赛费用
     */
    @ApiModelProperty(value = "比赛费用")
    private Float free;
    /**
     * 是否自动筛选
     */
    @ApiModelProperty(value = "是否自动筛选")
    private Boolean isAutoSelect;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Float getFree() {
        return free;
    }

    public void setFree(Float free) {
        this.free = free;
    }

    public Boolean getAutoSelect() {
        return isAutoSelect;
    }

    public void setAutoSelect(Boolean autoSelect) {
        isAutoSelect = autoSelect;
    }
}
