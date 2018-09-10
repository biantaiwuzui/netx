package com.netx.common.wz.dto.gift;

//import com.sy.worth.rpc.model.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GiftSendDto {
    @ApiModelProperty(value = "礼物表ID")
    private String id;

    @ApiModelProperty(value = "礼物标题")
    @NotBlank(message = "礼物标题不能为空")
    private String title;

    @ApiModelProperty(value = "送礼人ID")
    @NotBlank(message = "送礼人ID不能为空")
    private String fromUserId;

    @ApiModelProperty(value = "收礼人ID")
    @NotBlank(message = "收礼人ID不能为空")
    private String toUserId;

    @ApiModelProperty(value = "礼物类型：\n" +
            "1：红包\n" +
            "2：网币\n" +
            "3：商品")
    @NotNull(message = "礼物类型不能为空")
    private Integer giftType;

    @ApiModelProperty(value = "关联ID，红包ID、网币ID、订单ID，根据不同赠送类型来传")
    private String relatableId;

    @ApiModelProperty(value = "网币或红包金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "留言")
    private String description;

    @ApiModelProperty(value = "是否隐身赠送")
    @NotNull(message = "是否隐身不能为空")
    private Boolean isAnonymity;

    @ApiModelProperty(value = "要求送达时间")
    private Long deliveryAt;

    @ApiModelProperty(value = "来源ID，即从哪个资讯点进来的")
    private String articleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public String getRelatableId() {
        return relatableId;
    }

    public void setRelatableId(String relatableId) {
        this.relatableId = relatableId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Long getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(Long deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
