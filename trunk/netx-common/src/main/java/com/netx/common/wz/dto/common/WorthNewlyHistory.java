package com.netx.common.wz.dto.common;

import com.netx.common.common.enums.WorthTypeEnum;

import java.io.Serializable;

public class WorthNewlyHistory implements Serializable {

    /**
     * 网能id
     */
    private String id;

    /**
     * 网能类型
     */
    private WorthTypeEnum worthTypeEnum;

    /**
     * 用户id
     */
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorthTypeEnum getWorthTypeEnum() {
        return worthTypeEnum;
    }

    public void setWorthTypeEnum(WorthTypeEnum worthTypeEnum) {
        this.worthTypeEnum = worthTypeEnum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
