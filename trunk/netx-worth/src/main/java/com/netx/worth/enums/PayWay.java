package com.netx.worth.enums;

public enum PayWay {
	ONLINE_CURRENCY(0, "网币"),
    MONEY(1, "现金");
    public Integer code;
    public String description;

    private PayWay(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
