package com.netx.worth.enums;

public enum RefundStatus {
	REQUEST(0, "已申请退款"),
    ACCEPT(1, "已同意同款"),
    REFUSE(2, "已拒绝退款"),
    EXPIRED(3, "超期自动同意退款");


    public Integer status;
    public String description;

    private RefundStatus(Integer status, String description) {
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

    public static RefundStatus getStatus(int status) {
    	RefundStatus s = null;
        switch (status) {
            case 0:
                s = REQUEST;
                break;
            case 1:
                s = ACCEPT;
                break;
            case 2:
                s = REFUSE;
                break;
            case 3:
                s = EXPIRED;
                break;
            default:
                s = REQUEST;
                break;
        }
        return s;
    }
}
