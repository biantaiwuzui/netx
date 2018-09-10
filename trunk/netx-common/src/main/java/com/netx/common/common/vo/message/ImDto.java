package com.netx.common.common.vo.message;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Map;

public class ImDto {

    @ApiModelProperty("事件id（无跳转类型不用传）")
    private String typeId;

    @ApiModelProperty("发送者id")
    private String fromUserId;

    @ApiModelProperty("接收者id")
    private String toUserId;

    @ApiModelProperty("信息")
    private String messagePayload;
    /**
     * 网值类型：
       网能：Activity
       网友：User
       网商：Product
       网信：Credit
     */
    @ApiModelProperty("网值类型")
    private MessageTypeEnum typeEnum;

    @ApiModelProperty("跳转参数")
    private Map<String,Object> pushParamsMap;

    @ApiModelProperty("信息类型")
    private PushMessageDocTypeEnum docTypeEnum;

    @ApiModelProperty("发送时间")
    private Date sendTime;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public MessageTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(MessageTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Map<String, Object> getPushParamsMap() {
        return pushParamsMap;
    }

    public void setPushParamsMap(Map<String, Object> pushParamsMap) {
        this.pushParamsMap = pushParamsMap;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }

    public PushMessageDocTypeEnum getDocTypeEnum() {
        return docTypeEnum;
    }

    public void setDocTypeEnum(PushMessageDocTypeEnum docTypeEnum) {
        this.docTypeEnum = docTypeEnum;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
