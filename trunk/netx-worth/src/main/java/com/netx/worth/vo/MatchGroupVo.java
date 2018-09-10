package com.netx.worth.vo;


import io.swagger.annotations.ApiModelProperty;

public class MatchGroupVo {

    private String id;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "赛事id")
    private String matchId;
    /**
     * 赛组名称
     */
    @ApiModelProperty(value = "赛组名称")
    private String matchGroupName;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty("是否已经填写")
    private boolean isWirte;
    @ApiModelProperty("参赛人数")
    private Integer quota;
    @ApiModelProperty("费用")
    private Float free;
    @ApiModelProperty("是否自动筛选")
    private Boolean isAutoSelect;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean isWirte() {
        return isWirte;
    }

    public void setWirte(boolean wirte) {
        isWirte = wirte;
    }
}
