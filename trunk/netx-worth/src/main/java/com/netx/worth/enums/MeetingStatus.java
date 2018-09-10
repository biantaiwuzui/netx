package com.netx.worth.enums;

public enum MeetingStatus {
	WAITING(0, "已发起，报名中"),
    STOP(1, "报名截止，已确定入选人"),
    CANCEL(2, "活动取消"),
    FAIL(3, "活动失败"),
    SUCCESS(4, "活动成功"),
    CODE_GENERATOR(5, "同意开始，分发验证码"),
    NO_VALID(6, "无人验证通过，活动失败");

    public Integer status;
    public String description;

    private MeetingStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
