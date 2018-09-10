package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 网信报名申购定时任务请求dto
 * @Author hj.Mao
 * @Date  2017-12-4
 */
@ApiModel
public class CurrencyEnrollApplyAutoRequestDto {

    @ApiModelProperty("报名申购优惠记录ID")
    @NotBlank(message = "报名申购优惠记录ID不能为空")
    private String enrollApplyRecordId;


    public String getEnrollApplyRecordId() {
        return enrollApplyRecordId;
    }

    public void setEnrollApplyRecordId(String enrollApplyRecordId) {
        this.enrollApplyRecordId = enrollApplyRecordId;
    }
}
