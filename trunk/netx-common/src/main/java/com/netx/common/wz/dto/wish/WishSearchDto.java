package com.netx.common.wz.dto.wish;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class WishSearchDto extends PageRequestDto{

    @ApiModelProperty(value = "心愿标签")
    private List<String> wishLabels = new ArrayList<>();

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "心愿主题")
    private String title;

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热 <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.支持网信 <br>" +
            "4.信用 <br>" +
            "5.价格最低 ")
    private Integer sort = 0;

    @ApiModelProperty(value = "需求状态：<br>" +
            "           1.已发布<br>" +
            "           2.已取消<br>" +
            "           3.已关闭，即推荐人数不足50%<br>" +
            "           4.推荐成功<br>" +
            "           5.已失败，即筹款目标未达成<br>" +
            "           6.筹集目标达成，即心愿发起成功<br>" +
            "           7.已完成，即金额使用完毕")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private double lon;

    @ApiModelProperty(value = "纬度")
    private double lat;

    public List<String> getWishLabels() {
        return wishLabels;
    }

    public void setWishLabels(List<String> wishLabels) {
        this.wishLabels = wishLabels;
    }

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
