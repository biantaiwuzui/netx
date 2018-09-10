package com.netx.utils.session;

import java.io.Serializable;

/**
 *
 * @author Runshine
 * @since 2015-8-13
 * @version 1.0.0
 *
 */
public class LoginUser implements Serializable{
    private static final long serialVersionUID = 1L;

    private int userId;

    private String userName;

    private String nickname;

    private String headPicUrl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

}
