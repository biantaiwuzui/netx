package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Date 2018-1-16
 * @Author hj.Mao
 */
@ApiModel
public class WalletChangeWechatUsedAccountRequestDto {

    @ApiModelProperty("微信账号昵称,作为微信账号")
    private String nickname;

    @ApiModelProperty("openId")
    private String openId;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "WalletChangeWechatUsedAccountRequestDto{" +
                ", nickname='" + nickname + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
