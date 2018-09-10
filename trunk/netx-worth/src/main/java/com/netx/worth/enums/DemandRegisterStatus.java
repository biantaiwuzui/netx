package com.netx.worth.enums;


public enum DemandRegisterStatus {
    REGISTERED(0, "已报名，未入选"),
    SUCCESS(1, "已入选"),
    CANCEL(2, "已取消"),
    FAIL(3, "未入选"),
    START(4, "已启动需求"),
    DROP(5, "放弃参与，即已入选放弃"),
    TIMEOUT(6, "超时未启动"),
	PUBLISH_CANCEL(7,"发布者取消入选者的需求"),
	CLOSE(8,"已结束");
    public Integer status;
    private String description;

    private DemandRegisterStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
