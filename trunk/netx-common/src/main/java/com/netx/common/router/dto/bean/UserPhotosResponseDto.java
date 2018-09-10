package com.netx.common.router.dto.bean;

import com.netx.common.user.model.UserPhotoImg;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 用户照片Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class UserPhotosResponseDto {
    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("相册")
    private List<UserPhotoImg> imgUrls;

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public List<UserPhotoImg> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<UserPhotoImg> imgUrls) {
        this.imgUrls = imgUrls;
    }

    @Override
    public String toString() {
        return "UserPhotosResponseDto{" +
                "headImgUrl='" + headImgUrl + '\'' +
                ", imgUrls=" + imgUrls +
                '}';
    }
}
