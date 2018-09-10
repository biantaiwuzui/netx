package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class RegisterMerchantRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty(value = "商家id")
    private String id;

    @ApiModelProperty(value = "父商家di")
    private String parentMerchantId;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty("商家名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "商家职务：\n" +
            "0.客服代理\n" +
            "1.商户代理\n" +
            "2.市场总监\n" +
            "3.营运总裁商家职务")
    private Integer position;

    @ApiModelProperty("商家类别id")
    @NotBlank(message = "类别不能为空")
    private String categoryId;

    @ApiModelProperty("商品标签，多个用逗号隔开")
    @NotBlank(message = "商品标签不能为空")
    private String tagIds;

    @ApiModelProperty("主管id")
    private String managerId;

    @ApiModelProperty("收银员id")
    private String cashierId;

    @ApiModelProperty("客服id")
    private String customerServiceId;

    @ApiModelProperty("地址-省")
    @NotBlank(message = "地址不能为空")
    private String provinceCode;

    @ApiModelProperty("地址-市")
    private String cityCode;

    @ApiModelProperty("地址-区")
    private String areaCode;

    @ApiModelProperty("地址-乡镇")
    private String addrCountry;

    @ApiModelProperty("地址-详细")
    private String addrDetail;

    @ApiModelProperty("地址-详细-门牌")
    private String addrDoorNumber;

    @ApiModelProperty("地址-联系电话")
    private String addrTel;

    @ApiModelProperty("地址-联系人")
    private String addrContact;

    @ApiModelProperty("地址-经度")
    @NotNull(message = "地址-经度不能为空")
    private BigDecimal lon;

    @ApiModelProperty("地址-纬度")
    @NotNull(message = "地址-纬度不能为空")
    private BigDecimal lat;

    @ApiModelProperty("商家红包设置id")
    @NotBlank(message = "红包设置不能为空")
    private String pacSetId;

    @ApiModelProperty("商家描述")
    private String desc;

    @ApiModelProperty("标识图片")
    @NotNull(message = "标识图片不能为空")
    @Size(min = 1, message = "标识图片不能为空")
    private List<PictureResponseVo> logoImagesUrl;

    @ApiModelProperty("商家照片")
    @NotNull(message = "商家照片不能为空")
    @Size(min = 1, message = "商家照片不能为空")
    private List<PictureResponseVo> merchantImagesUrl;

    @ApiModelProperty("证件")
    private List<PictureResponseVo> certiImagesUrl;

    @ApiModelProperty("验证法人代表")
    @NotBlank(message = "验证不能为空")
    private String verifyCorporate;

    @ApiModelProperty("验证手机号码")
    private String verifyPhone;

    @ApiModelProperty("验证身份证")
    private String verifyIdCard;

    @ApiModelProperty("验证注册网号")
    private String verifyNetworkNum;

    @ApiModelProperty("是否立即支付：" +
            "1：是<br>" +
            "2：否<br>")
    private Integer isPayAtOne;

    @ApiModelProperty("删除图片id")
    private List<String> deleteImagesUrlId;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParentMerchantId() {
        return parentMerchantId;
    }

    public void setParentMerchantId(String parentMerchantId) {
        this.parentMerchantId = parentMerchantId;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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

    public String getAddrCountry() {
        return addrCountry;
    }

    public void setAddrCountry(String addrCountry) {
        this.addrCountry = addrCountry;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getAddrDoorNumber() {
        return addrDoorNumber;
    }

    public void setAddrDoorNumber(String addrDoorNumber) {
        this.addrDoorNumber = addrDoorNumber;
    }

    public String getAddrTel() {
        return addrTel;
    }

    public void setAddrTel(String addrTel) {
        this.addrTel = addrTel;
    }

    public String getAddrContact() {
        return addrContact;
    }

    public void setAddrContact(String addrContact) {
        this.addrContact = addrContact;
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

    public String getPacSetId() {
        return pacSetId;
    }

    public void setPacSetId(String pacSetId) {
        this.pacSetId = pacSetId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<PictureResponseVo> getLogoImagesUrl() {
        return logoImagesUrl;
    }

    public void setLogoImagesUrl(List<PictureResponseVo> logoImagesUrl) {
        this.logoImagesUrl = logoImagesUrl;
    }

    public List<PictureResponseVo> getMerchantImagesUrl() {
        return merchantImagesUrl;
    }

    public void setMerchantImagesUrl(List<PictureResponseVo> merchantImagesUrl) {
        this.merchantImagesUrl = merchantImagesUrl;
    }

    public List<PictureResponseVo> getCertiImagesUrl() {
        return certiImagesUrl;
    }

    public void setCertiImagesUrl(List<PictureResponseVo> certiImagesUrl) {
        this.certiImagesUrl = certiImagesUrl;
    }

    public String getVerifyCorporate() {
        return verifyCorporate;
    }

    public void setVerifyCorporate(String verifyCorporate) {
        this.verifyCorporate = verifyCorporate;
    }

    public String getVerifyPhone() {
        return verifyPhone;
    }

    public void setVerifyPhone(String verifyPhone) {
        this.verifyPhone = verifyPhone;
    }

    public String getVerifyIdCard() {
        return verifyIdCard;
    }

    public void setVerifyIdCard(String verifyIdCard) {
        this.verifyIdCard = verifyIdCard;
    }

    public String getVerifyNetworkNum() {
        return verifyNetworkNum;
    }

    public void setVerifyNetworkNum(String verifyNetworkNum) {
        this.verifyNetworkNum = verifyNetworkNum;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getCustomerServiceId() {
        return customerServiceId;
    }

    public void setCustomerServiceId(String customerServiceId) {
        this.customerServiceId = customerServiceId;
    }

    public Integer getIsPayAtOne() {
        return isPayAtOne;
    }

    public void setIsPayAtOne(Integer isPayAtOne) {
        this.isPayAtOne = isPayAtOne;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDeleteImagesUrlId() {
        return deleteImagesUrlId;
    }

    public void setDeleteImagesUrlId(List<String> deleteImagesUrlId) {
        this.deleteImagesUrlId = deleteImagesUrlId;
    }

}
