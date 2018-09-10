package com.netx.ucenter.vo.router;

import io.swagger.annotations.ApiModelProperty;

public class SearchWishRequestDto {
    @ApiModelProperty("业务id")
    private String id;

    @ApiModelProperty("主题")
    private String title;

    @ApiModelProperty("距离(单位:km)")
    private Double redaius;

    @ApiModelProperty("在线(单位:min)")
    private Long online;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("经度")
    private Double lon;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("当前页")
    private int currentPage;

    @ApiModelProperty("当前获取数据量")
    private int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRedaius() {
        return redaius;
    }

    public void setRedaius(Double redaius) {
        this.redaius = redaius;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
