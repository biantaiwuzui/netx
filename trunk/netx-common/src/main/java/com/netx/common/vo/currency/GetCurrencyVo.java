package com.netx.common.vo.currency;

import com.netx.common.user.dto.common.CommonUserBaseInfoDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By hj.Mao
 * Description: 获取网信详情返回对象
 * Date: 2017-11-01
 */
public class GetCurrencyVo {
    /**
     * 返回保荐人列表
     */
    private List<RecommendTokenVo>  RecommendList;

    /**
     * 返回发行者基本信息
     */
    private CommonUserBaseInfoDto issueInfo;
    /**
     * 返回保荐人用户信息列表
     */
    private List<CommonUserBaseInfoDto> recommendInfoList;

    /**
     * 返回法人列表
     */
    private List<ReviewTokenVo> ReviewList;

    /**
     *调用接口者身份(是法人代表、保荐人、发行者，申购者、还是持有者)
     */
    private String identity;

    /**
     * 上个月交易额
     */
    private BigDecimal LastMonthAmount;
    /**
     * 申购数量
     */
    private Integer applyNum;

    /**
     * 持有者id
     */
    private String holdId;

    /**
     * 持有金额
     */
    private BigDecimal holdAmount;

    public List<RecommendTokenVo> getRecommendList() {
        return RecommendList;
    }

    public void setRecommendList(List<RecommendTokenVo> recommendList) {
        RecommendList = recommendList;
    }

    public List<ReviewTokenVo> getReviewList() {
        return ReviewList;
    }

    public void setReviewList(List<ReviewTokenVo> reviewList) {
        ReviewList = reviewList;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public List<CommonUserBaseInfoDto> getRecommendInfoList() {
        return recommendInfoList;
    }

    public void setRecommendInfoList(List<CommonUserBaseInfoDto> recommendInfoList) {
        this.recommendInfoList = recommendInfoList;
    }

    public CommonUserBaseInfoDto getIssueInfo() {
        return issueInfo;
    }

    public void setIssueInfo(CommonUserBaseInfoDto issueInfo) {
        this.issueInfo = issueInfo;
    }

    public BigDecimal getLastMonthAmount() {
        return LastMonthAmount;
    }

    public void setLastMonthAmount(BigDecimal lastMonthAmount) {
        LastMonthAmount = lastMonthAmount;
    }
}
