package com.netx.common.wz.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("注意，如果选择第二项‘解冻部分费用并退回给我，其余部分支付给Ta’，那么一定要先支付成功了才能调用该方法，并回填bail和payWay两个字段值")
public class CreateRefundDto {
//    @ApiModelProperty(value = "业务主键，取值范围：需求单ID和技能预约单ID")
    @ApiModelProperty(value = "需求订单id  DemandOrderId")
    @NotBlank(message = "业务主键不能为空")
    private String id;
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "退款费用  即退退给预约者的金额")
    @NotNull(message = "退款费用不能为空")
    @DecimalMin(value = "0.01", message = "退款金额不能小于1分钱")
    BigDecimal amount;
    @ApiModelProperty(value = "支付的金额  即支付给发布者的部分金额")
    BigDecimal bail;
    @ApiModelProperty(value = "支付方式：0：网币 1：现金")
    private Integer payWay;
    @ApiModelProperty(value = "退款理由")
    private String description;

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }
}
