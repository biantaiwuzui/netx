package com.netx.common.wz.dto.matchEvent;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 参赛资料
 * Created by Yawn on 2018/8/2 0002.
 */
public class MatchChildInfoDTO {
    @ApiModelProperty(value = "用户团体报名的时候填用户的userId，其他情况不填")
    private String id;
    /**
     * 监护人ID
     */
    @ApiModelProperty(value = "监护人ID")
    private String participantId;

    @ApiModelProperty(value = "头像")
    private String headImage;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "孩子姓名")
    private String name;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "出生日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;
    /**
     * 介绍
     */
    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "参赛要求",notes = "填JSON自己解析")
    private String otherRequirement;
    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private String imagesUrl;

    @ApiModelProperty(value = "报名要求")
    private List<MatchRequirementDataDTO> matchRequirementDataDTOList;

    @ApiModelProperty(value = "团队成员的userId")
    private String userId;
    @ApiModelProperty("手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}|9[2-8]\\d{9}$|(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$", message = "手机号码不正确!")
    private String mobile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<MatchRequirementDataDTO> getMatchRequirementDataDTOList() {
        return matchRequirementDataDTOList;
    }

    public void setMatchRequirementDataDTOList(List<MatchRequirementDataDTO> matchRequirementDataDTOList) {
        this.matchRequirementDataDTOList = matchRequirementDataDTOList;
    }

    public String getOtherRequirement() {
        return otherRequirement;
    }

    public void setOtherRequirement(String otherRequirement) {
        this.otherRequirement = otherRequirement;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
