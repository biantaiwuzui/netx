package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WishPublishDto {
    @ApiModelProperty(value = "心愿ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String title;

    @ApiModelProperty(value = "标签，传ID，以逗号分隔")
    @NotBlank(message = "标签不能为空")
    private String wishLabel;

    @ApiModelProperty(value = "希望筹集的金额")
    @NotNull(message = "金额不能为空")
    @Range(min=0, max=1000000,message = "筹集金额必须大于0元，小于100万元")
    private BigDecimal amount;

    @ApiModelProperty(value = "推荐人，逗号分隔")
    @NotBlank(message = "标签不能为空")
    private String refereeIds;

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "图片2")
    private String pic2;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

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

    public String getWishLabel() {
        return wishLabel;
    }

    public void setWishLabel(String wishLabel) {
        this.wishLabel = wishLabel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRefereeIds() {
        return refereeIds;
    }

    public void setRefereeIds(String refereeIds) {
        this.refereeIds = refereeIds;
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

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
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
}
