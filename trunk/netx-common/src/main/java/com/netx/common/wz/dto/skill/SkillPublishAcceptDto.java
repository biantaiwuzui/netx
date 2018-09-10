package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SkillPublishAcceptDto {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "技能表ID")
    @NotBlank(message = "技能表ID不能为空")
    private String skillId;

    @ApiModelProperty(value = "建议开始时间")
    @NotNull(message = "开始时间不能为空")
    private Long startAt;

    @ApiModelProperty(value = "建议结束时间")
    private Long endAt;

    @ApiModelProperty(value = "单位")
    @NotBlank(message = "单位不能为空")
    private String unit;

    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private Integer number;

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "是否隐身，true、false")
    private Boolean isAnonymity;
}
