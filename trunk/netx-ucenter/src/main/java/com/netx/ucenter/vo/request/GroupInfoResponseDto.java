package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户是存在组dto类
 * @Author hj.Mao
 * @Date 2017-11-18
 */
public class GroupInfoResponseDto{

    private Boolean wetherExist;

    @ApiModelProperty("groupId")
    private Long groupId;

    public Boolean isWetherExist() {
        return wetherExist;
    }

    public void setWetherExist(Boolean wetherExist) {
        this.wetherExist = wetherExist;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
