package com.netx.common.user.dto.information;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class SelectUserListByDetailRequestDto {

    @Valid
    private CommonListDto page;

    @ApiModelProperty("昵称（自定义搜索词）")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("距离")
    private BigDecimal radius;

    @Min(value = 0, message = "在线时间最小值不能小于0")
    @ApiModelProperty("在线时间")
    private Long onlineTime;

    @ApiModelProperty("情感")
    private String emotion;

    @Min(value=0, message = "最大年龄值不能小于0")
    @ApiModelProperty("最大年龄值")
    private int maxAge;
    @Min(value=0, message = "最小年龄值不能小于0")
    @ApiModelProperty("最小年龄值")
    private int minAge;

    @ApiModelProperty("学历")
    private String degree;

    @Min(value=0, message = "最大身高值不能小于0")
    @ApiModelProperty("最大身高值")
    private int maxHeight;
    @Min(value=0, message = "最小身高值不能小于0")
    @ApiModelProperty("最小身高值")
    private int minHeight;

    @Min(value=0, message = "最大体重值不能小于0")
    @ApiModelProperty("最大体重值")
    private int maxWeight;
    @Min(value=0, message = "最小体重值不能小于0")
    @ApiModelProperty("最小体重值")
    private int minWeight;

    @Min(value=0, message = "最大收入值不能小于0")
    @ApiModelProperty("最大收入值")
    private int maxIncome;
    @Min(value=0, message = "最小收入值不能小于0")
    @ApiModelProperty("最小收入值")
    private int minIncome;

    @ApiModelProperty("民族")
    private String nation;

    @ApiModelProperty("属相")
    private String animalSigns;

    @ApiModelProperty("星座")
    private String starSign;

    public CommonListDto getPage() {
        return page;
    }

    public void setPage(CommonListDto page) {
        this.page = page;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public Integer getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Integer maxIncome) {
        this.maxIncome = maxIncome;
    }

    public Integer getMinIncome() {
        return minIncome;
    }

    public void setMinIncome(Integer minIncome) {
        this.minIncome = minIncome;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAnimalSigns() {
        return animalSigns;
    }

    public void setAnimalSigns(String animalSigns) {
        this.animalSigns = animalSigns;
    }

    public String getStarSign() {
        return starSign;
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public BigDecimal getRadius() {
        return radius;
    }

    public void setRadius(BigDecimal radius) {
        this.radius = radius;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public void setMaxIncome(int maxIncome) {
        this.maxIncome = maxIncome;
    }

    public void setMinIncome(int minIncome) {
        this.minIncome = minIncome;
    }

}
