package com.netx.common.user.enums;

/**
 * 咨讯软文类型枚举
 * @author 李卓
 */
public enum ArticleAdvertorialTypeEnum {

    NORMAL_ARTICLE("普通咨讯（不是软文）", 0),
    NORMAL_ADVERTORIAL("普通软文", 1),
    ILLEGAL_ADVERTORIAL("违规软文(发布者不声明软文)", 2);

    ArticleAdvertorialTypeEnum(String name, Integer value){
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
