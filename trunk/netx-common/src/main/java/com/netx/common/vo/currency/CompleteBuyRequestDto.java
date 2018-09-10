package com.netx.common.vo.currency;

import com.netx.common.vo.common.FrozenAddRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 网信立即竞购接口请求参数
 * Date: 2017-09-27
 */
@ApiModel
public class CompleteBuyRequestDto extends FrozenAddRequestDto {


    @ApiModelProperty(required = true, value = "答题机会数")
    @NotNull(message = "答题机会数不能为空")
    @Min(value = 2, message = "至少购买两次")
    private Integer chanceNum;

    public Integer getChanceNum() {
        return chanceNum;
    }

    public void setChanceNum(Integer chanceNum) {
        this.chanceNum = chanceNum;
    }
}
