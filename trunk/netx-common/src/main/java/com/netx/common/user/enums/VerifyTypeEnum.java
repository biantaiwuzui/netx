package com.netx.common.user.enums;

/**
 * 认证类型
 * @author 李卓
 */
public enum VerifyTypeEnum {
    IDCARD_TYPE("身份", 1),
    VIDEO_TYPE("视频", 2),
    CAR_TYPE("车辆", 3),
    HOUSE_TYPE("房产", 4),
    DEGREE_TYPE("学历", 5);

    VerifyTypeEnum(String name, Integer value){
        this.name = name;
        this.value = value;
    };
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

    public static VerifyTypeEnum getEnumByValue(int value) {
        for (VerifyTypeEnum c : VerifyTypeEnum.values()) {
            if (c.getValue() == value) {
                return c;
            }
        }
        return null;
    }


}
