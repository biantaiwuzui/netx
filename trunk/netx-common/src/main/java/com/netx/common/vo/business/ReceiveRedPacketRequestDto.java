package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class ReceiveRedPacketRequestDto {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("红包发放id")
    @NotBlank(message = "红包发放id不能为空")
    private String redpacketSendId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRedpacketSendId() {
        return redpacketSendId;
    }

    public void setRedpacketSendId(String redpacketSendId) {
        this.redpacketSendId = redpacketSendId;
    }
}
