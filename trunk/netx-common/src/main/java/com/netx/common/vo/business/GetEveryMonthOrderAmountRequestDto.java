package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 网商每个月订单交易额请求dto
 * @Authro hj.Mao
 * @Date 2017-11-21
 */

@ApiModel
public class GetEveryMonthOrderAmountRequestDto extends CommonUserIdRequestDto {

    @ApiModelProperty("开始时间，既是tais   private Date start")
    @NotNull(message = "开始时间不能为空")
    private Date start;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}
