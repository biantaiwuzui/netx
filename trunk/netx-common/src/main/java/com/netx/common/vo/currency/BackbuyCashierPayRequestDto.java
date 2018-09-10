package com.netx.common.vo.currency;

import com.netx.common.vo.common.FrozenAddRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * 收银人员支付回购者dto
 */
@ApiModel
public class BackbuyCashierPayRequestDto extends FrozenAddRequestDto {

    @ApiModelProperty("回购网信id")
    @NotBlank(message = "回购网信id不能为空")
    private String currency_Id;

    @ApiModelProperty("支付结果：1.同意    2.拒绝")
    @Range(min = 1,max = 2)
    private int result;

    public String getCurrency_Id() {
        return currency_Id;
    }

    public void setCurrency_Id(String currency_Id) {
        this.currency_Id = currency_Id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BackbuyCashierPayRequestDto{" +
                "currency_Id='" + currency_Id + '\'' +
                ", result=" + result +
                '}';
    }
}
