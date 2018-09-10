package com.netx.common.wz.dto.common;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CommonSearchDto {
    @ApiModelProperty(value = "业务ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "当前页")
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage;

    @ApiModelProperty(value = "每页显示数量")
    @NotNull(message = "每页显示数量不能为空")
    @Max(value = 50, message = "每页显示数量最多为50")
    private Integer size;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    @ApiModelProperty(value = "距离，单位km")
    @Min(value = 1, message = "最少只能找附近1公里的事物")
    private Double length;
    @Min(value = 0, message = "半径不能小于0")
    @ApiModelProperty(value = "半径（仅心愿搜索需填）")
    private Double radius;
    @ApiModelProperty(value = "主题")
    private String title;
    @Min(value = -1, message = "在线时间不能小于 -1")
    @ApiModelProperty(value = "X分钟前在线，-1：不限，1：1分钟前在线，即当前在线。5：5分钟前在线。其它类推，写实际需要的分钟数")
    private Long minute;
    @ApiModelProperty(value = "性别，0:男、1：女")
    private String sex;
    @ApiModelProperty(value = "标签，逗号分隔")
    private String labels;
//    @ApiModelProperty(value = "状态。0：正在发起，1：已完成")
    @ApiModelProperty(value = "状态。1：已发布，2：已取消，3：已关闭，4：推荐成功，5")
    private Integer status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getMinute() {
        return minute;
    }

    public void setMinute(Long minute) {
        this.minute = minute;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
