package com.netx.ucenter.vo.response;

import com.netx.ucenter.model.user.User;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户基本信息Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-9-22
 */
public class SelectUserResponse{
    @ApiModelProperty("用户信息")
    private User user;

    @ApiModelProperty("头像")
    private String headImg;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "SelectUserResponse{" +
                "user=" + user.toString() +
                ", headImg='" + headImg + '\'' +
                '}';
    }
}
