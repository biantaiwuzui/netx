package com.netx.common.vo.common;

import com.netx.common.user.enums.ArticleIntroduceDateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@ApiModel
public class ArticleIntroduceToTopRequestDto {

    @ApiModelProperty("资讯Id,必须填")
    @NotEmpty(message = "资讯Id不能为空")
    public List<String> articleIds;

    @ApiModelProperty(" 在列表置顶的复选框的值：0.表示选中列表页置顶    1.表示选中在类中首页\n")
    private Integer allListTopRefund;

    @ApiModelProperty("在列表置顶数值")
    private String allListTopValue;

    /**
     * 在列表置顶的单位
     */
    @ApiModelProperty("在列表置顶的单位,枚举类型\n" +
            "ARTICLE_INTRODUCE_DATE_TYPE_HOUR(\"小时\",1),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_DAY(\"天\",2),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_WEEK(\"周\",3),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_MONTH(\"月\",4);")
    private ArticleIntroduceDateTypeEnum allListTopUnit;


    @ApiModelProperty("在类列表置顶数值")
    private String classListTopValue;

    @ApiModelProperty("在类列表置顶的单位,枚举类型\n" +
            "ARTICLE_INTRODUCE_DATE_TYPE_HOUR(\"小时\",1),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_DAY(\"天\",2),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_WEEK(\"周\",3),\n" +
            "    ARTICLE_INTRODUCE_DATE_TYPE_MONTH(\"月\",4);")
    private ArticleIntroduceDateTypeEnum classListTopUnit;

    public List<String> getArticleIds() { return articleIds; }

    public void setArticleIds(List<String> articleIds) { this.articleIds = articleIds; }

    public Integer getAllListTopRefund() {
        return allListTopRefund;
    }

    public void setAllListTopRefund(Integer allListTopRefund) {
        this.allListTopRefund = allListTopRefund;
    }

    public String getAllListTopValue() {
        return allListTopValue.trim();
    }

    public void setAllListTopValue(String allListTopValue) {
        this.allListTopValue = allListTopValue;
    }

    public ArticleIntroduceDateTypeEnum getAllListTopUnit() {
        return allListTopUnit;
    }

    public void setAllListTopUnit(ArticleIntroduceDateTypeEnum allListTopUnit) {
        this.allListTopUnit = allListTopUnit;
    }

    public String getClassListTopValue() {
        return classListTopValue.trim();
    }

    public void setClassListTopValue(String classListTopValue) {
        this.classListTopValue = classListTopValue;
    }

    public ArticleIntroduceDateTypeEnum getClassListTopUnit() {
        return classListTopUnit;
    }

    public void setClassListTopUnit(ArticleIntroduceDateTypeEnum classListTopUnit) {
        this.classListTopUnit = classListTopUnit;
    }
}
