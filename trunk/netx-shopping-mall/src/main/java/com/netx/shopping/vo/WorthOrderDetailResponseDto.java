package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class WorthOrderDetailResponseDto{

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("注册者id")
    private String userId;

    @ApiModelProperty("收银员id")
    private String cashierId;

    @ApiModelProperty("商家二级类目")
    private List<String> tages;

    @ApiModelProperty("商家一级类目")
    private List<String> categories;

    @ApiModelProperty("商家信用")
    private Integer credit;

    @ApiModelProperty("是否支持网信")
    private Boolean isHoldCredit;

    @ApiModelProperty("首单红包")
    private BigDecimal redRate;

    @ApiModelProperty("距离")
    private Double district;

    @ApiModelProperty("商家地址")
    private String address;

    @ApiModelProperty("订单合计")
    private BigDecimal totalPrice;

    @ApiModelProperty("商家logo")
    private String merchantLogo;

    private List<OrderProductBean> productBeans;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public List<String> getTages() {
        return tages;
    }

    public void setTages(List<String> tages) {
        this.tages = tages;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }

    public BigDecimal getRedRate() {
        return redRate;
    }

    public void setRedRate(BigDecimal redRate) {
        this.redRate = redRate;
    }

    public Double getDistrict() {
        return district;
    }

    public void setDistrict(Double district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public List<OrderProductBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(List<OrderProductBean> productBeans) {
        this.productBeans = productBeans;
    }
}
