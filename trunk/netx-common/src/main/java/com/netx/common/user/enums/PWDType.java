package com.netx.common.user.enums;

public enum PWDType {
    USER_TYPE("password"),
    PAY_TYPE("payPassword"),
    ADMIN_TYPE("adminPassword"),
    ADMIN_FORGET_TYPE("adminPassword");
    private String code;
    private PWDType(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}