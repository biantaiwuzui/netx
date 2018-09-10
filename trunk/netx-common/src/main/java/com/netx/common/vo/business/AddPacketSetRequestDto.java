package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class AddPacketSetRequestDto {

    @ApiModelProperty("网商红包Id,不是必填，如果是插入的话，忽略这个属性，要是进行修改的话，传递一个红包ID")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 是否变动提成
     */
    @ApiModelProperty(value = "是否变动提成,这个是单选框的name属性\r\n" +
            "1.变动提成\r\n0.固定提成\r\n" +
            "如果值为1,firstRate,gradualRate,limitRate必填\r\n" +
            "如果为0，fixedRate必填")
    @NotNull
    @Range(max = 1)
    private Integer changeRate;

    @ApiModelProperty("首单提成比例")
    private BigDecimal firstRate;

    @ApiModelProperty("逐单提成比例")
    private BigDecimal gradualRate;

    @ApiModelProperty("封顶提成比例")
    private BigDecimal limitRate;

    @ApiModelProperty("固定提成比例")
    private BigDecimal fixedRate;

    @ApiModelProperty("是否每天启动红包金额" +
            "1.是    0.否")
    private Integer startPacket;

    @ApiModelProperty("启动红包金额")
    @NotNull(message = "启动红包金额不能为空")
    private Integer packetMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(Integer changeRate) {
        this.changeRate = changeRate;
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public BigDecimal getGradualRate() {
        return gradualRate;
    }

    public void setGradualRate(BigDecimal gradualRate) {
        this.gradualRate = gradualRate;
    }

    public BigDecimal getLimitRate() {
        return limitRate;
    }

    public void setLimitRate(BigDecimal limitRate) {
        this.limitRate = limitRate;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public Integer getStartPacket() {
        return startPacket;
    }

    public void setStartPacket(Integer startPacket) {
        this.startPacket = startPacket;
    }

    public Integer getPacketMoney() {
        return packetMoney;
    }

    public void setPacketMoney(Integer packetMoney) {
        this.packetMoney = packetMoney;
    }
}
