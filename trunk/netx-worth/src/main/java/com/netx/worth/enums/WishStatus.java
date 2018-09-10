package com.netx.worth.enums;

public enum WishStatus {
	PUBLISHED(1, "已发布"),
    CANCEL(2, "已取消"),
    CLOSE(3, "已关闭，即推荐人数不足50%"),
    REFEREE_SUCCESS(4, "推荐成功"),
    FAIL(5, "已失败，即筹款目标未达成"),
    SUCCESS(6, "筹集目标达成，即心愿发起成功"),
    COMPLETE(7, "已完成，即金额使用完毕"),
	UNDERWAY(8,"推荐中，即有人推荐，但是还没有推荐完成");

    public Integer status;
    public String description;

    private WishStatus(Integer status, String description) {
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
}
