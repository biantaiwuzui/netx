package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SkillPublishDto {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "技能标签，逗号分隔")
    @NotBlank(message = "技能标签不能为空")
    private String skill;

    @ApiModelProperty(value = "水平标签，逗号分隔")
    @NotBlank(message = "水平标签不能为空")
    private String level;

    @ApiModelProperty(value = "详细描述")
    @NotBlank(message = "详细描述不能为空")
    private String description;

    @ApiModelProperty(value = "图片")
    @NotBlank(message = "图片不能为空")
    private String pic;

    private String pic2;

    @ApiModelProperty(value = "单位，传汉字，如：小时、件、个。")
    @NotBlank(message = "单位不能为空")
    private String unit;

    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价不能为空")
    @Range(min=0, max=100000,message = "单价充值金额必须大于0元，小于10万元")
    private BigDecimal amount;

    @ApiModelProperty(value = "价格说明")
    private String intr;

    @ApiModelProperty(value = "预约对象：\n" +
            "     1：不限制。\n" +
            "     2：仅限线上交易\n" +
            "     3：仅接受附近预约\n" +
            "     4：仅限女性预约\n" +
            "     5：仅限男性预约\n" +
            "     6：仅限好友预约")
    @NotNull(message = "预约对象不能为空")
    @Min(value = 1, message = "预约对象不能小于1")
    private Integer obj;

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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIntr() {
        return intr;
    }

    public void setIntr(String intr) {
        this.intr = intr;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }
}
