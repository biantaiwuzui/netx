package com.netx.common.wz.dto.gift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel("设置物流信息实体类")
public class GiftLogisticsDto {

    @NotBlank(message = "收礼人ID不能为空")
    @ApiModelProperty(value = "收礼人ID")
    private String toUserId;

    @NotBlank(message = "礼物ID不能为空")
    @ApiModelProperty(value = "礼物ID")
    private String giftId;

    @NotBlank(message = "物流信息不能为空")
    @ApiModelProperty(value = "物流信息，这个字段包含收货人姓名、地址、电话，以json存储到一起，前端自行安排json格式，展示时会原封不动get出来")
    private String address;

    @NotNull(message = "送达时间不能为空")
    @ApiModelProperty(value = "送达时间")
    private Long deliveryAt;

    @ApiModelProperty(value = "其它要求")
    private String message;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(Long deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
