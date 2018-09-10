package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Yawn on 2018/8/4 0004.
 */
public class MatchRequirementDataDTO {
    /**
     * 要求ID号
     */
    @ApiModelProperty(value = "要求ID")
    private String requirementId;
    /**
     * 要求名称
     */
    @ApiModelProperty(value = "要求名称")
    private String requirementTitle;
    /**
     * 资料介绍
     */
    @ApiModelProperty(value = "资料介绍")
    private String introduction;
    /**
     * 资料图片
     */
    @ApiModelProperty(value = "图片资料")
    private String imagesUrl;


    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getRequirementTitle() {
        return requirementTitle;
    }

    public void setRequirementTitle(String requirementTitle) {
        this.requirementTitle = requirementTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
