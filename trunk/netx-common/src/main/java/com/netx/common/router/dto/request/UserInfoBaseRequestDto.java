package com.netx.common.router.dto.request;

import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserInfoBaseRequestDto {

    @NotNull(message = "查询条件不能为空")
    @ApiModelProperty("查询条件，目前仅支持：<br>" +
            "①用户id<br>" +
            "②网号<br>" +
            "③手机号<br>")
    private SelectConditionEnum selectConditionEnum;


    @NotNull(message = "查询字段列表不能为null")
    @ApiModelProperty("查询字段列表：<br>" +
            "<b>注意：</b><br>" +
            "1、现在列表为空，不再查询所有信息，列表为空，只返回null<br>" +
            "2、为了增加性能，已删掉不常用的可查询字段，若有需要的数据没有，请向用户中心成员提出<br>" +
            "3、现在查询出的列表必定存在userId信息，另外，根据什么样的条件查询，条件值也会查出，<br>" +
            "例如：根据网号查询，那查询出的列表里也会有网号的信息，不再需要填写字段“USER_NUMBER”<br><br>" +
            "//====== 1、用户表 ======<br>" +
            "    //------ 基本信息 ------<br>" +
            "    USER_NUMBER：网号<br>" +
            "    NICKNAME：昵称<br>" +
            "    SEX：性别<br>" +
            "    AGE：年龄<br>" +
            "    LV：等级lv<br>" +
            "    USER_PROFILE_SCORE：用户资料分值,<br>" +
            "    //------ 认证信息 ------<br>" +
            "    （除了手机号，其它认证信息详情都不公开，仅用于查看是否已经认证，若已认证，则信息不为null）<br>" +
            "    （有需要视频、车辆、房产、学历认证信息请提出）<br>" +
            "    MOBILE：手机号<br>" +
            /*"    ID_NUMBER：身份证号<br>" +
            "    VIDEO：视频信息<br>" +
            "    CAR：车辆信息<br>" +
            "    HOUSE：房产信息<br>" +
            "    DEGREE：学历信息<br>" +*/
            "    //------ 综合信息 ------<br>" +
            "    （有需要文化、工作、兴趣概况的请提出）<br>" +
            /*"    EDUCATION_LABEL：文化教育概况<br>" +
            "    PROFESSION_LABEL：工作经历概况<br>" +
            "    INTEREST_LABEL：兴趣爱好概况<br>" +*/
            "    //------ 网名信息 ------<br>" +
            "    SCORE：总积分<br>" +
            "    CREDIT：总信用<br>" +
            "    VALUE：总身价<br>" +
            "    INCOME：总收益<br>" +
            "    CONTRIBUTION：总贡献<br>" +
            "    //------ 其他信息 ------<br>" +
            "    LOCK_VERSION：乐观锁<br>" +
            "    LAST_LOGIN_AT：最后登录时间<br>" +
            //"    LOGIN_DAYS：连续登录天数<br>" +
            "    GIFT_SETTING：礼物设置<br>" +
            "    INVITATION_SETTING：邀请设置<br>" +
            "    ROLE：角色<br>【角色返回信息详解：1.系统管理，2.用户管理，3.商家管理，4.咨讯管理，5.财务管理，6.仲裁管理】" +
            "<br>" +
            "    //====== 2、用户详情表 ======<br>" +
            "    OFTEN_IN：常驻<br>" +
            "    DISPOSITION：性格（个性标签）<br>" +
            "<br>" +
            "    //====== 3、用户照片表 ======<br>" +
            "    HEAD_IMG_URL：头像")
    private List<SelectFieldEnum> selectFieldEnumList;

    public SelectConditionEnum getSelectConditionEnum() {
        return selectConditionEnum;
    }

    public void setSelectConditionEnum(SelectConditionEnum selectConditionEnum) {
        this.selectConditionEnum = selectConditionEnum;
    }

    public List<SelectFieldEnum> getSelectFieldEnumList() {
        return selectFieldEnumList;
    }

    public void setSelectFieldEnumList(List<SelectFieldEnum> selectFieldEnumList) {
        this.selectFieldEnumList = selectFieldEnumList;
    }

}
