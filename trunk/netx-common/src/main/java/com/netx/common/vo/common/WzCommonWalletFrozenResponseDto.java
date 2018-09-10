package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class WzCommonWalletFrozenResponseDto {
    @ApiModelProperty("冻结id")
    private String id;
    /**
     * 消费渠道活动,需求,心愿,技能,商品,网币,用类名表示
     */
    @ApiModelProperty("消费渠道活动,需求,心愿,技能,商品,网币,用类名表示")
    private String frozenType;
    /**
     * 冻结金额
     */
    @ApiModelProperty("冻结金额")
    private BigDecimal amount;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 交易对象id即钱报用户id
     */
    @ApiModelProperty("交易对象id即钱报用户id")
    private String toUserId;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * frozenType事件id
     */
    @ApiModelProperty("frozenType事件id")
    private String typeId;

    /**
     * 是否生效
     */
    @ApiModelProperty("是否生效")
    private Integer delTag;
    /**
     * 是否已消费
     */
    @ApiModelProperty("是否已消费")
    private Integer hasConsume;
    /**
     * 等于dto的tradeType
     * 交易方式.0零钱,1网币,2混合支付
     */
    @ApiModelProperty("等于dto的tradeType,交易方式.0零钱,1网币,2混合支付")
    private String bak1;
    /**
     * 等于dto的网币id
     */
    @ApiModelProperty("等于dto的网币id")
    private String bak2;
    /**
     * 冻结零钱还是网币,0零钱,1网币
     */
    @ApiModelProperty("冻结零钱还是网币,0零钱,1网币")
    private String bak3;

    private String bak4;

    private String bak5;

    private Integer credit;

    private String nickname;

    private Integer lv;

    private String sex;

    private Integer age;

    private Integer vsn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrozenType() {
        return frozenType;
    }

    public void setFrozenType(String frozenType) {
        this.frozenType = frozenType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getDelTag() {
        return delTag;
    }

    public void setDelTag(Integer delTag) {
        this.delTag = delTag;
    }

    public Integer getHasConsume() {
        return hasConsume;
    }

    public void setHasConsume(Integer hasConsume) {
        this.hasConsume = hasConsume;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4;
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getVsn() {
        return vsn;
    }

    public void setVsn(Integer vsn) {
        this.vsn = vsn;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }
}
