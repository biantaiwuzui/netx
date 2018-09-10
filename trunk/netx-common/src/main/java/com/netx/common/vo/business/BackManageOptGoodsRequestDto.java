package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created By wj.liu
 * Description: 商家后台管理操作商品请求参数
 * Date: 2017-09-21
 */
@ApiModel
public class BackManageOptGoodsRequestDto{

    @ApiModelProperty("商品id")
    @NotBlank(message = "商品id不能为空")
    private String id;

    @ApiModelProperty("操作类型, 1: 上架， 2：下架")
    @NotNull(message = "操作类型不能为空")
    private Integer status;

    @ApiModelProperty("操作理由")
    private String reason;

    @ApiModelProperty("商品上下架操作者id")
    @NotBlank(message = "商品上下架操作者id不能为空")
    private String handlersId;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHandlersId() {
        return handlersId;
    }

    public void setHandlersId(String handlersId) {
        this.handlersId = handlersId;
    }
}
