package com.netx.common.router.dto.bean;

import com.netx.common.router.dto.select.SelectUserBeanResponseDto;
import com.netx.common.router.dto.select.SelectUserOtherNumResponseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户统计数据基类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class UserBeanData {
    @ApiModelProperty("用户统计(图文数、音频数、邀请数、礼物数)")
    private SelectUserOtherNumResponseDto otherNum;

    @ApiModelProperty("用户基本信息")
    private SelectUserBeanResponseDto detail;

    public SelectUserOtherNumResponseDto getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(SelectUserOtherNumResponseDto otherNum) {
        this.otherNum = otherNum;
    }

    public SelectUserBeanResponseDto getDetail() {
        return detail;
    }

    public void setDetail(SelectUserBeanResponseDto detail) {
        this.detail = detail;
    }
}
