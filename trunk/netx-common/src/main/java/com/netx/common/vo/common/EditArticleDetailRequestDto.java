package com.netx.common.vo.common;

import com.netx.common.user.enums.ArticleTypeEnum;
import com.netx.common.user.enums.WhoCanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ApiModel("修改图文详情信息")
public class EditArticleDetailRequestDto {

    @NotBlank(message = "图文ID不能为空")
    @ApiModelProperty("图文ID")
    private String id;

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private String userId;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @NotBlank(message = "封面src不能为空(默认)")
    @ApiModelProperty("封面图片")
    private String pic;

    @ApiModelProperty("附件")
    private String atta;

    @NotBlank(message = "资讯内容不能为空")
    @ApiModelProperty("资讯主要内容")
    private String content;

    @NotNull(message = "分享范围不能为空")
    @ApiModelProperty("分享一下")
    private WhoCanEnum who;

    @NotEmpty
    @ApiModelProperty("指定分享的用户ID,已 ，分隔开")
    private String receiver;

    @NotNull
    @ApiModelProperty("是否匿名发布")
    private Boolean isAnonymity;

    @NotNull
    @ApiModelProperty("是否显示位置信息")
    private Boolean isShowLocation;

    @NotNull
    @ApiModelProperty("是否申明为软文")
    private Boolean isAdvertorial;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAtta() {
        return atta;
    }

    public void setAtta(String atta) {
        this.atta = atta;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WhoCanEnum getWho() {
        return who;
    }

    public void setWho(WhoCanEnum who) {
        this.who = who;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Boolean getShowLocation() {
        return isShowLocation;
    }

    public void setShowLocation(Boolean showLocation) {
        isShowLocation = showLocation;
    }

    public Boolean getAdvertorial() {
        return isAdvertorial;
    }

    public void setAdvertorial(Boolean advertorial) {
        isAdvertorial = advertorial;
    }

    @Override
    public String toString() {
        return "EditArticleDetailRequestDto{" +
                "userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", atta='" + atta + '\'' +
                ", content='" + content + '\'' +
                ", who=" + who +
                ", receiver='" + receiver + '\'' +
                ", isAnonymity=" + isAnonymity +
                ", isShowLocation=" + isShowLocation +
                '}';
    }
}
