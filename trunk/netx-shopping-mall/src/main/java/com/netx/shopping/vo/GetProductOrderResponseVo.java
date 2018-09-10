package com.netx.shopping.vo;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.model.order.ProductOrderItem;
import com.netx.shopping.model.order.ProductReturn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 获取订单详情
 * Date: 2017-09-14
 */
public class GetProductOrderResponseVo extends ProductOrder {

    /**
     * 商家名字
     */
    private String sellerName;

    /**
     * 商家用户id
     */
    private String sellerUserId;

    /**
     * 订单项列表
     */
    private List<ProductOrderItem> itemList;

    /**
     * 订单列表里的对应商品列表
     */
    private List<GetProductResponseVo> goodsList;

    /**
     * 退货对象
     */
    private ProductReturn productReturn;

    /**
     * 商家地址-市
     */
    private String cityCode;
    /**
     * 商家地址-区
     */
    private String areaCode;
    /**
     * 商家地址-详细
     */
    private String addrDetail;

    /**
     * 顾客 的 地址-经度
     */
    private BigDecimal userLon;
    /**
     * 顾客 的 地址-纬度
     */
    private BigDecimal userLat;

    /**
     * 商家 的 地址-经度
     */
    private BigDecimal sellerLon;

    /**
     * 商家 的 地址-纬度
     */
    private BigDecimal sellerLat;

    private String logisticsName;
    /**
     * 订单退货退款状态
     */
    private Integer orderReturnStatus;
    /**
     * 订单延期状态
     */
    private Integer orderPutOffStatus;
    /**
     * 物流情况
     */
    private List<Map<String,String>> logistcsDetailList;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public List<GetProductResponseVo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GetProductResponseVo> goodsList) {
        this.goodsList = goodsList;
    }

    public List<ProductOrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ProductOrderItem> itemList) {
        this.itemList = itemList;
    }

    public ProductReturn getProductReturn() {
        return productReturn;
    }

    public void setProductReturn(ProductReturn productReturn) {
        this.productReturn = productReturn;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public BigDecimal getUserLon() {
        return userLon;
    }

    public void setUserLon(BigDecimal userLon) {
        this.userLon = userLon;
    }

    public BigDecimal getUserLat() {
        return userLat;
    }

    public void setUserLat(BigDecimal userLat) {
        this.userLat = userLat;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public BigDecimal getSellerLon() {
        return sellerLon;
    }

    public void setSellerLon(BigDecimal sellerLon) {
        this.sellerLon = sellerLon;
    }

    public BigDecimal getSellerLat() {
        return sellerLat;
    }

    public void setSellerLat(BigDecimal sellerLat) {
        this.sellerLat = sellerLat;
    }

    public Integer getOrderReturnStatus() {
        return orderReturnStatus;
    }

    public void setOrderReturnStatus(Integer orderReturnStatus) {
        this.orderReturnStatus = orderReturnStatus;
    }

    public Integer getOrderPutOffStatus() {
        return orderPutOffStatus;
    }

    public void setOrderPutOffStatus(Integer orderPutOffStatus) {
        this.orderPutOffStatus = orderPutOffStatus;
    }

    public List<Map<String, String>> getLogistcsDetailList() {
        return logistcsDetailList;
    }

    public void setLogistcsDetailList(List<Map<String, String>> logistcsDetailList) {
        this.logistcsDetailList = logistcsDetailList;
    }
}