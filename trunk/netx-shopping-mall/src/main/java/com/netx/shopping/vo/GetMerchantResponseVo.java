package com.netx.shopping.vo;

import com.netx.shopping.model.productcenter.Category;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GetMerchantResponseVo {

    /**
     * 创建者id
     */
    private String userId;
    /**
     * 创建者昵称
     */
    private String nickname;
    /**
     * 信用
     */
    private Integer credit;
    /**
     * 商家id
     */
    private String id;
    /**
     * 父商家ID
     */
    private String parentMerchantId;
    /**
     * 引荐人服务代码
     */
    private String customerServiceCode;
    /**
     * 商家名
     */
    private String name;
    /**
     * 商家职务：
     0：客服代理
     1：商户代理
     2：市场总监
     3：营运总裁商家职务
     */
    private Integer position;
    /**
     * 引荐客服代码
     */
    private String referralServiceCode;
    /**
     * 省份
     */
    private String provinceCode;
    /**
     * 地级市
     */
    private String cityCode;
    /**
     * 县/区
     */
    private String areaCode;
    /**
     * 镇/街道
     */
    private String addrCountry;
    /**
     * 详细地址
     */
    private String addrDetail;
    /**
     * 门牌号
     */
    private String addrDoorNumber;
    /**
     * 联系方式
     */
    private String addrTel;

    /**
     * 地址-联系人
     */
    private String addrContact;
    /**
     * 商家详情
     */
    private String desc;
    /**
     * 商家状态1.正常2.拉黑
     */
    private Integer status;

    /**
     * 禁用原因
     */
    private String disableReason;

    /**
     * 启用原因
     */
    private String enableReason;

    /**
     * 访问量
     */
    private Integer visitCount;
    /**
     * 收款二维码
     */
    private String qrcode;

    /**
     * 注册管理费缴费状态
     0：已缴费
     1：待缴费
     2：待续费
     */
    private Integer payStatus;
    /**
     * 缴费时间
     */
    private Date feeTime;
    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 是否支持网信
     */
    private Boolean isSupportCredit;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *用户是否收藏商家状态，有为1，没有则为2
     */
    private Integer isHaveCollect;

    /**
     * 商家商品数量
     */
    private Integer goodsNum = 0;

    /**
     * 商家订单数量
     */
    private Integer orderNum = 0;
    /**
     * 一级类目
     */
    private List<Category> categoryId;
    /**
     * 二级类目
     */
    private List<Category> tagIds;

    /**
     * 商家订单总成交额
     */
    private BigDecimal sumOrderAmount = new BigDecimal(0);

    /**
     *红包发放时间
     */
    private Long sendTime;

    private String moneyNikName;

    /**
     * 红包池
     */
    private Long packetPoolAmount;

    /**
     * 商家距离
     */
    private Double distance;

    private BigDecimal lat;

    private BigDecimal lon;

    /**
     * 标识图片，多个逗号隔开
     */
    private List<PictureResponseVo> logoImagesUrl;
    /**
     * 商家照片，多个逗号隔开数量不限
     */
    private List<PictureResponseVo> merchantImagesUrl;
    /**
     * 证件，多个逗号隔开
     */
    private List<PictureResponseVo> certiImagesUrl;
    /**
     * 可编辑
     */
    private Boolean edit = false;
    /**
     * 首单红包
     */
    private BigDecimal firstRate;
    private BigDecimal limitRate;
    /**
     * 红包设置id
     */
    private String pacSetId;

    private List<ManagerListResponseVo> managerList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentMerchantId() {
        return parentMerchantId;
    }

    public void setParentMerchantId(String parentMerchantId) {
        this.parentMerchantId = parentMerchantId;
    }

    public String getCustomerServiceCode() {
        return customerServiceCode;
    }

    public void setCustomerServiceCode(String customerServiceCode) {
        this.customerServiceCode = customerServiceCode;
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

    public String getReferralServiceCode() {
        return referralServiceCode;
    }

    public void setReferralServiceCode(String referralServiceCode) {
        this.referralServiceCode = referralServiceCode;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public String getEnableReason() {
        return enableReason;
    }

    public void setEnableReason(String enableReason) {
        this.enableReason = enableReason;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getFeeTime() {
        return feeTime;
    }

    public void setFeeTime(Date feeTime) {
        this.feeTime = feeTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getSupportCredit() {
        return isSupportCredit;
    }

    public void setSupportCredit(Boolean supportCredit) {
        isSupportCredit = supportCredit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public BigDecimal getSumOrderAmount() {
        return sumOrderAmount;
    }

    public void setSumOrderAmount(BigDecimal sumOrderAmount) {
        this.sumOrderAmount = sumOrderAmount;
    }

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

    public Long getPacketPoolAmount() {
        return packetPoolAmount;
    }

    public void setPacketPoolAmount(Long packetPoolAmount) {
        this.packetPoolAmount = packetPoolAmount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public String getPacSetId() {
        return pacSetId;
    }

    public void setPacSetId(String pacSetId) {
        this.pacSetId = pacSetId;
    }

    public List<ManagerListResponseVo> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<ManagerListResponseVo> managerList) {
        this.managerList = managerList;
    }

    public BigDecimal getLimitRate() {
        return limitRate;
    }

    public void setLimitRate(BigDecimal limitRate) {
        this.limitRate = limitRate;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }
}
