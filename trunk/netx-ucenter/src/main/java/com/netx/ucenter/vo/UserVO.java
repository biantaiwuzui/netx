package com.netx.ucenter.vo;

import com.netx.ucenter.model.user.User;

/**
 * Created by 85169 on 2017/12/4.
 */
public class UserVO extends User {

    /**
     * 头像
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
