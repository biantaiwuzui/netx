package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-27
 */
@ApiModel
public class DisposeAddFriendMessageRequestDto {
    @NotBlank
    private String id;
    @ApiModelProperty(required = true, value = "是否同意,0不同意,1同意")
    @NotNull
    private Boolean dispose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDispose() {
        return dispose;
    }

    public void setDispose(Boolean dispose) {
        this.dispose = dispose;
    }

    @Override
    public String toString() {
        return "DisposeAddFriendMessageRequest{" +
                "id='" + id + '\'' +
                ", dispose=" + dispose +
                '}';
    }
}
