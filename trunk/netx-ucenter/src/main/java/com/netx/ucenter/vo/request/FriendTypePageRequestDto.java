package com.netx.ucenter.vo.request;

import com.netx.common.vo.common.PageRequestDto;
import com.netx.searchengine.enums.FriendTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public class FriendTypePageRequestDto extends PageRequestDto{
    @ApiModelProperty("搜索类型")
    @NotNull(message = "搜索类型不能为空")
    private FriendTypeEnum friendTypeEnum;

    public FriendTypeEnum getFriendTypeEnum() {
        return friendTypeEnum;
    }

    public void setFriendTypeEnum(FriendTypeEnum friendTypeEnum) {
        this.friendTypeEnum = friendTypeEnum;
    }

}
