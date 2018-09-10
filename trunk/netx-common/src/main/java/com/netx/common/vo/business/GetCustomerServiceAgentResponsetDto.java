package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class GetCustomerServiceAgentResponsetDto {
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
     * 直接提成
     */
    @ApiModelProperty("直接提成")
    private BigDecimal directCommission;

    /**
     * 月提成
     */
    @ApiModelProperty("月提成")
    private BigDecimal monthIndirectCommission;

    /**
     * 累计提成
     */
    @ApiModelProperty("累计提成")
    private BigDecimal allIndirectCommission;

    /**
     * 客服组数
     */
    @ApiModelProperty("客服组数")
    private Integer customerServiceGrounp;

    /**
     * 当月向下发展的商家数量
     */
    @ApiModelProperty("当月向下发展的商家数量")
    private Integer newAddSellerNum;

    /**
     * 商家累计个数
     */
    @ApiModelProperty("商家累计个数")
    private Integer allSellerNum;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private String registerTime;

    /**
     * 名次
     */
    @ApiModelProperty("名次")
    private Integer areaRanking;

    /**
     *客服数量
     */
    @ApiModelProperty("客服数量")
    private Integer customerServicerNum;

    /**
     * 剩余天数
     */
    @ApiModelProperty("剩余天数")
    private Long surplusDay;

    /**
     * 商家id
     * @return
     */
    @ApiModelProperty("商家id")
    private String sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

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

    public BigDecimal getDirectCommission() {
        return directCommission;
    }

    public void setDirectCommission(BigDecimal directCommission) {
        this.directCommission = directCommission;
    }

    public BigDecimal getMonthIndirectCommission() {
        return monthIndirectCommission;
    }

    public void setMonthIndirectCommission(BigDecimal monthIndirectCommission) {
        this.monthIndirectCommission = monthIndirectCommission;
    }

    public BigDecimal getAllIndirectCommission() {
        return allIndirectCommission;
    }

    public void setAllIndirectCommission(BigDecimal allIndirectCommission) {
        this.allIndirectCommission = allIndirectCommission;
    }

    public Integer getCustomerServiceGrounp() {
        return customerServiceGrounp;
    }

    public void setCustomerServiceGrounp(Integer customerServiceGrounp) {
        this.customerServiceGrounp = customerServiceGrounp;
    }

    public Integer getNewAddSellerNum() {
        return newAddSellerNum;
    }

    public void setNewAddSellerNum(Integer newAddSellerNum) {
        this.newAddSellerNum = newAddSellerNum;
    }

    public Integer getAllSellerNum() {
        return allSellerNum;
    }

    public void setAllSellerNum(Integer allSellerNum) {
        this.allSellerNum = allSellerNum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getAreaRanking() {
        return areaRanking;
    }

    public void setAreaRanking(Integer areaRanking) {
        this.areaRanking = areaRanking;
    }

    public Integer getCustomerServicerNum() {
        return customerServicerNum;
    }

    public void setCustomerServicerNum(Integer customerServicerNum) {
        this.customerServicerNum = customerServicerNum;
    }

    public Long getSurplusDay() {
        return surplusDay;
    }

    public void setSurplusDay(Long surplusDay) {
        this.surplusDay = surplusDay;
    }
}
