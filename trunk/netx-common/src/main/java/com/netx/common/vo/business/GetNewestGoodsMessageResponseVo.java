package com.netx.common.vo.business;

//import com.sy.worth.shoppingmall.model.goods.Goods;
//import com.sy.worth.shoppingmall.model.order.GoodsOrder;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Created By liwei
 * Description:获取最新发布商品发出订单
 * Date: 2017-11-1
 */
public class GetNewestGoodsMessageResponseVo {
    //====================商品详情======================
    @ApiModelProperty("标识ID")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;
    /**
     * 供应商家ID
     */
    @ApiModelProperty("供应商家ID")
    private String sellerId;
    /**
     * 商品第二类目，标签
     */
    private String TagesName;
    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    private String description;
    /**
     * 商品图片列表，多个用逗号隔开
     */
    @ApiModelProperty("商品图片列表，多个用逗号隔开")
    private String goodsImages;
    /**
     * 商品包装明细ID
     */@ApiModelProperty("商品包装明细ID")
    private String packageId;
    /**
     * 计价单位
     */
    @ApiModelProperty(" 计价单位")
    private String priceUnit;
    /**
     * 商品基础价格
     */
    @ApiModelProperty("商品基础价格")
    private BigDecimal price;
    /**
     * 是否配送
     */
    @ApiModelProperty("是否配送")
    private Boolean isDelivery;
    /**
     * 是否退换
     */
    @ApiModelProperty("是否退换")
    private Boolean isReturn;
    /**
     * 商品规格ID
     */
    @ApiModelProperty("商品规格ID")
    private String specId;
    /**
     * 商品详情图片列表，多个用逗号隔开
     */
    @ApiModelProperty("商品详情图片列表，多个用逗号隔开")
    private String detailImages;
    /**
     * 上架原因
     */
    @ApiModelProperty("上架原因")
    private String upReason;
    /**
     * 下架原因
     */
    @ApiModelProperty("下架原因")
    private String downReason;
    /**
     * 商品状态
     1：上架
     2：下架
     */
    @ApiModelProperty("商品状态")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(" 创建时间")
    private Integer delTag;

    /**
     * 自选分类名称
     */
    @ApiModelProperty(" 自选分类名称")
    private String kindName;
    /**
     * 系统规格分类名称
     */
    @ApiModelProperty(" 系统规格分类名称")
    private String categoryName;
    /**
     * 商家名称
     */
    @ApiModelProperty(" 商家名称")
    private String sellerName;
    /**
     * 商品成交额
     */
    @ApiModelProperty(" 商品成交额")
    private BigDecimal goodsDealAmount;
    /**
     * 商家信用
     */
    @ApiModelProperty(" 商家信用值")
    private int credit;
    /**
     * 今日下单
     */
    @ApiModelProperty(" 今日下单")
    private int todayOderQuantity;

    /**
     * 浏览量
     */
    @ApiModelProperty(" 浏览量")
    private Integer visitNum;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoodsImages() {
        return goodsImages;
    }

    public void setGoodsImages(String goodsImages) {
        this.goodsImages = goodsImages;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public String getUpReason() {
        return upReason;
    }

    public void setUpReason(String upReason) {
        this.upReason = upReason;
    }

    public String getDownReason() {
        return downReason;
    }

    public void setDownReason(String downReason) {
        this.downReason = downReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelTag() {
        return delTag;
    }

    public void setDelTag(Integer delTag) {
        this.delTag = delTag;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getGoodsDealAmount() {
        return goodsDealAmount;
    }

    public void setGoodsDealAmount(BigDecimal goodsDealAmount) {
        this.goodsDealAmount = goodsDealAmount;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getTodayOderQuantity() {
        return todayOderQuantity;
    }

    public void setTodayOderQuantity(int todayOderQuantity) {
        this.todayOderQuantity = todayOderQuantity;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public String getTagesName() {
        return TagesName;
    }

    public void setTagesName(String tagesName) {
        TagesName = tagesName;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }


}
