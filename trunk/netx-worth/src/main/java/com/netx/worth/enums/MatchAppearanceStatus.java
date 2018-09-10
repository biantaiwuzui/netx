package com.netx.worth.enums;

/**
 * 表演状态表
 * Created by Yawn on 2018/8/2 0002.
 */
public enum MatchAppearanceStatus {
    IN_PREPARATION(0, "已保存"),
    PERFORMING(1, "待审核"),
    FINISH_PERFORMANCE(2, "审核拒绝"),
    RATING(3, "审核通过"),
    RATING_END(4, "已启动");


    public Integer status;
    private String description;

    private MatchAppearanceStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

}
