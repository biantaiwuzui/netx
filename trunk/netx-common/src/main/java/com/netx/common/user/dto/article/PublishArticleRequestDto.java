package com.netx.common.user.dto.article;

import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.user.enums.ArticleTypeEnum;
import com.netx.common.user.enums.WhoCanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 李卓
 */
@ApiModel("发布图文信息")
public class PublishArticleRequestDto {

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("图文内容，若不是图文，这里为空")
    private String content;

    @NotNull(message = "谁可以看此图文动态不能为空")
    @ApiModelProperty("谁可以看此图文动态,分别是：\n" +
            "‘①所有人’，\n" +
            "‘②所有好友’，\n" +
            "‘③我关注的人’，\n" +
            "‘④关注我的人’，\n" +
            "‘⑤指定好友’")
    private WhoCanEnum who;

    @ApiModelProperty("若who的值为DESIGNED_FRIENDS时，该字段才有效，" +
            "储存指定好友id，多个以逗号隔开")
    private String receiver;

    @NotNull(message = "是否匿名（隐身发布）不能为空")
    @ApiModelProperty("是否匿名（隐身发布），true：匿名，false：不匿名")
    private Boolean isAnonymity;

    @NotNull(message = "是否显示位置不能为空")
    @ApiModelProperty("是否显示位置，true：显示，false：显示")
    private Boolean isShowLocation;

    @NotNull(message = "是否为软文")
    @ApiModelProperty("是否为软文")
    private Boolean isAdvertorial;

    @ApiModelProperty("发布时的位置名称")
    private String location;

    @ApiModelProperty("图文内容长度")
    private int length=0;

    @ApiModelProperty("图文标签，多个用逗号隔开")
    @NotBlank(message = "图文标签不能为空")
    private String tagNames;

    @ApiModelProperty(value = "图文类型",notes = "1.WISH_TYPE(心愿)，" +
            "2.DEMAND_TYPE(需求)，3.SKILL_TYPE（技能），4.MEETING_TYPE（活动）" +
            "5.MATCH_TYPE（赛事），6.MERCHANT_TYPE(商家)，7.PRODUCT_TYPE(商品)")
    private WorthTypeEnum worthTypeEnum;

    @ApiModelProperty("图文相关联的id")
    private String worthTypeIds;

    public WorthTypeEnum getWorthTypeEnum() {
        return worthTypeEnum;
    }

    public void setWorthTypeEnum(WorthTypeEnum worthTypeEnum) {
        this.worthTypeEnum = worthTypeEnum;
    }

    public String getWorthTypeIds() {
        return worthTypeIds;
    }

    public void setWorthTypeIds(String worthTypeIds) {
        this.worthTypeIds = worthTypeIds;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if(StringUtils.isNotBlank(pic)){
            this.pic = pic;
        }
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

    public Boolean getIsAdvertorial() {
        return isAdvertorial;
    }

    public void setIsAdvertorial(Boolean isAdvertorial) {
        this.isAdvertorial = isAdvertorial;
    }

    public int getLength() {
        return length;
    }

    public void setLength(Integer length) {
        if(length!=null){
            this.length = length;
        }
    }
}
