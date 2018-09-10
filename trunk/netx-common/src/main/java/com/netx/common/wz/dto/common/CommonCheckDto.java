package com.netx.common.wz.dto.common;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CommonCheckDto {
    @ApiModelProperty(value = "主方验证还是客方验证，比如邀请人就是主方，被邀请人就是客方，再比如活动发布者为主方，活动报名者就是客方。千万不可传错，否则要影响结算。主方：0，客方：1")
    @NotNull(message = "验证方不能为空")
    private Integer fromOrTo;

    @ApiModelProperty(value = "业务ID，根据主、客方不同而不同，取值范围有：邀请表ID、活动发布表ID、活动报名表ID、需求发布表ID、需求报名表ID、技能发布表ID、技能报名表ID")
    @NotBlank(message = "业务ID不能为空")
    private String id;

    public Integer getFromOrTo() {
        return fromOrTo;
    }

    public void setFromOrTo(Integer fromOrTo) {
        this.fromOrTo = fromOrTo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
