package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * 门票支付表
 * Created by Yawn on 2018/8/1 0001.
 */
public class MatchAudienceDTO {
    @ApiModelProperty(value = "id(插入的时候不用传，更新的时候传)")
    private String Id;
    /**
     * 门票的ID
     */
    @ApiModelProperty(value = "门票的ID")
    private String matchTicketId;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 是否支付
     */
    @ApiModelProperty(value = "是否支付")
    private Boolean isPay;
    /**
     *是否出席
     */
    @ApiModelProperty(value = "是否出席")
    private Boolean isAttend;

    @ApiModelProperty(value = "赛事的ID")
    private String matchId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchTicketId() {
        return matchTicketId;
    }

    public void setMatchTicketId(String matchTicketId) {
        this.matchTicketId = matchTicketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getAttend() {
        return isAttend;
    }

    public void setAttend(Boolean attend) {
        isAttend = attend;
    }
}
