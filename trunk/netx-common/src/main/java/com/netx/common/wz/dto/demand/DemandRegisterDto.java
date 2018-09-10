package com.netx.common.wz.dto.demand;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class DemandRegisterDto {
    @ApiModelProperty(value = "需求预约单ID")
    private String id;

    @ApiModelProperty(value = "需求ID")
    @NotBlank(message = "需求ID不能为空")
    private String demandId;

    @ApiModelProperty(value = "报名人ID")
    private String userId;

    @ApiModelProperty(value = "建议开始时间")
    private Long startAt;

    @ApiModelProperty(readOnly = true)
    private Date startDate;

    @ApiModelProperty(value = "建议结束时间")
    private Long endAt;

    @ApiModelProperty(readOnly = true)
    private Date endDate;

    @ApiModelProperty(value = "时间单位")
    private String unit;

    @ApiModelProperty(value = "建议时间要求：只有大概范围，如：50天内、仅限周末等，具体的时间待申请成功后再与发布者协商确定")
    private String about;

    @ApiModelProperty(value = "请说明申请的目的、要求及优势、计费的依据等")
    private String description;

    @ApiModelProperty(value = "希望的报酬")
    private BigDecimal wage;

    @ApiModelProperty(readOnly = true)
    private Long wageLong;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180,max = 180,message = "经度输入不在范围内")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90,max = 90,message = "纬度输入不在范围内")
    private BigDecimal lat;

    @ApiModelProperty(value = "是否匿名")
    private Boolean isAnonymity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
        if(startAt!=null){
            this.startDate = new Date(startAt);
        }
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
        if(endAt!=null){
            this.endDate = new Date(endAt);
        }
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Long getWageLong() {
        return wageLong;
    }

    public void setWageLong(Long wageLong) {
        this.wageLong = wageLong;
    }
}
