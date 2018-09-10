package com.netx.common.wz.dto.invitation;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class InvitationSendDto {

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String fromUserId;

    @ApiModelProperty(value = "被邀请人ID")
    @NotBlank(message = "被邀请人ID不能为空")
    private String toUserId;

    @ApiModelProperty(value = "邀请主题")
    @NotBlank(message = "邀请主题不能为空")
    private String title;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "开始时间")
    private Long startAt;

    @ApiModelProperty(value = "结束时间")
    private Long endAt;

    @ApiModelProperty(value = "报酬")
    @NotNull(message = "报酬不能为空")
    @Min(value = 1, message = "报酬必须大于1")
    private BigDecimal amout;
    @ApiModelProperty(value = "目的、内容、要求等")
    @NotBlank(message = "目的不能为空")
    private String description;

    @ApiModelProperty(value = "是否隐身邀请")
    @NotNull(message = "是否隐身邀请不能为空")
    private Boolean isAnonymity;

    @ApiModelProperty(value = "是否纯线上活动（无需地址）")
    @NotNull(message = "请选择地址")
    private Boolean isOnline;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    private BigDecimal lat;

    @ApiModelProperty(value = "来源ID，即从哪个资讯点进来的")
    private String articleId;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public BigDecimal getAmout() {
        return amout;
    }

    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
