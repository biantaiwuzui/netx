package com.netx.worth.enums;

/**
 * 扫码出席类型
 * Created by Yawn on 2018/9/1 0001.
 */
public enum MatchAttendType {
    PARTICIPANT(1, "参赛者"),
    AUDNENT(2, "观众");

    public Integer type;
    public String description;

    MatchAttendType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }
}
