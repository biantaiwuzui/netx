package com.netx.shopping.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 订单发货请求参数
 * Date: 2017-09-15
 */
@ApiModel
public class MerchantOrderSendRequestDto {

    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    @ApiModelProperty("物流公司Code")
    @NotBlank(message = "物流公司代号不能为空")
    private String logisticsCode;

    @ApiModelProperty("物流单号")
    @NotBlank(message = "物流单号不能为空")
    private String logisticsNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }
}
