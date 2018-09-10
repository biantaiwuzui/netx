package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class AddCreditRecordRequestDto extends AddWangMingRecordSuperRequestDto{

    @NotNull(message = "本笔信用不能为空")
    @ApiModelProperty("本笔信用，收入支持用正负号标识")
    private Integer credit;

    @NotBlank(message = "描述不能为空")
    @ApiModelProperty("描述")
    private String description;

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AddCreditRequestDto{" +
                "credit=" + credit +
                '}';
    }
}
