package com.netx.common.vo.common;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

public class GetUserListRequestDto extends CommonListDto {

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("用户手机号")
    private String mobile;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
