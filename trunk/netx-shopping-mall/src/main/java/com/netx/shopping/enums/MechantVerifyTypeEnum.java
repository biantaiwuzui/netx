package com.netx.shopping.enums;

public enum MechantVerifyTypeEnum {

    IDCARD("身份证"),
    PHONE("手机号码");

    private String name;

    MechantVerifyTypeEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
