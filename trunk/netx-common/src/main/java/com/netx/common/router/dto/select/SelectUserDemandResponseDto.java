package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 跨模块使用的需求类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-16
 */
public class SelectUserDemandResponseDto {

    @ApiModelProperty("业务id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("需求id")
    private String demandId;

    @ApiModelProperty("主题")
    private String title;

    @ApiModelProperty("标签，逗号分隔")
    private String demandLabel;

    @ApiModelProperty("开始时间")
    private Long startAt;

    @ApiModelProperty("结束时间")
    private Long endAt;

    @ApiModelProperty("大概时间")
    private String about;

    @ApiModelProperty("需求人数")
    private Integer amount;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("活动图片")
    private String pic;

    @ApiModelProperty("状态:1-已发布,2-已取消,3-已关闭")
    private Integer status;

    @ApiModelProperty("报名人数")
    private Long registerCount;

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

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDemandLabel() {
        return demandLabel;
    }

    public void setDemandLabel(String demandLabel) {
        this.demandLabel = demandLabel;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Long registerCount) {
        this.registerCount = registerCount;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
