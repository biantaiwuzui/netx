package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class GetSellerAgentResponsetDto {
    /**
     * 商务代理名称
     */
    @ApiModelProperty("商务代理名称")
    private String sellerName;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String areaName;

    /**
     * 月业绩
     */
    @ApiModelProperty("月业绩")
    private Integer monthAchievement;

    /**
     * 累计业绩
     */
    @ApiModelProperty("累计业绩")
    private Long allAchievement;

    /**
     * 客服组数
     */
    @ApiModelProperty("客服组数")
    private Integer customerServiceGrounp;

    /**
     * 客服个数
     */
    @ApiModelProperty("客服个数")
    private Integer customerServiceNum;

    /**
     * 商家个数
     */
    @ApiModelProperty("商家个数")
    private Integer sellerNum;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private String registerTime;

    /**
     * 区域名次
     */
    @ApiModelProperty("区域名次")
    private Long areaRanking;

    /**
     *国家名次
     */
    @ApiModelProperty("国家名次")
    private Long countryRanking;

    /**
     * 新增客服数
     */
    @ApiModelProperty("新增客服数")
    private Integer newAddCustomerServiceNum;

    /**
     * 新增商家数
     */
    @ApiModelProperty("新增商家数")
    private Integer  newAddSellerNum;

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


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getMonthAchievement() {
        return monthAchievement;
    }

    public void setMonthAchievement(Integer monthAchievement) {
        this.monthAchievement = monthAchievement;
    }

    public Long getAllAchievement() {
        return allAchievement;
    }

    public void setAllAchievement(Long allAchievement) {
        this.allAchievement = allAchievement;
    }

    public Integer getCustomerServiceGrounp() {
        return customerServiceGrounp;
    }

    public void setCustomerServiceGrounp(Integer customerServiceGrounp) {
        this.customerServiceGrounp = customerServiceGrounp;
    }

    public Integer getCustomerServiceNum() {
        return customerServiceNum;
    }

    public void setCustomerServiceNum(Integer customerServiceNum) {
        this.customerServiceNum = customerServiceNum;
    }

    public Integer getSellerNum() {
        return sellerNum;
    }

    public void setSellerNum(Integer sellerNum) {
        this.sellerNum = sellerNum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Long getAreaRanking() {
        return areaRanking;
    }

    public void setAreaRanking(Long areaRanking) {
        this.areaRanking = areaRanking;
    }

    public Long getCountryRanking() {
        return countryRanking;
    }

    public void setCountryRanking(Long countryRanking) {
        this.countryRanking = countryRanking;
    }

    public Integer getNewAddCustomerServiceNum() {
        return newAddCustomerServiceNum;
    }

    public void setNewAddCustomerServiceNum(Integer newAddCustomerServiceNum) {
        this.newAddCustomerServiceNum = newAddCustomerServiceNum;
    }

    public Integer getNewAddSellerNum() {
        return newAddSellerNum;
    }

    public void setNewAddSellerNum(Integer newAddSellerNum) {
        this.newAddSellerNum = newAddSellerNum;
    }
}
