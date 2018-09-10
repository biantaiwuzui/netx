package com.netx.worth.enums;

public enum WishApplyType {
	SELF(0, "提现"),
    USER(1, "给平台网友"),
    OTHER(2, "给第三方");
    public int type;
    private String description;

    private WishApplyType(int type, String description) {
        this.description = description;
        this.type = type;
    }
}
