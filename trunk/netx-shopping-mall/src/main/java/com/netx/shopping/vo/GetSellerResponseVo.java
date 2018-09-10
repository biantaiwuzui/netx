package com.netx.shopping.vo;

import com.netx.shopping.model.business.Category;
import com.netx.shopping.model.business.Seller;

import java.math.BigDecimal;
import java.util.List;
/**
 * Created By wj.liu
 * Description: 获取商家详情返回对象
 * Date: 2017-09-09
 */
public class GetSellerResponseVo extends Seller {

    /**
     *收银人员用户id
     */
    private String cashierUserId;
    /**
     *业务主管用户id
     */
    private String manageUserId;

    /**
     *用户是否收藏商家状态，有为1，没有则为2
     */
    private Integer isHaveCollect;

    /**
     * 商家商品数量
     */
    private Integer goodsNum;

    /**
     * 商家订单数量
     */
    private Integer orderNum;

    private List<Category> categoryId;

    private List<Category> tagIds;

    /**
     * 商家订单总成交额
     */
    private BigDecimal sumOrderAmount;

//    /**
//     * 系统类别名称
//     */
//    private String categoryName;

//    /**
//     *订单金额百分百1
//     */
//    private BigDecimal orderPercent1;
//
//    /**
//     *订单金额百分百2
//     */
//    private BigDecimal orderPercent2;

    /**
     *红包发放时间
     */
    private Long sendTime;

    private  String moneyNikName;

    /**
     * 红包池
     */
    private Long packetPoolAmount;

    /**
     * 商家距离
     */
    private Double distance;

    public String getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(String cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public String getManageUserId() {
        return manageUserId;
    }

    public void setManageUserId(String manageUserId) {
        this.manageUserId = manageUserId;
    }

    public Integer getIsHaveCollect() {
        return isHaveCollect;
    }

    public void setIsHaveCollect(Integer isHaveCollect) {
        this.isHaveCollect = isHaveCollect;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getSumOrderAmount() {
        return sumOrderAmount;
    }

    public void setSumOrderAmount(BigDecimal sumOrderAmount) {
        this.sumOrderAmount = sumOrderAmount;
    }

//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    public BigDecimal getOrderPercent1() {
//        return orderPercent1;
//    }
//
//    public void setOrderPercent1(BigDecimal orderPercent1) {
//        this.orderPercent1 = orderPercent1;
//    }
//
//    public BigDecimal getOrderPercent2() {
//        return orderPercent2;
//    }
//
//    public void setOrderPercent2(BigDecimal orderPercent2) {
//        this.orderPercent2 = orderPercent2;
//    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMoneyNikName() {
        return moneyNikName;
    }

    public void setMoneyNikName(String moneyNikName) {
        this.moneyNikName = moneyNikName;
    }

    public List<Category> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Category> categoryId) {
        this.categoryId = categoryId;
    }

    public List<Category> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Category> tagIds) {
        this.tagIds = tagIds;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public Long getPacketPoolAmount() {
        return packetPoolAmount;
    }

    @Override
    public void setPacketPoolAmount(Long packetPoolAmount) {
        this.packetPoolAmount = packetPoolAmount;
    }
}
