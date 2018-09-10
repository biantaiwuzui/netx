package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by Yawn on 2018/9/8 0008.
 */
public class MatchNoticeVo {
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公共内容")
    private String afficheContent;
    /**
     * 發佈公告者名称
     */
    @ApiModelProperty(value = "發佈公告者名称")
    private String userName;
    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 發佈公告的商家id
     */
//    private String merchantId;
//    /**
//     * 商家名称
//     */
//    private String merchantName;
//    /**
//     * 商家类型
//     */
//    private String merchantType;
    /**
     * 消息类型/0/1/2/3/4越小优先级越高
     */
    @ApiModelProperty(value = "消息类型/0/1/2/3/4越小优先级越高")
    private Integer messageType;

    /**
     * 查看数
     */
    @ApiModelProperty(value = "查看数")
    private Integer watchNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAfficheContent() {
        return afficheContent;
    }

    public void setAfficheContent(String afficheContent) {
        this.afficheContent = afficheContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Integer watchNum) {
        this.watchNum = watchNum;
    }
}
