package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class UpdateDeliverymanRequestDto {
    @ApiModelProperty(required = true, value = "用户id")
    @NotBlank(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty(value = "配送人员列表下标，0开始")
    private  long index;

    @ApiModelProperty(value = "配送人员详情")
    private List<String> deliveryman;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public List<String> getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(List<String> deliveryman) {
        this.deliveryman = deliveryman;
    }
}
