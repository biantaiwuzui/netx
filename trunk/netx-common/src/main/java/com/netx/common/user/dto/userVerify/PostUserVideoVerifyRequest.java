package com.netx.common.user.dto.userVerify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("视频验证请求信息")
public class PostUserVideoVerifyRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "视频url不能为空")
    @ApiModelProperty("视频url")
    private String videoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "PostUserVideoVerifyRequest{" +
                "userId='" + userId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
