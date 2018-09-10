package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Create on 17-11-20
 *
 * @author wongloong
 */
@ApiModel
public class LuckMoneyQueryDto {
    @ApiModelProperty
    private String time;

    @ApiModelProperty("状态 1.已审核 2.等待审核,3.明天生效")
    private int status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
