package com.netx.shopping.enums;

public enum  MerchantManagerIsCurrentEnum {

    NO(0, "不是"),
    YES(1, "是");

    private Integer code;
    private String name;

    MerchantManagerIsCurrentEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
