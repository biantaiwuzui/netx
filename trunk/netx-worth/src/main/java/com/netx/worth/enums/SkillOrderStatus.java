package com.netx.worth.enums;

public enum SkillOrderStatus {
	INIT(0, "初始化"),
    START(1, "已开始"),
    CANCEL(2, "已取消"),
    SUCCESS(3, "已成功"),
    FAIL(4, "已失败");
    public Integer status;
    public String description;

    private SkillOrderStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
