package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-1
 */
@ApiModel
public class TagsScopeChangeRequestDto {
    @NotNull
    @ApiModelProperty("操作记录ids")
    private String[] ids;
    @NotNull
    @ApiModelProperty("0为降级为自选标签,1为升级为公用标签")
    private int toPublic;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public int getToPublic() {
        return toPublic;
    }

    public void setToPublic(int toPublic) {
        this.toPublic = toPublic;
    }
}
