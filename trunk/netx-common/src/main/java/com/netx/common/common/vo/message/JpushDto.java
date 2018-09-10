package com.netx.common.common.vo.message;

import com.netx.common.common.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Create by wongloong on 17-8-22
 */
@ApiModel
public class JpushDto {
    @ApiModelProperty(value = "通知消息",required = true)
    private String alertMsg;
    @ApiModelProperty(value = "通知标题")
    private String title;
    @ApiModelProperty(value = "模块类型")
    private MessageTypeEnum type;
    @ApiModelProperty(value = "发送者")
    private String fromUserId;
    @ApiModelProperty(value = "登录时设置的标识",required = true)
    private String userId;
    @ApiModelProperty(value = "额外参数,map")
    private Map pushParams;
    @ApiModelProperty(value = "额外参数,key为docType")
    private String docType;

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map getPushParams() {
        return pushParams;
    }

    public void setPushParams(Map pushParams) {
        this.pushParams = pushParams;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public MessageTypeEnum getType() {
        return type;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Override
    public String toString() {
        return "JpushDto{"
                + "alertMsg='" + alertMsg
                + ", title='" + title
                + ", type=" + type
                + ", fromUserId='" + fromUserId
                + ", userId='" + userId
                + ", pushParams=" + pushParams
                + ", docType='" + docType
                + '}';
    }
}
