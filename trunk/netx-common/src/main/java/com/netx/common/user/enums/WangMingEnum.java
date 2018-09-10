package com.netx.common.user.enums;

/**
 * 网名枚举：身价、收益、贡献、信用、积分（不含商家）
 */
public enum WangMingEnum {

    VALUE("身价"), INCOME("收益"), CONTRIBUTION("贡献"), CREDIT("信用"), SCORE("积分");

    WangMingEnum(String name){
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
