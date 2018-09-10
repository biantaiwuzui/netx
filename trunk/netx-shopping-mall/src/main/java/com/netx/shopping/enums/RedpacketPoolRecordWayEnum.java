package com.netx.shopping.enums;

public enum  RedpacketPoolRecordWayEnum {
    INCOME(1,"支入"),
    EXPEND(2,"支出");

    private int code;
    private String name;

    RedpacketPoolRecordWayEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
