package com.netx.ucenter.vo.request;

/**
 * Create on 17-11-16
 *
 * @author wongloong
 */
public class GroupChatQueryByPwdRequest {
    private String userId;
    private String pwd;
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
