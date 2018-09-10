package com.netx.shopping.vo;

import com.netx.shopping.model.productcenter.Category;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;

public class GetProductDetailResponseVo {

    /**
     * 标识ID
     */
    private String id;
    /**
     * 供应商家
     */
    private String merchantId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品描述
     */
    private String characteristic;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 是否配送
     */
    private Boolean isDelivery;
    /**
     * 是否支持退换
     */
    private Boolean isReturn;
    /**
     * 配送方式
     */
//    private Integer deliveryWay;
    private List<AddDeliveryWayRequestDto> deliveryWayList;
    /**
     * 商品状态
     1：上架
     2：下架
     */
    private Integer onlineStatus;
    /**
     * 访问量
     */
    private Integer visitCount;
    /**
     * 运费设置id
     */
    private String shippingFeeId;
    /**
     * 商品运费
     */
    private BigDecimal shoppingFee;

    /**
     * 商品第一类目
     */
    private List<Category> categories;
    /**
     * 商品第二类目
     */
    private List<Category> Tages;

    /**
     *用户是否收藏商品状态，有为1，没有则为2
     */
    private Integer isHaveCollect;

    /**
     *供应商家图片
     */
    private String marchantImagesUrl;

    /**
     * 可编辑
     */
    private Boolean edit = false;

    /**
     * 商家信息
     */
    private GetMerchantListVo merchantList;

    /**
     * 属性列表
     */
    private List<GetPropertyResponseVo> propertyList;

    /**
     * 商品图片
     */
    private List<PictureResponseVo> logoImageUrl;

    /**
     * 商品详情图片
     */
    private List<PictureResponseVo> detailImageUrl;

    /**
     * 最低价
     */
    private BigDecimal minPrice;

    /**
     * 最高价
     */
    private BigDecimal maxPrice;

    /**
     * 客服userId
     */
    private String customerServiceUserId;
    
    @ApiModelProperty(value = "商品单价")
    private String unit;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<AddDeliveryWayRequestDto> getDeliveryWayList() {
        return deliveryWayList;
    }
    

    public void setDeliveryWayList(List<AddDeliveryWayRequestDto> deliveryWayList) {
        this.deliveryWayList = deliveryWayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public Boolean getReturn() {
        return isReturn;
    }

    public void setReturn(Boolean aReturn) {
        isReturn = aReturn;
    }

//    public Integer getDeliveryWay() {
//        return deliveryWay;
//    }
//
//    public void setDeliveryWay(Integer deliveryWay) {
//        this.deliveryWay = deliveryWay;
//    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getShippingFeeId() {
        return shippingFeeId;
    }

    public void setShippingFeeId(String shippingFeeId) {
        this.shippingFeeId = shippingFeeId;
    }

    public BigDecimal getShoppingFee() {
        return shoppingFee;
    }

    public void setShoppingFee(BigDecimal shoppingFee) {
        this.shoppingFee = shoppingFee;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getTages() {
        return Tages;
    }

    public void setTages(List<Category> tages) {
        Tages = tages;
    }

    public Integer getIsHaveCollect() {
        return isHaveCollect;
    }

    public void setIsHaveCollect(Integer isHaveCollect) {
        this.isHaveCollect = isHaveCollect;
    }

    public String getMarchantImagesUrl() {
        return marchantImagesUrl;
    }

    public void setMarchantImagesUrl(String marchantImagesUrl) {
        this.marchantImagesUrl = marchantImagesUrl;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public GetMerchantListVo getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(GetMerchantListVo merchantList) {
        this.merchantList = merchantList;
    }

    public List<GetPropertyResponseVo> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<GetPropertyResponseVo> propertyList) {
        this.propertyList = propertyList;
    }

    public List<PictureResponseVo> getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(List<PictureResponseVo> logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public List<PictureResponseVo> getDetailImageUrl() {
        return detailImageUrl;
    }

    public void setDetailImageUrl(List<PictureResponseVo> detailImageUrl) {
        this.detailImageUrl = detailImageUrl;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCustomerServiceUserId() {
        return customerServiceUserId;
    }

    public void setCustomerServiceUserId(String customerServiceUserId) {
        this.customerServiceUserId = customerServiceUserId;
    }
}
