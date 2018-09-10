package com.netx.common.user.enums;

/**
 * 咨讯设置枚举
 * （隐私设置）
 * @author 李卓
 */
public enum ArticleSettingEnum {

    NOTHING("不起任何作用", 0),
    FRIENDS("仅限好友查看（包括指定好友）", 1);

    ArticleSettingEnum(String name, Integer value){
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
