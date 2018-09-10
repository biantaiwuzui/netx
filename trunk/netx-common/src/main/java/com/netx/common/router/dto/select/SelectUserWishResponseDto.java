package com.netx.common.router.dto.select;

import com.netx.common.user.model.UserInfo;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 跨模块使用的用户心愿类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-16
 */
public class SelectUserWishResponseDto {
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    @ApiModelProperty("心愿信息")
    private SelectWishDetailDataResponseDto selectWishDetailDataResponseDto;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public SelectWishDetailDataResponseDto getSelectWishDetailDataResponseDto() {
        return selectWishDetailDataResponseDto;
    }

    public void setSelectWishDetailDataResponseDto(SelectWishDetailDataResponseDto selectWishDetailDataResponseDto) {
        this.selectWishDetailDataResponseDto = selectWishDetailDataResponseDto;
    }
}
