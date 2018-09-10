package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class AddGoodsRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty("商品ID，编辑必填")
    private String id;

    @ApiModelProperty("商家ID")
    @NotBlank(message = "商家ID不能为空")
    private String merchantId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty("商品描述")
    @NotBlank(message = "商品描述不能为空")
    private String characteristic;

    @ApiModelProperty("商品详情")
    @NotBlank(message = "商品详情不能为空")
    private String description;

    @ApiModelProperty("是否配送:true/false")
    @NotNull(message = "是否配送不能为空")
    private Boolean isDelivery;

    @ApiModelProperty("是否支持退换:true/false")
    @NotNull(message = "是否支持退换不能为空")
    private Boolean isReturn;

//    @ApiModelProperty("配送方式\n" +
//            "1：支持第三方配送\n" +
//            "2：不提供配送，仅限现场消费\n" +
//            "3：同时提供外卖配送")
//    @NotNull(message = "配送方式不能为空")
//    private Integer deliveryWay;
    
    @ApiModelProperty("配送方式")
    @NotNull(message = "配送方式不能为空")
    private List<AddDeliveryWayRequestDto> deliveryWayList;

    @ApiModelProperty("商品状态\n" +
            "1：上架\n" +
            "2：下架")
    private Integer onlineStatus;

//    @ApiModelProperty("运费")
    //@NotNull(message = "运费不能为空")
//    private BigDecimal shopping_fee;

    @ApiModelProperty("商品库存相关信息")
    @NotNull(message = "商品库存相关信息不能为空")
    private List<AddSkuRequestDto> addSkuDtoList;

    @ApiModelProperty("商品图片")
    @NotNull(message = "商品图片不能为空")
    @Size(min = 1,message = "商品图片最少有一张")
    private List<PictureResponseVo> logoImageUrl;

    @ApiModelProperty("系统类别id")
    @NotBlank(message = "系统类别不能为空")
    private String categoryId;

    @ApiModelProperty("商品标签，多个用逗号隔开")
    @NotBlank(message = "商品标签不能为空")
    private String tagIds;

    @ApiModelProperty("删除图片id")
    private List<String> deleteImageId;

    public List<AddDeliveryWayRequestDto> getDeliveryWayList() {
        return deliveryWayList;
    }

    public void setDeliveryWayList(List<AddDeliveryWayRequestDto> deliveryWayList) {
        this.deliveryWayList = deliveryWayList;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

//    public BigDecimal getShopping_fee() {
//        return shopping_fee;
//    }
//
//    public void setShopping_fee(BigDecimal shopping_fee) {
//        this.shopping_fee = shopping_fee;
//    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public List<AddSkuRequestDto> getAddSkuDtoList() {
        return addSkuDtoList;
    }

    public void setAddSkuDtoList(List<AddSkuRequestDto> addSkuDtoList) {
        this.addSkuDtoList = addSkuDtoList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public List<PictureResponseVo> getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(List<PictureResponseVo> logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public List<String> getDeleteImageId() {
        return deleteImageId;
    }

    public void setDeleteImageId(List<String> deleteImageId) {
        this.deleteImageId = deleteImageId;
    }


    @Override
    public String toString() {
        return "AddGoodsRequestDto{" +
                "hash='" + hash + '\'' +
                ", id='" + id + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", name='" + name + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", description='" + description + '\'' +
                ", isDelivery=" + isDelivery +
                ", isReturn=" + isReturn +
                ", deliveryWayList=" + deliveryWayList +
                ", onlineStatus=" + onlineStatus +
//                ", shopping_fee=" + shopping_fee +
                ", addSkuDtoList=" + addSkuDtoList +
                ", logoImageUrl=" + logoImageUrl +
                ", categoryId='" + categoryId + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", deleteImageId=" + deleteImageId +
                '}';
    }
}
