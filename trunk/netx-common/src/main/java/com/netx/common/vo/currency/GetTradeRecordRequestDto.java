package com.netx.common.vo.currency;


import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class GetTradeRecordRequestDto extends PageRequestDto {
    /**
     * 用户id
     */
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 交易的网信id
     */
    @ApiModelProperty("目标网信ID")
    @NotBlank(message = "网信ID不能为空")
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
