package com.netx.shopping.enums;

/**
 * Created By wj.liu
 * Description: 物流状态枚举
 * Date: 2017-09-14
 */
public enum LogisticsStatusEnum {

    ON(1, "物流中"),
    COMPLETED(2, "已完成");

    private Integer code;
    private String name;

    LogisticsStatusEnum(Integer code, String name){
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
