package com.netx.common.wz.dto.gift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * GiftController的List方法，以后的命名规则都如此，就不再赘述。
 */
@ApiModel("礼物实体类")
public class GiftListDto {

    @ApiModelProperty(value = "送礼人")
    private String fromUser;

    @ApiModelProperty(value = "收礼人")
    private String toUser;
    @ApiModelProperty(value = "礼物类型：1：红包，2：网币，3：商品")
    private Integer giftType;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "要求发货时间")
    private Integer deliveryAt;

    @ApiModelProperty(value = "订单ID")
    private String relatableId;

    @ApiModelProperty(value = "留言")
    private String description;

    @ApiModelProperty(value = "是否隐身赠送")
    private Boolean isAnonymity;

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(Integer deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public String getRelatableId() {
        return relatableId;
    }

    public void setRelatableId(String relatableId) {
        this.relatableId = relatableId;
    }
}
