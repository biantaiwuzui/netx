package com.netx.common.user.dto.userVerify;

import com.netx.common.user.dto.userManagement.SelectUserVerifyResourceResponse;

import java.util.List;

public class VerifyResponse {

    /**
     * 认证id
     */
    private String id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 认证信息
     */
    private String verifyMessage;

    /**
     * 认证状态
     */
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 认证资源
     */
    private List<SelectUserVerifyResourceResponse> list;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SelectUserVerifyResourceResponse> getList() {
        return list;
    }

    public void setList(List<SelectUserVerifyResourceResponse> list) {
        this.list = list;
    }
}
