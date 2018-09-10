package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class TokenCurrencyVo{
    /**
     * 标识ID
     */
    @ApiModelProperty("标识ID")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 网信名称
     */
    @ApiModelProperty("网信名称")
    private String name;
    /**
     * 网信标签，多个用逗号隔开
     */
    @ApiModelProperty("网信标签,多个用逗号隔开")
    private String tagIds;
    /**
     * 网信正面样式
     */
    @ApiModelProperty("网信正面样式")
    private String frontStyle;
    /**
     * 网信反面样式
     */
    @ApiModelProperty("网信反面样式")
    private String backStyle;
    /**
     * 选择范围商家，选择适用范围2的时候必填
     */
    @ApiModelProperty("选择范围商家，选择适用范围2的时候必填")
    private String sellerIds;
    /**
     * 结转用户网号
     */
    @ApiModelProperty("结转用户网号")
    private String importNetNum;
    /**
     * 结转用户姓名
     */
    @ApiModelProperty("结转用户姓名")
    private String importName;
    /**
     * 结转用户手机号
     */
    @ApiModelProperty("结转用户手机号")
    private String importPhone;
    /**
     * 结转用户身份证
     */
    @ApiModelProperty("结转用户身份证")
    private String importIdCard;
    /**
     * 发行数量
     */
    @ApiModelProperty("发行数量")
    private Integer releaseNum;
    /**
     * 发行时间
     */
    @ApiModelProperty("发行时间")
    private Date releaseTime;
    /**
     * 网信面值
     */
    @ApiModelProperty("网信面值")
    private Integer faceValue;
    /**
     * 申购单价
     */
    @ApiModelProperty("申购单价")
    private Long applyPrice;
    /**
     * 回购系数
     */
    @ApiModelProperty("回购系数")
    private BigDecimal buyFactor;
    /**
     * 递增幅度
     */
    @ApiModelProperty("递增幅度")
    private Integer growthRate;
    /**
     * 固定分红提成比例
     */
    @ApiModelProperty("固定分红提成比例")
    private Integer royaltyRatio;
    /**
     * 网信说明
     */
    @ApiModelProperty("网信说明")
    private String remark;
    /**
     * 已申购金额
     */
    @ApiModelProperty("已申购金额")
    private Long buyAmount;
    /**
     * 已兑付金额
     */
    @ApiModelProperty("已兑付金额")
    private Long payAmount;
    /**
     * 网信状态
     1：等待审核
     2：不予批准
     3：等待保荐
     4：不予保荐
     5：正在申购
     6：发行成功
     7：兑付完成
     */
    @ApiModelProperty("网信状态\n" +
            "     1：等待审核\n" +
            "     2：不予批准\n" +
            "     3：等待保荐\n" +
            "     4：不予保荐\n" +
            "     5：正在申购\n" +
            "     6：发行成功\n" +
            "     7：兑付完成")
    private Integer status;

    @ApiModelProperty("网信购买的人数")
    private Long applyCount;

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

    public String getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
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

    public String getImportIdCard() {
        return importIdCard;
    }

    public void setImportIdCard(String importIdCard) {
        this.importIdCard = importIdCard;
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

    public Long getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Long applyCount) {
        this.applyCount = applyCount;
    }

    @Override
    public String toString() {
        return "TokenCurrencyVo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", frontStyle='" + frontStyle + '\'' +
                ", backStyle='" + backStyle + '\'' +
                ", sellerIds='" + sellerIds + '\'' +
                ", importNetNum='" + importNetNum + '\'' +
                ", importName='" + importName + '\'' +
                ", importPhone='" + importPhone + '\'' +
                ", importIdCard='" + importIdCard + '\'' +
                ", releaseNum=" + releaseNum +
                ", releaseTime=" + releaseTime +
                ", faceValue=" + faceValue +
                ", applyPrice=" + applyPrice +
                ", buyFactor=" + buyFactor +
                ", growthRate=" + growthRate +
                ", royaltyRatio=" + royaltyRatio +
                ", remark='" + remark + '\'' +
                ", buyAmount=" + buyAmount +
                ", payAmount=" + payAmount +
                ", status=" + status +
                ", applyCount=" + applyCount +
                '}';
    }
}
