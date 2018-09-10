package com.netx.shopping.enums;

/**
 * Created By wj.liu
 * Description: 网商状态枚举
 * Date: 2017-09-05
 */
public enum SellerStatusEnum {

    NORMAL(1, "正常"),
    BACK(2, "黑名单");

    private int code;
    private String name;

    SellerStatusEnum(int code, String name){
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
