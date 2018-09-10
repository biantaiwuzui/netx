package com.netx.shopping.enums;

public enum  MerchantManagerEnum {

    CASHIER("收银人员"),
    MANAGER("业务主管");

    private String name;

    MerchantManagerEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
