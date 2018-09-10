package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 密码vo类
 * </p>
 *
 * @author 黎子安
 * @since 2017-9-1
 */
public class WzPassWordRequest extends WzPassWordBaseRequest{


    @ApiModelProperty(value = "旧密码：修改管理员密码必填")
    private String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "WzPassWordRequest{" +
                ", password='" + super.getPassword() + '\'' +
                ", type='" + super.getType() + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                '}';
    }
}
