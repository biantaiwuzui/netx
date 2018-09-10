package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

@ApiModel
public class SendMessageToGroupChatUsersDto {

    @ApiModelProperty("群id")
    @NotBlank(message = "群id不能为空")
    private String id;

    @ApiModelProperty("当前发送消息的用户id")
    @NotBlank(message = "当前发送消息的用户id不能为空")
    private String userId;

    @ApiModelProperty("消息")
    @NotBlank(message = "消息不能为空")
    private String msg;

    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "额外参数,map")
    private Map pushParams;

    @ApiModelProperty(value = "额外参数,key为docType")
    private String docType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
