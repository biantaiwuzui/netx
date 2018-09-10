package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hj.Mao on 13/3/18.
 */
public class CreditSearchQuery extends BaseSearchQuery{

    /**
     * 网信名称
     */
    private String name;

    /**
     * 网信标签
     */
    private String tag;

    /**
     * 发行单价
     */
    private Integer faceValue;
    /**
     * 回购系数
     */
    private BigDecimal buyFactor;

    /**
     * 分红比例
     */
    private Integer royaltyRatio;

    /**
     * 发行状态, 1：正在发行; 2: 发行完成
     */
    private Integer status;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> firstAscQueries = new ArrayList<>();
    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public BigDecimal getBuyFactor() {
        return buyFactor;
    }

    public void setBuyFactor(BigDecimal buyFactor) {
        this.buyFactor = buyFactor;
    }

    public Integer getRoyaltyRatio() {
        return royaltyRatio;
    }

    public void setRoyaltyRatio(Integer royaltyRatio) {
        this.royaltyRatio = royaltyRatio;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<LastAscQuery> getFirstAscQueries() {
        return firstAscQueries;
    }

    public void setFirstAscQueries(List<LastAscQuery> firstAscQueries) {
        if (firstAscQueries != null && firstAscQueries.size() > 0) {
            this.firstAscQueries = firstAscQueries;
        }
    }

    public void addFirstAscQueries(LastAscQuery firstAscQueries) {
        this.firstAscQueries.add(firstAscQueries);
    }

    public List<LastAscQuery> getLastAscQueries() {
        return lastAscQueries;
    }

    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        if (lastAscQueries != null && lastAscQueries.size() > 0) {
            this.lastAscQueries = lastAscQueries;
        }
    }

    public void addLastAscQuery(LastAscQuery lastAscQuery) {
        this.lastAscQueries.add(lastAscQuery);
    }

}
