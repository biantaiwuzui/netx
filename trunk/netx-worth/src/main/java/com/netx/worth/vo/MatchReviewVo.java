package com.netx.worth.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.netx.worth.model.MatchReview;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 赛程单位得Vo
 */
public class MatchReviewVo {

    /**
     * 主键
     */
    private String id;
    /**
     * 比赛id
     */
    private String matchId;
    /**
     * 审核者id
     */
    private String userId;
    private String organizerName;
    private Integer organizerKind;
    private Boolean isAccept;
    private Boolean isApprove;
    private String logo;
    private String credit;
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public Integer getOrganizerKind() {
        return organizerKind;
    }

    public void setOrganizerKind(Integer organizerKind) {
        this.organizerKind = organizerKind;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }

    public Boolean getApprove() {
        return isApprove;
    }

    public void setApprove(Boolean approve) {
        isApprove = approve;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
