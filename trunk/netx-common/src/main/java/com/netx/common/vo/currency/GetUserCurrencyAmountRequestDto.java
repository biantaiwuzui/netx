package com.netx.common.vo.currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created By wj.liu
 * Description: 获取用户网信金额请求参数对象
 * Date: 2017-11-08
 */
@ApiModel
public class GetUserCurrencyAmountRequestDto {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "网信id")
    private String currencyId;

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
}
