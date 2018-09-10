package com.netx.worth.enums;

public enum WishApplyStatus {
    WAITING(0,"待交易"),
    TRADING(1, "交易中"),
    ACCEPT(2, "交易成功"),
    REFUSE(3, "交易失败");

    public Integer status;
    public String trading;


    private WishApplyStatus(Integer status, String trading) {
        this.status = status;
        this.trading = trading;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTrading() {
        return trading;
    }

    public void setTrading(String trading) {
        this.trading = trading;
    }

    public static WishApplyStatus getStatus(int status) {
        WishApplyStatus s = null;
        switch (status) {
            case 1:
                s = TRADING;
                break;
            case 2:
                s = ACCEPT;
                break;
            case 3:
                s = REFUSE;
                break;
            case 0:
                s = WAITING;
                break;
            default:
                s = WAITING;
                break;
        }
        return s;
    }


}
