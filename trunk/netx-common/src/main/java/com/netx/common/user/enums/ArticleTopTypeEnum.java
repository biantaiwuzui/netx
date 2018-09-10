package com.netx.common.user.enums;

public enum ArticleTopTypeEnum {
    LIST_TOP("列表置顶", 1),
    CLASSIFICATION("分类置顶", 2);

    ArticleTopTypeEnum(String name, Integer value){
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
