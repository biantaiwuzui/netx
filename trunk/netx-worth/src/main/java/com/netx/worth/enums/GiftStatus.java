package com.netx.worth.enums;

public enum GiftStatus {
	SEND(1),
    ACCEPT(2),
    REFUSE(3);
    public Integer status;

    private GiftStatus(Integer status) {
        this.status = status;
    }

    public static GiftStatus getStatus(int status) {
    	GiftStatus s = null;
        switch (status) {
            case 1:
                s = SEND;
                break;
            case 2:
                s = ACCEPT;
                break;
            case 3:
                s = REFUSE;
                break;
            default:
                s = SEND;
                break;
        }
        return s;
    }
}
