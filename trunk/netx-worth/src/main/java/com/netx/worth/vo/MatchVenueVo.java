package com.netx.worth.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class MatchVenueVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "有ID更新，否则插入")
    private String id;
    /**
     * 比赛id
     */
    private String zoneId;
    /**
     * 场次名称
     */
    private String title;
    /**
     * 赛制类型，填赛制ID
     */
    private MatchProgressVo MatchProgressVo;
    /**
     * 赛组的ID，多个逗号隔开
     */
    private List<GroupAndVenueVo> groupAndVenueVoList;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 比赛地址
     */
    private String address;
    /**
     * 比赛场地
     */
    private String site;
    /**
     * 比赛场地图片
     */
    private String siteImageUrl;
    /**
     * 用于规定顺序
     */
    private Integer sort;
    /**
     * 赛区名字
     */
    private String zoneName;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public com.netx.worth.vo.MatchProgressVo getMatchProgressVo() {
        return MatchProgressVo;
    }

    public void setMatchProgressVo(com.netx.worth.vo.MatchProgressVo matchProgressVo) {
        MatchProgressVo = matchProgressVo;
    }

    public List<GroupAndVenueVo> getGroupAndVenueVoList() {
        return groupAndVenueVoList;
    }

    public void setGroupAndVenueVoList(List<GroupAndVenueVo> groupAndVenueVoList) {
        this.groupAndVenueVoList = groupAndVenueVoList;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
