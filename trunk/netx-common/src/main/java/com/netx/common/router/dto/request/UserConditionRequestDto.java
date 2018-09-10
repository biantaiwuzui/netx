package com.netx.common.router.dto.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户信息查询条件Request类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class UserConditionRequestDto {
    @ApiModelProperty("查询用户图片（0:无需用户图片;1:只需头像;2:需要头像和相册）")
    private Integer userPhotoType;

    @ApiModelProperty("是否需要用户位置")
    private boolean isUserGeo;

    @ApiModelProperty("是否需要个性标签(性格：以','隔开)")
    private boolean isDispositionTag;

    @ApiModelProperty("是否需要常驻地")
    private boolean isOftenIn;

    public UserConditionRequestDto() {
        userPhotoType=2;
        isUserGeo=true;
        isDispositionTag=true;
        isOftenIn=true;
    }

    public Integer getUserPhotoType() {
        return userPhotoType;
    }

    public void setUserPhotoType(Integer userPhotoType) {
        this.userPhotoType = userPhotoType;
    }

    public boolean isUserGeo() {
        return isUserGeo;
    }

    public void setUserGeo(boolean userGeo) {
        isUserGeo = userGeo;
    }

    public boolean isDispositionTag() {
        return isDispositionTag;
    }

    public void setDispositionTag(boolean dispositionTag) {
        isDispositionTag = dispositionTag;
    }

    public boolean isOftenIn() {
        return isOftenIn;
    }

    public void setOftenIn(boolean oftenIn) {
        isOftenIn = oftenIn;
    }
}
