package com.netx.common.wz.dto.demand;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class DemandSearchDto extends PageRequestDto{

    @ApiModelProperty(value = "需求发起者id")
    private String userId;

    @ApiModelProperty(value = "需求主题")
    private String title;

    @ApiModelProperty(value = "需求标签")
    private List<String> demandLabels = new ArrayList<>();

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热 <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.支持网信 <br>" +
            "4.信用 <br>" +
            "5.价格最高 ")
    private Integer sort = 0;

    @ApiModelProperty(value = "需求分类：<br>" +
            "1：技能 <br>" +
            "2：才艺 <br>" +
            "3：知识 <br>" +
            "4：资源")
    private Integer demandType;

    @ApiModelProperty(value = "需求状态：<br>" +
            "            1：已发布<br>" +
            "            2：已取消<br>" +
            "            3：已关闭<br>")
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

    public List<String> getDemandLabels() {
        return demandLabels;
    }

    public void setDemandLabels(List<String> demandLabels) {
        this.demandLabels = demandLabels;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getDemandType() {
        return demandType;
    }

    public void setDemandType(Integer demandType) {
        this.demandType = demandType;
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
