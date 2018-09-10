package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;

public class CreditSearchResponse {

    /**
     * 标识ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 网币名称
     */
    private String name;
    /**
     * 网币标签，多个用逗号隔开
     */
    private String tagIds;
    /**
     * 网币正面样式
     */
    private String frontStyle;
    /**
     * 网币反面样式
     */
    private String backStyle;
    /**
     * 是否适用范围1

     */
    private Boolean isFitScopeOne;
    /**
     * 是否适用范围2，选择商家

     */
    private Boolean isFitScopeTwo;
    /**
     * 选择范围商家，选择适用范围2的时候必填
     */
    private String sellerIds;
    /**
     * 是否适用范围3
     */
    private Boolean isFitScopeThree;
    /**
     * 结转用户网号
     */
    private String importNetNum;
    /**
     * 结转用户姓名
     */
    private String importName;
    /**
     * 结转用户手机号
     */
    private String importPhone;
    /**
     * 结转用户身份证
     */
    private String importIdcard;
    /**
     * 发行数量
     */
    private Integer releaseNum;
    /**
     * 发行时间
     */
    private Date releaseTime;
    /**
     * 发行成功时间
     */
    private Date successTime;
    /**
     * 网币面值
     */
    private Integer faceValue;
    /**
     * 申购单价,以 分 为单位
     */
    private Long applyPrice;
    /**
     * 回购系数
     */
    private BigDecimal buyFactor;
    /**
     * 递增幅度
     */
    private Integer growthRate;
    /**
     * 固定分红提成比例
     */
    private Integer royaltyRatio;
    /**
     * 网币说明
     */
    private String remark;
    /**
     * 已申购金额
     */
    private Long buyAmount;
    /**
     * 已兑付金额
     */
    private Long payAmount;
    /**
     * 网币状态
     1：等待审核
     2：不予批准
     3：等待保荐
     4：不予保荐
     5：正在申购
     6：发行成功
     7：兑付完成8:正在兑付
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    private double distance;

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

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getFrontStyle() {
        return frontStyle;
    }

    public void setFrontStyle(String frontStyle) {
        this.frontStyle = frontStyle;
    }

    public String getBackStyle() {
        return backStyle;
    }

    public void setBackStyle(String backStyle) {
        this.backStyle = backStyle;
    }

    public Boolean getFitScopeOne() {
        return isFitScopeOne;
    }

    public void setFitScopeOne(Boolean fitScopeOne) {
        isFitScopeOne = fitScopeOne;
    }

    public Boolean getFitScopeTwo() {
        return isFitScopeTwo;
    }

    public void setFitScopeTwo(Boolean fitScopeTwo) {
        isFitScopeTwo = fitScopeTwo;
    }

    public String getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
    }

    public Boolean getFitScopeThree() {
        return isFitScopeThree;
    }

    public void setFitScopeThree(Boolean fitScopeThree) {
        isFitScopeThree = fitScopeThree;
    }

    public String getImportNetNum() {
        return importNetNum;
    }

    public void setImportNetNum(String importNetNum) {
        this.importNetNum = importNetNum;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public String getImportPhone() {
        return importPhone;
    }

    public void setImportPhone(String importPhone) {
        this.importPhone = importPhone;
    }

    public String getImportIdcard() {
        return importIdcard;
    }

    public void setImportIdcard(String importIdcard) {
        this.importIdcard = importIdcard;
    }

    public Integer getReleaseNum() {
        return releaseNum;
    }

    public void setReleaseNum(Integer releaseNum) {
        this.releaseNum = releaseNum;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Long getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Long applyPrice) {
        this.applyPrice = applyPrice;
    }

    public BigDecimal getBuyFactor() {
        return buyFactor;
    }

    public void setBuyFactor(BigDecimal buyFactor) {
        this.buyFactor = buyFactor;
    }

    public Integer getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Integer growthRate) {
        this.growthRate = growthRate;
    }

    public Integer getRoyaltyRatio() {
        return royaltyRatio;
    }

    public void setRoyaltyRatio(Integer royaltyRatio) {
        this.royaltyRatio = royaltyRatio;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(Long buyAmount) {
        this.buyAmount = buyAmount;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
