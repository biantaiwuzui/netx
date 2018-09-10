package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * <p>
 * 跨模块使用的心愿类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-16
 */
public class SelectWishDetailDataResponseDto {
    @ApiModelProperty("心愿id")
    private String id;

    @ApiModelProperty("发布者id")
    private String userId;

    @ApiModelProperty("主题")
    private String title;

    @ApiModelProperty("希望筹集的金额")
    private BigDecimal amount;

    @ApiModelProperty("当前筹集数")
    private BigDecimal currentAmount;

    @ApiModelProperty("当前已使用金额")
    private BigDecimal currentApplyAmount;

    @ApiModelProperty("截至时间")
    private Long expiredAt;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("状态:1-已发布;2-已取消;3-已关闭，即推荐人数不足50%;4-推荐成功; 5-已失败，即筹款目标未达成;6-筹集目标达成，即心愿发起成功;7-已完成，即金额使用完毕")
    private Integer status;

    @ApiModelProperty("经纬度hash")
    private String geohash;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "标签",notes = "逗号分隔")
    private String wishLabel;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getCurrentApplyAmount() {
        return currentApplyAmount;
    }

    public void setCurrentApplyAmount(BigDecimal currentApplyAmount) {
        this.currentApplyAmount = currentApplyAmount;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
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

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
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

    public String getWishLabel() {
        return wishLabel;
    }

    public void setWishLabel(String wishLabel) {
        this.wishLabel = wishLabel;
    }

    @Override
    public String toString() {
        return "SelectWishDetailDataResponseDto{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", currentAmount=" + currentAmount +
                ", currentApplyAmount=" + currentApplyAmount +
                ", expiredAt=" + expiredAt +
                ", description='" + description + '\'' +
                ", pic='" + pic + '\'' +
                ", status=" + status +
                ", geohash='" + geohash + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", wishLabel='" + wishLabel + '\'' +
                '}';
    }
}
