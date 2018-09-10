package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class AddDeliverymanRequestDto {
    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty("配送人员信息")
    private List<String> deliveryman;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<String> getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(List<String> deliveryman) {
        this.deliveryman = deliveryman;
    }
}
