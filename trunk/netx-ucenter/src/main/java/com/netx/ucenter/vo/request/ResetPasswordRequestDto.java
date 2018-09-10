package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class ResetPasswordRequestDto {

    @ApiModelProperty("登录名")
    @NotBlank(message = "登录名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录名至少4位,由大小写字母和数字,-,_组成")
    private String userName;

    @ApiModelProperty("登录密码")
    @NotBlank(message = "登录密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$", message = "密码至少6位,由大小写字母和数字,-,_组成")
    private String password;

    @ApiModelProperty("登录名")
    @NotBlank(message = "登录名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录名至少4位,由大小写字母和数字,-,_组成")
    private String superUserName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuperUserName() {
        return superUserName;
    }

    public void setSuperUserName(String superUserName) {
        this.superUserName = superUserName;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequestDto{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", superUserName='" + superUserName + '\'' +
                '}';
    }
}
