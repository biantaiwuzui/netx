package com.netx.common.vo.business;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class ChangeGoodsOrderStatusRequestDto {
    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    /**
     * 订单状态
     1：待付款
     2：待发货
     3：物流中
     4：退货中
     5：投诉中
     6：待评论
     7：已完成
     8：已取消
     9: 待生成
     10: 已付款
     11: 已服务
     */
    @ApiModelProperty("订单状态:1：待付款,2：待发货,3：物流中,4：退货中,5：投诉中,6：待评论,7：已完成,8：已取消,9: 待生成,10: 已付款,11: 已服务")
    @NotNull
    private Integer status;

    @ApiModelProperty(value = "活动id，当是活动里生成的订单需传活动id", required = true)
    private String activityId;

    @ApiModelProperty(value = "心愿id，当是心愿里生成的订单需传活动id", required = true)
    private String demaneId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getDemaneId() {
        return demaneId;
    }

    public void setDemaneId(String demaneId) {
        this.demaneId = demaneId;
    }
}
