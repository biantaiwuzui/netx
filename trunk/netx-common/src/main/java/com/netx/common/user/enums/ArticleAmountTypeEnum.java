package com.netx.common.user.enums;

/**
 * 咨讯押金类型
 */
public enum ArticleAmountTypeEnum {

    NOTHING("没交押金", 0),
    CHANGE("零钱", 1),
    CURRENCY("网币", 2);

    ArticleAmountTypeEnum(String name, Integer value){
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
