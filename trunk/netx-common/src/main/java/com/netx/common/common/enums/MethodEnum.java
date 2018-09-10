package com.netx.common.common.enums;

public enum MethodEnum {

    POST("post"),
    GET("get"),
    PUT("put"),
    OPTIONS("options");

    private String name;

    MethodEnum(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public boolean eq(String method){
        return name.equalsIgnoreCase(method);
    }
}
