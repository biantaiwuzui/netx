package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class OauthRequestDto {
    @ApiModelProperty(value = "openId")
    @NotBlank(message = "openId不能为空")
    private String openId;

    @ApiModelProperty(value = "登录类型：\n" +
            "1.微信\n" +
            "2.支付宝\n" +
            "3.微博\n" +
            "4.QQ")
    @NotNull(message = "登录类型不能为空")
    @Range(min = 1l,max = 4l,message = "没有你输入的数据类型")
    private int type;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OauthRequestDto{" +
                "openId='" + openId + '\'' +
                ", type=" + type +
                '}';
    }
}
