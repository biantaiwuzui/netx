package com.netx.common.vo.common;

/**
 * 仲裁查询返回的vo类
 * @Author haojun
 * @Date create by 2017/9/30
 */
public class ArbitrationSelectResponseVo {

    /**
     * 主键ID
     */
    private String arbitrationId;

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 投诉的主题
     */
    private String theme;

    /**
     * 投诉的时间
     */
    private Long time;

    /**
     * 投诉的理由
     */
    private String reason;

    public String getArbitrationId() {
        return arbitrationId;
    }

    public void setArbitrationId(String arbitrationId) {
        this.arbitrationId = arbitrationId;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
