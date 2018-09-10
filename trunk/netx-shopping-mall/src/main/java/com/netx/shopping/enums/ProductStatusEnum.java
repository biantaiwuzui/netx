package com.netx.shopping.enums;

/**
 * Created By wj.liu
 * Description: 商品状态枚举
 * Date: 2017-09-14
 */
public enum ProductStatusEnum {

    UP(1, "上架"),
    DOWN(2, "下架"),
    FORCEDOWN(3,"强制下架");

    private Integer code;
    private String name;

    ProductStatusEnum(Integer code, String name){
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
