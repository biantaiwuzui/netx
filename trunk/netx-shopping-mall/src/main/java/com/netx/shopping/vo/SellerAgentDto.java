package com.netx.shopping.vo;

import java.util.Date;

public class SellerAgentDto {
    /**
     * 商家id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 一级商家数
     */
    private Integer secondNum;
    /**
     * 二级商家数
     */
    private Integer thirdNum;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 省
     */
    private String provinceCode;
    /**
     * 市
     */
    private String cityCode;
    /**
     * 累计业绩
     */
    private Long achievementTotal;
    /**
     * 月业绩
     */
    private Integer achievementMonth;
    /**
     * 月一级商家数
     */
    private Integer monthSecondNum;
    /**
     * 月二级商家数
     */
    private Integer monthThirdNum;
    /**
     * 国家排名
     */
    private Long rownum;

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

    public Integer getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(Integer secondNum) {
        this.secondNum = secondNum;
    }

    public Integer getThirdNum() {
        return thirdNum;
    }

    public void setThirdNum(Integer thirdNum) {
        this.thirdNum = thirdNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Long getAchievementTotal() {
        return achievementTotal;
    }

    public void setAchievementTotal(Long achievementTotal) {
        this.achievementTotal = achievementTotal;
    }

    public Integer getAchievementMonth() {
        return achievementMonth;
    }

    public void setAchievementMonth(Integer achievementMonth) {
        this.achievementMonth = achievementMonth;
    }

    public Integer getMonthSecondNum() {
        return monthSecondNum;
    }

    public void setMonthSecondNum(Integer monthSecondNum) {
        this.monthSecondNum = monthSecondNum;
    }

    public Integer getMonthThirdNum() {
        return monthThirdNum;
    }

    public void setMonthThirdNum(Integer monthThirdNum) {
        this.monthThirdNum = monthThirdNum;
    }

    public Long getRownum() {
        return rownum;
    }

    public void setRownum(Long rownum) {
        this.rownum = rownum;
    }
}
