package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 提交订单请求参数
 * Date: 2017-09-15
 */
@ApiModel
public class CommitOrderRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "商品列表", required = true)
    @NotNull(message = "商品不能为空")
    private List<CommitOrderGoodsListDto> goodsList;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty(value = "收货地址", required = true)
    private String address;

    @ApiModelProperty(value = "配送方式\n" +
            "1：第三方配送\n" +
            "2：不提供配送，现场消费\n" +
            "3：外卖配送", required = true)
    private Integer deliveryWay;

    @ApiModelProperty(value = "是否网能里的支付，是填：1  不是：不用填", required = true)
    private Integer isPayOfActivity;

    @ApiModelProperty(value = "1:加入购物车,2:立即购买", required = true)
    @NotNull(message = "操作方式不能为空")
    private Integer way;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<CommitOrderGoodsListDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CommitOrderGoodsListDto> goodsList) {
        this.goodsList = goodsList;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Integer getIsPayOfActivity() {
        return isPayOfActivity;
    }

    public void setIsPayOfActivity(Integer isPayOfActivity) {
        this.isPayOfActivity = isPayOfActivity;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }
}
