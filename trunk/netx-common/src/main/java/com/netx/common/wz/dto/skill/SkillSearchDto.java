package com.netx.common.wz.dto.skill;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class SkillSearchDto extends PageRequestDto{

    @ApiModelProperty(value = "活动发起者id")
    private String userId;

    @ApiModelProperty(value = "技能标签")
    private List<String> skillLabels = new ArrayList<>();

    @ApiModelProperty(value = "水平标签")
    private List<String> levels = new ArrayList<>();

    @ApiModelProperty(value = "技能描述")
    private String description;

    @ApiModelProperty(value = "经度")
    private double lon;

    @ApiModelProperty(value = "纬度")
    private double lat;

    @ApiModelProperty(value = "技能状态：<br>" +
            "           1.已发布<br>" +
            "           2.已取消<br>" +
            "           3.已结束")
    private Integer status;

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热 <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.支持网信 <br>" +
            "4.信用 <br>" +
            "5.价格最低 <br>" +
            "6.报名人数多 <br>" +
            "7.成交量最多 ")
    private Integer sort = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getSkillLabels() {
        return skillLabels;
    }

    public void setSkillLabels(List<String> skillLabels) {
        this.skillLabels = skillLabels;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
