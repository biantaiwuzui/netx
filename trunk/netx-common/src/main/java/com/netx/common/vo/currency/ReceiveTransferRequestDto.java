package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 网信转让请求参数对象
 * Date: 2017-10-28
 */
@ApiModel
public class ReceiveTransferRequestDto {

    @ApiModelProperty("转让id")
    @NotBlank(message = "转让id不能为空 ")
    private String id;
    /**
     * 受让者ID
     */
    @ApiModelProperty("受让者id")
    @NotBlank(message = "受让者id不能为空 ")
    private String beLaunchUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeLaunchUserId() {
        return beLaunchUserId;
    }

    public void setBeLaunchUserId(String beLaunchUserId) {
        this.beLaunchUserId = beLaunchUserId;
    }
}
