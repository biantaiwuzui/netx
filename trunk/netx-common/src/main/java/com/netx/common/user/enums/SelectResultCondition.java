package com.netx.common.user.enums;

public enum SelectResultCondition {
    NOT_USER_HEAD("不含用户头像",1),
    NOT_USER_PHOTO("不含用户图片",2),
    NOT_USER_TAG("不含用户标签",3),
    NOT_USER_GEO("不含用户位置",4);

    SelectResultCondition(String condition, Integer type) {
        this.condition = condition;
        this.type = type;
    }

    private String condition;

    private Integer type;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
