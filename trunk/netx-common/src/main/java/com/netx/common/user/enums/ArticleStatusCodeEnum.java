package com.netx.common.user.enums;

/**
 * 咨讯状态码枚举
 * @author 李卓
 */
public enum ArticleStatusCodeEnum {

    NORMAL("正常", 0),
    EXCEPTION("异常", 1),
    PENDING("待审核", 2),
    UNPAID_AMOUNT("待交押金", 3),
    NOT_SUFFICIENT_FUNDS("押金余额不足", 4);

    ArticleStatusCodeEnum(String name, Integer value){
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
