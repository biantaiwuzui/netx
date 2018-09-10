package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SkillRegisterDepositDto {
    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "预约表ID")
    @NotBlank(message = "预约表ID不能为空")
    private String skillRegisterId;

    @ApiModelProperty(value = "付款方式：0:网币 1:平台垫付")
    @NotNull(message = "付款方式不能为空")
    private Integer payWay;

    @ApiModelProperty(value = "付款金额不能为空")
    @NotNull(message = "付款金额不能为空")
    private BigDecimal bail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSkillRegisterId() {
        return skillRegisterId;
    }

    public void setSkillRegisterId(String skillRegisterId) {
        this.skillRegisterId = skillRegisterId;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }
}
