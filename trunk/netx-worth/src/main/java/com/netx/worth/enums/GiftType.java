package com.netx.worth.enums;


public enum GiftType {
	MONEY(1),
    CURRENCY(2),
    GOODS(3);
    public Integer giftType;

    private GiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public static GiftType getGiftType(int giftType) {
        GiftType g = null;
        switch (giftType) {
            case 1:
                g = MONEY;
                break;
            case 2:
                g = CURRENCY;
                break;
            case 3:
                g = GOODS;
                break;
            default:
                g = MONEY;
                break;
        }
        return g;
    }
}
