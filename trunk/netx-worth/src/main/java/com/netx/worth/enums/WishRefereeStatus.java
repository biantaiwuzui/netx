package com.netx.worth.enums;

public enum WishRefereeStatus {
	WAITING(0, "待确认"),
    ACCEPT(1, "同意"),
    REFUSE(2, "拒绝"),
    CANCEL(3, "弃权");

    public Integer status;
    public String description;

    private WishRefereeStatus(Integer status, String description) {
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

    public static WishRefereeStatus getStatus(int status) {
    	WishRefereeStatus s = null;
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
            case 3:
                s = CANCEL;
                break;
            default:
                s = WAITING;
                break;
        }
        return s;
    }
}
