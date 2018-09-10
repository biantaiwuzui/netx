package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By liwei
 * Description: 网信支付接口请求参数
 * Date: 2017-10-16
 */
@ApiModel
public class CurrencyPayRequestDto {

    @ApiModelProperty(required = true, value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(required = true, value = "网信id")
    @NotBlank(message = "网信id不能为空")
    private String currencyId;

    @ApiModelProperty(required = true, value = "交易金额（以元为单位）")
    @NotNull(message = "交易金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(required = true, value = "交易描述")
    @NotBlank(message = "交易描述不能为空")
    private String description;

    @ApiModelProperty(value = "操作类型,1-收入;2-支出3-退回",required = true)
    @NotNull(message = "操作类型不能为空")
    private Integer type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
