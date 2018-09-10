package com.netx.common.wz.dto.common;

import com.netx.common.common.enums.WorthIndexTypeEnum;
import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

public class WorthIndexRequestDto extends PageRequestDto {

    @ApiModelProperty("类型")
    private WorthIndexTypeEnum worthIndexTypeEnum;

    public WorthIndexTypeEnum getWorthIndexTypeEnum() {
        return worthIndexTypeEnum;
    }

    public void setWorthIndexTypeEnum(WorthIndexTypeEnum worthIndexTypeEnum) {
        this.worthIndexTypeEnum = worthIndexTypeEnum;
    }
}
