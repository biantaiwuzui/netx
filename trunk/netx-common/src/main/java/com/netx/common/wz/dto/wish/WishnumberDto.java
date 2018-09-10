package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class WishnumberDto {

    @ApiModelProperty(value = "网号")
    @NotBlank(message = "网号不能为空")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "WishnumberDto{" +
                "userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
