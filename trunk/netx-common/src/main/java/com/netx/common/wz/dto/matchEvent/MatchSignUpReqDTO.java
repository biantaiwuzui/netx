package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 添加报名要求
 * Created by Yawn on 2018/8/13 0013.
 */
public class MatchSignUpReqDTO {
    @ApiModelProperty(value = "有ID就更新，没有就插入")
    private String id;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    /**
     * 组别名称
     */
    @ApiModelProperty(value = "赛组名称")
    private String groupName;
    /**
     * 会场ID
     */
    @ApiModelProperty(value = "会馆ID")
    private String venueId;
    /**
     * 报名开始时间
     */
    @ApiModelProperty(value = "报名开始时间")
    private Date beginTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "报名结束时间")
    private Date endTime;
    /**
     * 描述
     */
    @ApiModelProperty(value = "报名描述，写成人组，儿童组这些")
    private String decription;
    /**
     * 报名名额
     */
    @ApiModelProperty(value = "报名名额")
    private Integer quota;
    /**
     * 报名费用
     */
    @ApiModelProperty(value = "报名费用")
    private Float free;
    /**
     * 是否系统筛选
     */
    @ApiModelProperty(value = "是否系统自动筛选成员")
    private Boolean isAutoSelect;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
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

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
