package com.netx.worth.enums;

public enum MeetingRegisterStatus {
	REGISTERD(1, "已报名"),
    SUCCESS(2, "已入选"),
    FAIL(3, "未入选"),
    CANCEL(4, "已取消"),
    START(5, "确认出席，准备校验验证码");
    public Integer status;
    private String ddescription;

    private MeetingRegisterStatus(Integer status, String description) {
        this.status = status;
        this.ddescription = description;
    }
}
