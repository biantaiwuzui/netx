package com.netx.common.wz.dto.matchEvent;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;

/**
 * 赛区的dto
 */
public class MatchZoneDto {
    @ApiModelProperty(value = "(更新的时候有就传)")
    private String id;
    /**
     * 分区名称
     */
    @ApiModelProperty(value = "赛区名称")
    private String zoneName;
    /**
     * 分区地址
     */
    @ApiModelProperty(value = "赛区地址")
    private String zoneAdress;
    /**
     * 分区地区
     */
    @ApiModelProperty(value = "赛区场地")
    private String zoneSite;
    /**
     * 排序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;
    @ApiModelProperty(value = "赛事id")
    private String matchId;

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
}
