package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 比赛分会场
 * Created by Yawn on 2018/8/1 0001.
 */
public class MatchVenueDTO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "有ID更新，否则插入")
    private String id;
    /**
     * 比赛id
     */
    @ApiModelProperty(value = "赛区ID")
    private String zoneId;
    /**
     * 场次名称
     */
    @ApiModelProperty(value = "场次名称")
    private String title;
    /**
     * 赛制类型，填赛制ID
     */
    @ApiModelProperty(value = "赛制类型，填赛制ID")
    private String kind;

    @ApiModelProperty(value = "赛组Ids(用逗号隔开)")
    private String groupIds;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 比赛地址
     */
    @ApiModelProperty(value = "比赛地址")
    private String address;
    /**
     * 比赛场地
     */
    @ApiModelProperty(value = "比赛场地")
    private String site;
    /**
     * 比赛场地图片
     */
    @ApiModelProperty(value = "选填，比赛场地图片")
    private String siteImageUrl;
    /**
     * 用于规定顺序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteImageUrl() {
        return siteImageUrl;
    }

    public void setSiteImageUrl(String siteImageUrl) {
        this.siteImageUrl = siteImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }
}
