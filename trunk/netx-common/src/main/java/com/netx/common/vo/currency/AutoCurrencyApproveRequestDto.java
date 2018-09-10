package com.netx.common.vo.currency;

/**
 * 网信审核
 * 包括   法人代表定时任务审核参数dto     ，保荐人定时任务审核参数dto
 * @Author 浩俊
 * @Date 2017-10-30
 */

public class AutoCurrencyApproveRequestDto {

    /**
     * 网信ID
     */
    private String currencyId;

    /**
     *法人代表用户ID
     */
    private String userId;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
