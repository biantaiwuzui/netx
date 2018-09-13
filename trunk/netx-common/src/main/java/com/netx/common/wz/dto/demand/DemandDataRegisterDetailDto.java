package com.netx.common.wz.dto.demand;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 具体的申请信息 发布者看的
 */
public class DemandDataRegisterDetailDto {

    @ApiModelProperty
    private Integer code;

    @ApiModelProperty
    private Integer amount;

    @ApiModelProperty("个性标签（以‘,’隔开）：性格")
    private List<String> tags;

    @ApiModelProperty(value = "当前预约地点和预约者申请时候的距离")
    private Double predistance;

    @ApiModelProperty(value = "order表的状态")
    private Integer order_status;

    @ApiModelProperty(value = "register表的状态")
    private Integer status;

    @ApiModelProperty(value = "当前用户和预约地点的距离")
    private Double distance;

    @ApiModelProperty(value = "需求申请单ID")
    private String id;

    @ApiModelProperty(value = "确认单ID")
    private String demandorderId;

    @ApiModelProperty(value = "商家消费id")
    private List<String> orderId;

    @ApiModelProperty(value = "需求ID")
    @NotBlank(message = "需求ID不能为空")
    private String demandId;

    @ApiModelProperty(value = "报名人ID")
    private String userId;

    @ApiModelProperty(value = "建议开始时间")
    private Long startAt;

    @ApiModelProperty(value = "建议结束时间")
    private Long endAt;

    @ApiModelProperty(value = "时间单位")
    private String unit;

    @ApiModelProperty(value = "请说明申请的目的、要求及优势、计费的依据等")
    private String description;

    @ApiModelProperty(value = "希望的报酬")
    private BigDecimal wage;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180, max = 180, message = "经度输入不在范围内")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90, max = 90, message = "纬度输入不在范围内")
    private BigDecimal lat;

    @ApiModelProperty(value = "是否匿名")
    private Boolean isAnonymity;

    @ApiModelProperty("商家消费")
    private BigDecimal order_price;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("视频信息")
    private String video;

    @ApiModelProperty("车辆信息")
    private String car;

    @ApiModelProperty("房产信息")
    private String house;

    @ApiModelProperty("学历信息")
    private String degree;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("申请时间")
    private Date createtime;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级")
    private Integer lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("发布者信用")
    private Integer credit;

    @ApiModelProperty("退款信息")
    private Object refund;

    private Boolean isEachWage;

    private BigDecimal publisterWage;

    private String about;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<String> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<String> orderId) {
        this.orderId = orderId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDemandorderId() {
        return demandorderId;
    }

    public void setDemandorderId(String demandorderId) {
        this.demandorderId = demandorderId;
    }

    public BigDecimal getPublisterWage() {
        return publisterWage;
    }

    public void setPublisterWage(BigDecimal publisterWage) {
        this.publisterWage = publisterWage;
    }

    public Boolean getEachWage() {
        return isEachWage;
    }

    public void setEachWage(Boolean eachWage) {
        isEachWage = eachWage;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getPredistance() {
        return predistance;
    }

    public void setPredistance(Double predistance) {
        this.predistance = predistance;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

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
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public BigDecimal getOrder_price() {
        return order_price;
    }

    public void setOrder_price(BigDecimal order_price) {
        this.order_price = order_price;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Object getRefund() {
        return refund;
    }

    public void setRefund(Object refund) {
        this.refund = refund;
    }
}
