package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 商家同意退货请求参数对象
 * Date: 2017-09-09
 */
@ApiModel
public class AgreeReturnRequestDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "退货记录id不能为空")
    private String id;

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空")
    private String sellerUserId;

    @ApiModelProperty(value = "退货地址,第三方配送时不能为空")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
