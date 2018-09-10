package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By wj.liu
 * Description: 网信加减操作接口请求参数对象
 * Date: 2017-11-08
 */
@ApiModel
public class CurrencyOperateRequestDto {

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户Id不能为空")
    private String userId;

    @ApiModelProperty(value = "网信id", required = true)
    @NotBlank(message = "网信Id不能为空")
    private String currencyId;

    @ApiModelProperty(value = "网信金额（以元为单位）", required = true)
    @NotNull(message = "网信金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "操作类型,1-收入;2-支出3-退回",required = true)
    @NotNull(message = "操作类型不能为空")
    private Integer type;

    @ApiModelProperty(required = true, value = "交易描述")
    @NotBlank(message = "交易描述不能为空")
    private String description;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
