package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * <p>
 * 跨模块使用的技能类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-16
 */
public class SelectUserSkillResponse {
    @ApiModelProperty("技能id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("技能单id")
    private String skillId;

    @ApiModelProperty("技能标签，逗号分隔")
    private String skill;

    @ApiModelProperty("水平标签，逗号分隔")
    private String level;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("单价")
    private BigDecimal amount;
    /**
     * 状态：
     1：已发布
     2：已取消
     3：已结束
     */
    @ApiModelProperty("状态:1-已发布;2-已取消;3-已结束")
    private Integer status;

    @ApiModelProperty("开始时间")
    private long startAt;

    @ApiModelProperty("结束时间")
    private long endAt;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("预约数")
    private Integer registerCount;

    @ApiModelProperty("成功预约数")
    private Integer successCount;

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

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    @Override
    public String toString() {
        return "SelectUserSkillResponse{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skill='" + skill + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", pic='" + pic + '\'' +
                ", unit='" + unit + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", address='" + address + '\'' +
                ", registerCount=" + registerCount +
                ", successCount=" + successCount +
                '}';
    }
}
