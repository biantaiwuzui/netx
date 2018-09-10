package com.netx.common.vo.business;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created By wj.liu
 * Description: 注册商家请求参数对象
 * Date: 2017-09-05
 */
@ApiModel
public class RegisterSellerRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty(value = "商家id，不传就是新增，编辑必填")
    private String id;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty("商家名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("商家类别id")
    @NotBlank(message = "类别不能为空")
    private String categoryId;

    @ApiModelProperty("商品标签，多个用逗号隔开")
    @NotBlank(message = "商品标签为空")
    private String tagIds;

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

    @ApiModelProperty("地址-单位名称")
    private String addrUnitName;

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
    private String sellerDesc;

    @ApiModelProperty("标识图片，多个逗号隔开")
    @NotBlank(message = "标识图片不能为空")
    private String logoImagesUrl;

    @ApiModelProperty("商家照片，多个逗号隔开数量不限")
    @NotBlank(message = "商家照片不能为空")
    private String sellerImagesUrl;

    @ApiModelProperty("证件，多个逗号隔开")
    //@NotBlank(message = "证件不能为空")
    private String certiImagesUrl;

    @ApiModelProperty("验证法人代表")
    @NotBlank(message = "验证不能为空")
    private String verifyCorporate;

    @ApiModelProperty("验证手机号码")
    private String verifyPhone;

    @ApiModelProperty("验证身份证")
    private String verifyIdCard;

    @ApiModelProperty("验证注册网号")
    private String verifyNetworkNum;

    @ApiModelProperty("业务主管id")
    @NotBlank(message = "业务主管不能为空")
    private String manageId;

    @ApiModelProperty("收银人员Id")
    @NotBlank(message = "收银人员Id")
    private String sellerCashierId;

    @ApiModelProperty("引荐人的客服代码")
    private String referralCode;

    @ApiModelProperty("是否立即支付：" +
            "1：是<br>" +
            "2：否<br>")
    private Integer isPayAtOne;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddrUnitName() {
        return addrUnitName;
    }

    public void setAddrUnitName(String addrUnitName) {
        this.addrUnitName = addrUnitName;
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

    public String getSellerDesc() {
        return sellerDesc;
    }

    public void setSellerDesc(String sellerDesc) {
        this.sellerDesc = sellerDesc;
    }

    public String getLogoImagesUrl() {
        return logoImagesUrl;
    }

    public void setLogoImagesUrl(String logoImagesUrl) {
        this.logoImagesUrl = logoImagesUrl;
    }

    public String getSellerImagesUrl() {
        return sellerImagesUrl;
    }

    public void setSellerImagesUrl(String sellerImagesUrl) {
        this.sellerImagesUrl = sellerImagesUrl;
    }

    public String getCertiImagesUrl() {
        return certiImagesUrl;
    }

    public void setCertiImagesUrl(String certiImagesUrl) {
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

    public String getManageId() {
        return manageId;
    }

    public void setManageId(String manageId) {
        this.manageId = manageId;
    }

    public String getSellerCashierId() {
        return sellerCashierId;
    }

    public void setSellerCashierId(String sellerCashierId) {
        this.sellerCashierId = sellerCashierId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Integer getIsPayAtOne() {
        return isPayAtOne;
    }

    public void setIsPayAtOne(Integer isPayAtOne) {
        this.isPayAtOne = isPayAtOne;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
