package com.netx.shopping.enums;

public enum MerchantVerifyStatusEnum {

    ADOPT("已通过验证"),
    NOTADOPT("未通过验证");

    private String name;

    MerchantVerifyStatusEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
