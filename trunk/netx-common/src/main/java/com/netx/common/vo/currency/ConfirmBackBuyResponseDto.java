package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By liwei
 * Description: 网信回购请求参数对象
 * Date: 2017-10-31
 */
@ApiModel
public class ConfirmBackBuyResponseDto {

    @ApiModelProperty("回购id")
    @NotBlank(message = "回购id不能为空")
    private String id;

    @ApiModelProperty(required = true, value = "网信发布者用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("回购支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty("回购状态，2：同意 3：拒绝")
    @NotNull
    @Min(value = 2, message = "最小值是2")@Max(value=3, message = "最大值是3")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
}
