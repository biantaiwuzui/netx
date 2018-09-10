package com.netx.common.common.enums;

/**
 * Create by wongloong on 17-9-1
 */
public enum TagsEnum {
    INLINE("内置标签",0),
    SKILL("技能标签",1),
    HOBBY("兴趣标签",2),
    GOOD("商品标签",3)
    ;

    private String value;
    private Integer code;

    TagsEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

}
