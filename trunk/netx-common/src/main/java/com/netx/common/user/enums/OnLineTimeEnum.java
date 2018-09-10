package com.netx.common.user.enums;

public enum OnLineTimeEnum {
    ON_LINE_TIME_ENUM(1,1000*60*30L);

    private int code;
    private Long time;

    OnLineTimeEnum(int code,Long time){
        this.code=code;
        this.time=time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
