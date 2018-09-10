package com.netx.ucenter.vo.request;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class QueryUserVerifyListRequestDto extends CommonListDto {

    @ApiModelProperty(value = "手机号码,模糊可以随意")
    @Pattern(regexp = "^[0-9]*[1-9][0-9]*$|^$",message = "手机号码只能能为数字")
    @Size(max = 15,message = "手机号码最多15位")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "网号")
    @Pattern(regexp = "^[0-9]*[1-9][0-9]*$|^$",message = "网号只能能为数字")
    @Size(max = 10,message = "网号最多10位")
    private String userNumber;

    @ApiModelProperty(value = "认证状态")
    private Integer status;

    @ApiModelProperty(value = "认证状态")
    private Integer verifyType;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public String toString() {
        return "QueryUserVerifyListRequestDto{" +
                "mobile='" + mobile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", status=" + status +
                ", verifyType=" + verifyType +
                '}';
    }
}
