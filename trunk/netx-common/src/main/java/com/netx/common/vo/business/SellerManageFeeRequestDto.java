package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 商家管理费处理请求参数对象
 * Date: 2017-12-01
 */
@ApiModel
public class SellerManageFeeRequestDto {

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "商家id", required = true)
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty(value = "缴费有效期,0：终生;其他传年数", required = true)
    @NotNull(message = "缴费有效期不能为空")
    @Min(value = 0,message = "有效期不能小于0")
    private Integer effectiveTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
