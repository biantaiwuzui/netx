package com.netx.shopping.model.merchantcenter.constants;

public enum  MerchantStatusEnum {

    NORMAL(1, "正常"),
    BACK(2, "黑名单");

    private int code;
    private String name;

    MerchantStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
