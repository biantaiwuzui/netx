package com.netx.worth.enums;

public enum WishAuthorizeStatus {
	WAITING(0, "待批准"),
    ACCEPT(1, "批准"),
    REFUSE(2, "拒绝");

    public Integer status;
    public String description;

    private WishAuthorizeStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static WishAuthorizeStatus getStatus(int status) {
    	WishAuthorizeStatus s = null;
        switch (status) {
            case 0:
                s = WAITING;
                break;
            case 1:
                s = ACCEPT;
                break;
            case 2:
                s = REFUSE;
                break;
            default:
                s = WAITING;
                break;
        }
        return s;
    }
}
