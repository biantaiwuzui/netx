package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created By wj.liu
 * Description: 商品订单详情请求参数
 * Date: 2017-09-16
 */
@ApiModel
public class GetGoodsOrderRequestDto {

    @ApiModelProperty("订单id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("订单号")
    private String orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
