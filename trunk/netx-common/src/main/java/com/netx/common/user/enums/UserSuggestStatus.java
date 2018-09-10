package com.netx.common.user.enums;

public enum UserSuggestStatus {
    WAITING(null,"未审核"),
    ACCEPT(1, "有效"),
    REFUSE(0, "无效"),
    SHELVE(2, "搁置");

    public Integer isEffective;
    public String trading;


    private UserSuggestStatus(Integer isEffective, String trading) {
        this.isEffective = isEffective;
        this.trading = trading;
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
    }

    public String getTrading() {
        return trading;
    }

    public void setTrading(String trading) {
        this.trading = trading;
    }

    public static UserSuggestStatus getEffective(int isEffective) {
        UserSuggestStatus s = null;
        switch (isEffective) {
            case 1:
                s = ACCEPT;
                break;
            case 0:
                s = REFUSE;
                break;
            case 2:
                s = SHELVE;
                break;
            default:
                s = WAITING;
                break;
        }
        return s;
    }


}
