package com.netx.shopping.vo;

import java.util.List;

public class CartMerchantResponseDto {

    private String merchantId;

    private String name;
    
    private double distance;

    private List<CartItemListResponseDto> products;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CartItemListResponseDto> getProducts() {
        return products;
    }

    public void setProducts(List<CartItemListResponseDto> products) {
        this.products = products;
    }
}
