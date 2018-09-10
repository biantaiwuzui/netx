package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 网信保荐请求参数对象
 * Date: 2017-09-02
 */
@ApiModel
public class RecommendCurrencyRequestDto {

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
    private String currencyId;

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("保荐结果 1：同意，2：拒绝")
    @NotNull(message = "保荐结果不能为空，同意值为1，拒绝值为2")
    @Min(value = 1)@Max(value = 2)
    private Integer result;

    @ApiModelProperty("保荐意见")
    @NotBlank(message = "保荐意见不能为空")
    private String advice;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
