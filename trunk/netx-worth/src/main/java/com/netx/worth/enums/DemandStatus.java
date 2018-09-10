package com.netx.worth.enums;


public enum DemandStatus {
    PUBLISHED(1, "已发布"),
    CANCEL(2, "已取消"),
    STOP(3, "已关闭"),
    START(4,"已结束报名"),
    CONFIRM(5,"已确认细节");
    public Integer status;
    public String description;

    private DemandStatus(Integer status, String description) {
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

    public static DemandStatus getStatus(int status) {
        switch (status) {
            case 1:
                return PUBLISHED;
            case 2:
                return CANCEL;
            case 3:
                return STOP;
            case 4:
                return START;
            default:
                return PUBLISHED;
        }
    }
}
