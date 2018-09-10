package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created By wj.liu
 * Description: 获取订单列表请求参数
 * Date: 2017-09-15
 */
@ApiModel
public class GetGoodsOrderListRequestDto extends PageRequestDto{

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("商家id")
    private String selleId;

    @ApiModelProperty(value = "订单状态, 1、待付款\n" +
            "    2、待发货\n" +
            "    3、物流中\n" +
            "    4、退货中\n" +
            "    5、投诉中\n" +
            "    6、待评论\n" +
            "    7、已完成\n" +
            "    8、已取消\n" +
            "    9、待生成\n" +
            "    10、已付款\n" +
            "    11、已服务\n" +
            "    12、延迟中", required = true)
    private Integer status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelleId() {
        return selleId;
    }

    public void setSelleId(String selleId) {
        this.selleId = selleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
