package com.netx.common.wz.dto.common;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class CommonPayCallbackDto {
  @ApiModelProperty("事件ID，即冻结时填写的事件ID。")
  private String id;
  @ApiModelProperty("支付用户ID")
  private String userId;
  @ApiModelProperty("支付方式：0：网币支付，1：自己钱包支付，2：冻结款支付")
  private String payType;
  @ApiModelProperty("支付金额")
  private BigDecimal amount;
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getPayType()
  {
    return this.payType;
  }
  
  public void setPayType(String payType)
  {
    this.payType = payType;
  }
  
  public BigDecimal getAmount()
  {
    return this.amount;
  }
  
  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }
}
