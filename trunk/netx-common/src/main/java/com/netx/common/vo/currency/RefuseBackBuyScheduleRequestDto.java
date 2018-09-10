package com.netx.common.vo.currency;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class RefuseBackBuyScheduleRequestDto {

    @ApiModelProperty("网信id不能为空")
    @NotBlank(message = "网信id不能为空")
    private String currencyId;

    @ApiModelProperty("定时开关：true表示拒绝后首次启动的定时器，false表示第二次启动的定时器，用户扣除网信相关人员的钱包金额")
    @NotNull(message = "isFirst不能为空")
    private Boolean isFirst;

    @ApiModelProperty("回购的网信总金额")
    private BigDecimal allAmount;

    private BigDecimal remainderAmount;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean getFirst() { return isFirst; }

    public void setFirst(Boolean first) { isFirst = first; }

    public BigDecimal getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(BigDecimal allAmount) {
        this.allAmount = allAmount;
    }

    public BigDecimal getRemainderAmount() {
        return remainderAmount;
    }

    public void setRemainderAmount(BigDecimal remainderAmount) {
        this.remainderAmount = remainderAmount;
    }
}
