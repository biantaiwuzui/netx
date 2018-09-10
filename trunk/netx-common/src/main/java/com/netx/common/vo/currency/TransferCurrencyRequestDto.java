package com.netx.common.vo.currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 网信转让请求参数对象
 * Date: 2017-09-03
 */
@ApiModel
public class TransferCurrencyRequestDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空 ")
    private String currencyId;

    @ApiModelProperty("发起转让用户id")
    @NotBlank(message = "发起转让用户id不能为空")
    private String launchUserId;

    @ApiModelProperty("转让网信金额")
    @NotNull
    @Min(value = 1, message = "转让网信金额不能小于1")
    private Long price;

    @ApiModelProperty("设定转让价格")
    @NotNull
    private Long setPrice;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getLaunchUserId() {
        return launchUserId;
    }

    public void setLaunchUserId(String launchUserId) {
        this.launchUserId = launchUserId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSetPrice() {
        return setPrice;
    }

    public void setSetPrice(Long setPrice) {
        this.setPrice = setPrice;
    }
}
