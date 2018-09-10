package com.netx.common.wz.dto.demand;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DemandPublishDto {
    @ApiModelProperty(value = "需求ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String title;

    @ApiModelProperty(value = "需求分类\n" +
            "        1：技能\n" +
            "        2：才艺\n" +
            "        3：知识\n" +
            "        4：资源")
    @NotNull(message = "需求分类不能为空")
    private Integer demandType;

    @ApiModelProperty(value = "标签，传ID，以逗号分隔")
    @NotBlank(message = "标签不能为空")
    private String demandLabel;

    @ApiModelProperty(value = "是否长期有效")
    @NotNull(message = "是否长期有效")
    private Boolean isOpenEnded;

    @ApiModelProperty(value = "开始时间")
    private Long startAt;

    @ApiModelProperty(value = "结束时间")
    private Long endAt;

    @ApiModelProperty(value = "时间单位")
    private String unit;

    @ApiModelProperty(value = "时间要求：只有大概的要求，如：50天内、仅限周末等")
    private String about;

    @ApiModelProperty(value = "需求人数")
    @NotNull(message = "需求人数不能为空")
    @Min(value = 1, message = "需求人数要大于0")
    private Integer amount;

    @ApiModelProperty(value = "报名对象：\n" +
            "     * 1：不限制。\n" +
            "     * 2：仅限女性报名\n" +
            "     * 3：仅限男性报名\n" +
            "     * 4：仅允许我的好友报名\n" +
            "     * 5：仅限指定人员报名")
    @NotNull(message = "报名对象不能为空")
    private Integer obj;

    @ApiModelProperty(value = "指定报名对象列表，逗号分隔")
    private String objList;

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

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "活动图片")
    private String pic;

    @ApiModelProperty(value = "详情图片")
    private String merchantId;

    @ApiModelProperty(value = "订单ID，逗号分隔")
    private String orderIds;

    @ApiModelProperty(value = "订单消费")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "如发生消费，是否由我全额承担")
    private Boolean isPickUp;

    @ApiModelProperty(value = "报酬，根据isEachWage判断是总报酬还是单位报酬")
    @Range(min=0, max=100000,message = "报酬金额必须大于0元，小于10万元")
    private BigDecimal wage;

    @ApiModelProperty(value = "是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬")
    private Boolean isEachWage;

    @ApiModelProperty(value = "托管保证金")
    @NotNull(message = "托管保证金不能为空")
    @Min(value = 0, message = "不能低于0元")
    private BigDecimal bail;


    @ApiModelProperty(value = "是否支付（托管）")
    private Boolean isPay;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDemandType() {
        return demandType;
    }

    public void setDemandType(Integer demandType) {
        this.demandType = demandType;
    }

    public String getDemandLabel() {
        return demandLabel;
    }

    public void setDemandLabel(String demandLabel) {
        this.demandLabel = demandLabel;
    }

    public Boolean getOpenEnded() {
        return isOpenEnded;
    }

    public void setOpenEnded(Boolean openEnded) {
        isOpenEnded = openEnded;
    }

    public Long getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public String getObjList() {
        return objList;
    }

    public void setObjList(String objList) {
        this.objList = objList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Boolean getPickUp() {
        return isPickUp;
    }

    public void setPickUp(Boolean pickUp) {
        isPickUp = pickUp;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public Boolean getEachWage() {
        return isEachWage;
    }

    public void setEachWage(Boolean eachWage) {
        isEachWage = eachWage;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
