package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 用户确认退货请求参数对象
 * Date: 2017-09-09
 */
@ApiModel
public class ConfirmReturnRequestDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "退货记录id不能为空")
    private String id;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "退货状态，3：确认退货，4：撤销退货", required = true)
    @NotNull(message = "退货状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "退货物流名称")
    private String logisticsName;

    @ApiModelProperty(value = "退货物流单号")
    private String logisticsNo;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }
}
