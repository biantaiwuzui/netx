
package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class FrozenAddRequestDto {
    /**
     * 消费渠道活动,需求,心愿,技能,商品,网币,用类名表示
     */
    @ApiModelProperty(notes = "心愿:Wish,技能:Skill,活动:Meeting,需求:Demand,商品:Goods,网币:Currency", required = true)
    @NotBlank
    private String frozenType;
    /**
     * 冻结金额
     */
    @ApiModelProperty(notes = "冻结金额", required = true)
    @NotNull
    private BigDecimal amount;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank
    private String userId;
    @ApiModelProperty(value = "收钱用户id", required = true, notes = "如果此交易最终成功,那么冻结金额会进入此id所拥有的钱包,并生成流水记录")
    private String toUserId;
    /**
     * 描述
     */
    @ApiModelProperty(value = "冻结描述,如购买商品,参加xx活动", required = true)
    private String description;
    /**
     * frozenType事件id
     */
    @ApiModelProperty(value = "事件id", required = true)
    @NotBlank
    private String typeId;
    /**
     * 支付方式
     */
    @ApiModelProperty(value = "交易方式.0零钱,1网币,2混合支付",required = true)
    private Integer tradeType;

    /**
     * 网币id
     */
    @ApiModelProperty(value = "网币id,当支付方式为1&2的时候,必须填写")
    private String currencyId;
    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getFrozenType() {
        return frozenType;
    }

    public void setFrozenType(String frozenType) {
        this.frozenType = frozenType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

}
