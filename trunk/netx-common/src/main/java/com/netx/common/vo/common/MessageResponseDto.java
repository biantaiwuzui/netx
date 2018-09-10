package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.Map;

public class MessageResponseDto {

    @ApiModelProperty("信息id")
    private String id;

    @ApiModelProperty("模块类型")
    private String type;

    @ApiModelProperty("事件id")
    private String typeId;

    @ApiModelProperty("内容")
    private String messagePayload;

    @ApiModelProperty("信息类型")
    private String docType;

    @ApiModelProperty("跳转参数")
    private Map<String,Object> pushParamsMap;

    @ApiModelProperty("发送者id")
    private String userId;

    @ApiModelProperty("发送时间")
    private Date sendTime;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Map<String, Object> getPushParamsMap() {
        return pushParamsMap;
    }

    public void setPushParamsMap(Map<String, Object> pushParamsMap) {
        this.pushParamsMap = pushParamsMap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setISRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
