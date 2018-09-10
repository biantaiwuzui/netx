package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by Yawn on 2018/9/8 0008.
 */
public class MatchNoticeDTO {
    @ApiModelProperty(value = "ID，有更新，没插入")
    private String id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 公共内容
     */
    @ApiModelProperty(value = "公共内容")
    private String afficheContent;
    /**
     * 賽事id
     */
    @ApiModelProperty(value = "賽事id")
    private String matchId;
    /**
     * 發佈公告者id
     */
    @ApiModelProperty(value = "發佈公告者id")
    private String userId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间，后端生成")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间,后端生成")
    private Date updateTime;
    /**
     * 發佈公告的商家id
     */
    @ApiModelProperty(value = "發佈公告的商家id")
    private String merchantId;
    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    /**
     * 商家类型
     */
    @ApiModelProperty(value = "商家类型，不用传")
    private String merchantType;
    /**
     * 消息类型/0/1/2/3/4越小优先级越高
     */
    @ApiModelProperty(value = "消息类型/0/1/2/3/4越小优先级越高" ,notes = "你们想怎么弄就怎么弄")
    private Integer messageType;


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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
}
