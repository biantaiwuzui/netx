package com.netx.worth.enums;

public enum WishHistoryStatus {
    WAITING(1, "待转账"),
    ACCEPT(2, "转账成功"),
    REFUSE(3, "转账失败");

    public Integer status;
    public String reason;

    private WishHistoryStatus(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public static WishHistoryStatus getStatus(int status) {
        WishHistoryStatus s = null;
        switch (status) {
            case 1:
                s = WAITING;
                break;
            case 2:
                s = ACCEPT;
                break;
            case 3:
                s = REFUSE;
                break;
            default:
                s = WAITING;
                break;
        }
        return s;
    }
}
