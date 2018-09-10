package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 发布网信请求参数对象
 * Date: 2017-08-30
 */
@ApiModel
public class ReleaseCurrencyRequestDto {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("网信名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("网信id")
    private String id;

    @ApiModelProperty("网信标签，多个用逗号隔开")
    private String tagIds;

    @ApiModelProperty("网信正面样式，图片")
    private String frontStyle;

    @ApiModelProperty("网信反面样式，图片")
    private String backStyle;

    @ApiModelProperty("是否适用范围")
    private Boolean isFitScopeOne;

    @ApiModelProperty("是否适用范围2，选择商家")
    private Boolean isFitScopeTwo;

    /**
     * 选择范围商家，选择适用范围2的时候必填
     */
    @ApiModelProperty("选择范围商家，选择适用范围2的时候必填")
    private List<String> selectSellers;

    @ApiModelProperty("是否适用范围3")
    private Boolean isFitScopeThree;

    @ApiModelProperty("结转用户网号")
    private String importNetNum;

    @ApiModelProperty("结转用户姓名")
    private String importName;

    @ApiModelProperty("结转用户手机号")
    private String importPhone;

    @ApiModelProperty("结转用户身份证")
    private String importIdCard;

    @ApiModelProperty("发行数量")
    private Integer releaseNum;

    @ApiModelProperty("网信面值")
    private Integer faceValue;

    @ApiModelProperty("申购单价")
    private Integer applyPrice;

    @ApiModelProperty("回购系数")
    private BigDecimal buyFactor;

    @ApiModelProperty("递增幅度")
    private Integer growthRate;

    @ApiModelProperty("固定分红提成比例")
    private Integer royaltyRatio;

    @ApiModelProperty("网信说明")
    private String remark;

    @ApiModelProperty("选择保荐人，至少五个")
    @NotEmpty(message = "请选择保荐人")
    private List<String> selectRecommenders;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getSelectSellers() {
        return selectSellers;
    }

    public void setSelectSellers(List<String> selectSellers) {
        this.selectSellers = selectSellers;
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

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Integer applyPrice) {
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

    public List<String> getSelectRecommenders() {
        return selectRecommenders;
    }

    public void setSelectRecommenders(List<String> selectRecommenders) {
        this.selectRecommenders = selectRecommenders;
    }
}
