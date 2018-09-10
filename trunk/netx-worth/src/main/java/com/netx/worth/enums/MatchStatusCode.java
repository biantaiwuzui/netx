package com.netx.worth.enums;

/**
 * Created by Yawn on 2018/8/1 0001.
 */
public enum MatchStatusCode {
    SAVE(0, "已保存"),
    REVIEW_PENDING(1, "待审核"),
    REVIEW_REJECTED(2, "审核拒绝"),
    AUDIT_PASS(3, "审核通过（启动中，购票和报名都还未开始）"),
    ALL_START(4, "启动了，购票和报名都在进行中"),
    APPLY_END(5, "报名结束"),
    TICKET_END(6, "购票结束"),
    TICKET_APPLY_END(7, "购票和报名都结束(全面进入进行中)"),
    END(8, "赛事结束");

    public Integer status;
    private String description;

    private MatchStatusCode(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

}
