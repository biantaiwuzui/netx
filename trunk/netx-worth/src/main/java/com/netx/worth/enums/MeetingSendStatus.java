package com.netx.worth.enums;

public enum MeetingSendStatus {
	WAITING(0, "待同意"),
    ACCEPT(1, "同意（已发起)"),
    REFUSE(2, "拒绝"),
    CANCEL(3, "已取消"),
	ATTEND(4,"已出席");
    public Integer status;
    private String description;

    private MeetingSendStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
