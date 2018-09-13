package com.netx.worth.enums;

/**
 * Created by Yawn on 2018/8/6 0006.
 */
public enum MatchMemberKind {
    SPONSOR(0, "发起人"),
    HOST(1, "主持人"),
    STAFF(2, "其他工作人员"),
    AUDIT_PASS(3, "会场管理员"),
    GUEST(4, "嘉宾"),
    RATER(5, "评委"),
    OTHER(6, "其他人员");

    public Integer status;
    public String description;

    private MatchMemberKind(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
