package com.netx.worth.vo;


import io.swagger.annotations.ApiModelProperty;

public class MatchZoneVo {
    @ApiModelProperty(value = "赛区id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "赛区名称")
    private String zoneName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "赛区地址")
    private String zoneAdress;
    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    private String zoneSite;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "赛事id")
    private String matchId;
    @ApiModelProperty(value = "是否已经写完")
    private Boolean iswrite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneAdress() {
        return zoneAdress;
    }

    public void setZoneAdress(String zoneAdress) {
        this.zoneAdress = zoneAdress;
    }

    public String getZoneSite() {
        return zoneSite;
    }

    public void setZoneSite(String zoneSite) {
        this.zoneSite = zoneSite;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Boolean getIswrite() {
        return iswrite;
    }

    public void setIswrite(Boolean iswrite) {
        this.iswrite = iswrite;
    }
}
