package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class JMssageHistoryRequestDto {
    @ApiModelProperty(value = "发送者id")
    @NotBlank(message = "发送者id不能为空")
    private String sendId;

    @ApiModelProperty(value = "接收者id")
    @NotBlank(message = "接收者id不能为空")
    private String reviceId;

    @ApiModelProperty(value = "是否为即时聊天，群聊传false")
    @NotNull(message = "类型不能为空")
    private Boolean type;

    @ApiModelProperty(value = "消息")
    @NotBlank(message = "消息不能为空")
    private String message;

    @ApiModelProperty(value = "发送时间")
    @NotNull(message = "发送时间不能为空")
    private Long sendTime;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReviceId() {
        return reviceId;
    }

    public void setReviceId(String reviceId) {
        this.reviceId = reviceId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }
}
