package com.netx.common.vo.currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 网信法人代表审批请求参数对象
 * Date: 2017-09-01
 */
@ApiModel
public class CurrencyReviewApproveRequestDto {

    @ApiModelProperty("审批用户id")
    @NotNull(message = "审批用户id不能为空")
    private String userId;

    @ApiModelProperty("网信id")
    @NotBlank(message = "网信id不能为空")
    private String currencyId;

    @ApiModelProperty("审批结果, 1: 同意 2：拒绝")
    @NotNull(message = "审批结果不能为空，同意值为1，拒绝值为2")
    @Min(value = 1) @Max(value = 2)
    private Integer result;

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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
