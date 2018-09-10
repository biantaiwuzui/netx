package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GetSellerResponseDto {
    /**
     * 客服代理名称
     */
    @ApiModelProperty("客服代理名称")
    private String customerServicerName;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String areaName;

    /**
     * 提成
     */
    @ApiModelProperty("提成")
    private BigDecimal Commission;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private String registerTime;

    /**
     * 剩余天数
     */
    @ApiModelProperty("剩余天数")
    private Long surplusDay;

    public String getCustomerServicerName() {
        return customerServicerName;
    }

    public void setCustomerServicerName(String customerServicerName) {
        this.customerServicerName = customerServicerName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public BigDecimal getCommission() {
        return Commission;
    }

    public void setCommission(BigDecimal commission) {
        Commission = commission;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Long getSurplusDay() {
        return surplusDay;
    }

    public void setSurplusDay(Long surplusDay) {
        this.surplusDay = surplusDay;
    }
}
