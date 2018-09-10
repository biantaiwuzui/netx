package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class GetSellerByUserIdVo {
    /**
     * 商家id
     */
    @ApiModelProperty("商家id")
    private String id;

    /**
     * 商家名称
     */
    @ApiModelProperty("商家名称")
    private String name;

    /**
     * 商家商品数量
     */
    private Integer goodsNum;

    /**
     * 商家订单数量
     */
    private Integer orderNum;

    /**
     * 商家订单总成交额
     */
    private BigDecimal sumOrderAmount;

    /**
     * 系统类别名称
     */
    @ApiModelProperty("系统类别名称")
    private String categoryName;

    /**
     * 商品标签，多个用逗号隔开
     */
    @ApiModelProperty("商品标签，多个用逗号隔开")
    private String tagIds;

    /**
     * 地址-详细
     */
    @ApiModelProperty("地址-详细")
    private String addrDetail;

    /**
     * 地址-经纬度hash
     */
    @ApiModelProperty("地址-经纬度hash")
    private String geohash;
    /**
     * 地址-经度
     */
    @ApiModelProperty("地址-经度")
    private BigDecimal lon;
    /**
     * 地址-纬度
     */
    @ApiModelProperty("地址-纬度")
    private BigDecimal lat;
    /**
     * 红包池
     */
    @ApiModelProperty("红包池")
    private BigDecimal packetPoolAmount;

    /**
     * 标识图片，多个逗号隔开
     */
    @ApiModelProperty("标识图片，多个逗号隔开")
    private String logoImages;
    /**
     * 商家照片，多个逗号隔开数量不限
     */
    @ApiModelProperty("商家照片，多个逗号隔开数量不限")
    private String sellerImages;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 访问量
     */
    @ApiModelProperty("访问量")
    private Integer visitNum;

    /**
     * 地址-市
     */
    @ApiModelProperty("地址-市")
    private String cityCode;
    /**
     * 地址-区
     */
    @ApiModelProperty("地址-区")
    private String areaCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }



    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getPacketPoolAmount() {
        return packetPoolAmount;
    }

    public void setPacketPoolAmount(BigDecimal packetPoolAmount) {
        this.packetPoolAmount = packetPoolAmount;
    }

    public String getLogoImages() {
        return logoImages;
    }

    public void setLogoImages(String logoImages) {
        this.logoImages = logoImages;
    }

    public String getSellerImages() {
        return sellerImages;
    }

    public void setSellerImages(String sellerImages) {
        this.sellerImages = sellerImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
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
}
