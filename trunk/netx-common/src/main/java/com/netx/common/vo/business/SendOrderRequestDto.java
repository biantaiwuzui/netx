package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 订单发货请求参数
 * Date: 2017-09-15
 */
@ApiModel
public class SendOrderRequestDto {

    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    @ApiModelProperty("物流公司Code")
    private String logisticsCode;

    @ApiModelProperty("物流单号")
    private String logisticsNum;

    @ApiModelProperty("配送员姓名")
    private String deliveryman;

    @ApiModelProperty("配送员联系号码")
    private String deliveryNum;

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

    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    public String getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(String deliveryman) {
        this.deliveryman = deliveryman;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }
}
