package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-3
 */
@ApiModel
public class DisposeCostSettingRequestDto {
    @NotBlank
    private String id;
    @NotNull
    @ApiModelProperty("0不同意,1同意")
    private Integer dispose;
    @NotBlank
    @ApiModelProperty("审核人id")
    private String disposeUser;

    public String getId() {
        return id;
    }

    public String getDisposeUser() {
        return disposeUser;
    }

    public void setDisposeUser(String disposeUser) {
        this.disposeUser = disposeUser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDispose() {
        return dispose;
    }

    public void setDispose(Integer dispose) {
        this.dispose = dispose;
    }
}
