package com.netx.common.wz.dto.meeting;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MeetingSearchDto extends PageRequestDto{

    @ApiModelProperty(value = "活动发起者id")
    private String userId;

    @ApiModelProperty(value = "活动主题")
    private String title;

    @ApiModelProperty(value = "活动标签")
    private List<String> meetingLabels = new ArrayList<>();

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热(报名人数多) <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.支持网信 <br>" +
            "4.信用 <br>" +
            "5.价格最低 ")
    private Integer sort = 0;

    @ApiModelProperty(value = "活动形式：<br>" +
            "1：活动，即1对1 <br>" +
            "2：聚合，即多对多 <br>" +
            "3：纯线上活动 <br>" +
            "4：不发生消费的线下活动")
    private Integer meetingType;

    @ApiModelProperty(value = "活动状态：<br>" +
            "            0：已发起，报名中<br>" +
            "            1：报名截止，已确定入选人<br>" +
            "            2：活动取消<br>" +
            "            3：活动失败<br>" +
            "            4：活动成功<br>" +
            "            5：同意开始，分发验证码<br>" +
            "            6：无人验证通过，活动失败")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private double lon;
    @ApiModelProperty(value = "纬度")
    private double lat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMeetingLabels() {
        return meetingLabels;
    }

    public void setMeetingLabels(List<String> meetingLabels) {
        this.meetingLabels = meetingLabels;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
