package com.netx.ucenter.vo.request;

import com.netx.common.user.enums.PWDType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 密码vo类
 * </p>
 *
 * @author 黎子安
 * @since 2017-9-1
 */
public class WzPassWordBaseRequest {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "密码类型")
    @NotNull(message = "密码类型不能为空")
    private PWDType type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PWDType getType() {
        return type;
    }

    public void setType(PWDType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WzPassWordBaseRequest{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                '}';
    }
}
