package com.netx.shopping.vo;

import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class FinishCartRequestDto {

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @Valid
    @ApiModelProperty("购物车清单")
    @NotNull(message = "购物车清单不能为空")
    @Size(min = 1,message = "结算商品最少一件")
    private List<CartRequestDto> dtos;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<CartRequestDto> getDtos() {
        return dtos;
    }

    public void setDtos(List<CartRequestDto> dtos) {
        this.dtos = dtos;
    }
}
