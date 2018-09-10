package com.netx.common.user.dto.wangMing;

import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.enums.WangMingEnum;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SelectWangMingListRequestDto {

    @ApiModelProperty("网名枚举，值为：" +
            "‘①身价’，‘②收益’，‘③贡献’，‘④信用’，‘⑤积分’")
    @NotNull(message = "网名枚举不能为空")
    private WangMingEnum wangMingEnum;

    @Valid
    private CommonListDto commonListDto;

    public WangMingEnum getWangMingEnum() {
        return wangMingEnum;
    }

    public void setWangMingEnum(WangMingEnum wangMingEnum) {
        this.wangMingEnum = wangMingEnum;
    }

    public CommonListDto getCommonListDto() {
        return commonListDto;
    }

    public void setCommonListDto(CommonListDto commonListDto) {
        this.commonListDto = commonListDto;
    }
}
