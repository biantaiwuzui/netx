package com.netx.common.user.enums;

/**
 * 咨讯类型枚举
 * @author 李卓
 */
public enum ArticleTypeEnum {

    IMAGE_ARTICLE("图文类型", 1),
    AUDIO_VIDEO("音视类型", 2);

    ArticleTypeEnum(String name, Integer value){
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
