package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 发布商品请求参数对象
 * Date: 2017-09-13
 */
@ApiModel
public class ReleaseGoodsRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;


    @ApiModelProperty("商品id，编辑必填")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 10,message = "商品名称长度不能超过10")
    private String name;

    @ApiModelProperty("商品描述")
    @NotBlank(message = "商品描述不能为空")
    @Length(max = 40,message = "商品描述长度不能超过40")
    private String characteristic;

    @ApiModelProperty("系统类别id")
    @NotBlank(message = "系统类别不能为空")
    private String categoryId;

    @ApiModelProperty("供应商家id")
    private String sellerId;

    @ApiModelProperty("商品标签，多个用逗号隔开")
    @NotBlank(message = "商品标签不能为空")
    private String tagIds;

    @ApiModelProperty("商品详情")
    @NotBlank(message = "商品详情不能为空")
    private String description;

    @ApiModelProperty("商品图片列表，多个用逗号隔开")
    private String productImagesUrl;

//    @ApiModelProperty("商品包装明细id，多个用逗号隔开")
//    private String packageId;
//
//    @ApiModelProperty("计价单位")
//    private String priceUnit;

//    @ApiModelProperty("商品价格")
//    private BigDecimal price;

    @ApiModelProperty("是否配送")
    private Boolean isDelivery;

    @ApiModelProperty("配送方式\n" +
            "1：支持第三方配送\n" +
            "2：不提供配送，仅限现场消费\n" +
            "3：同时提供外卖配送或现场消费")
    @NotNull(message = "配送方式不能为空")
    private Integer deliveryWay;

    @ApiModelProperty("是否退换")
    private Boolean isReturn;

    @ApiModelProperty("商品规格列表")
    private List<AddProductSpeRequertDto> speRequertDtoList;

    @ApiModelProperty("商品详情图片列表，多个用逗号隔开")
    private String detailImagesUrl;

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImagesUrl() {
        return productImagesUrl;
    }

    public void setProductImagesUrl(String productImagesUrl) {
        this.productImagesUrl = productImagesUrl;
    }

//    public String getPackageId() {
//        return packageId;
//    }
//
//    public void setPackageId(String packageId) {
//        this.packageId = packageId;
//    }
//
//    public String getPriceUnit() {
//        return priceUnit;
//    }
//
//    public void setPriceUnit(String priceUnit) {
//        this.priceUnit = priceUnit;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }

    public Boolean getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(Boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public Boolean getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Boolean isReturn) {
        this.isReturn = isReturn;
    }

    public List<AddProductSpeRequertDto> getSpeRequertDtoList() {
        return speRequertDtoList;
    }

    public void setSpeRequertDtoList(List<AddProductSpeRequertDto> speRequertDtoList) {
        this.speRequertDtoList = speRequertDtoList;
    }

    public String getDetailImagesUrl() {
        return detailImagesUrl;
    }

    public void setDetailImagesUrl(String detailImagesUrl) {
        this.detailImagesUrl = detailImagesUrl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
