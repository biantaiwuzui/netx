package com.netx.shopping.vo;

import java.math.BigDecimal;
import java.util.Date;

public class QueryBossProductResponseDto {

    /**
     * 标识ID
     */
    private String id;
    /**
     * 供应商家
     */
    private String merchantName;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品描述
     */
    private String characteristic;

    /**
     * 是否配送
     */
    private Boolean isDelivery;
    /**
     * 是否支持退换
     */
    private Boolean isReturn;
    /**
     * 商品状态
     1：上架
     2：下架
     */
    private Integer onlineStatus;
    /**
     * 发布者昵称
     */
    private String nickName;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
