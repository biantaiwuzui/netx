package com.netx.worth.vo;

import com.netx.worth.model.MatchRequirementData;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 审核参赛者VO
 * Created by Yawn on 2018/8/18 0018.
 */
public class MatchParticipantReviewVo {
    @ApiModelProperty(value = "参赛者id,通过审核是传这个过来")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "参赛项目名称")
    private String projectName;
    @ApiModelProperty(value = "年龄")
    private int age;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "头像，用户长传参赛要求的首张照片，不是用户头像")
    private String headImg;
    @ApiModelProperty(value = "参赛时的要求")
    private String requirement_text;
    @ApiModelProperty(value = "是否已支付报名费用")
    private Boolean isPay;
    @ApiModelProperty(value = "是否通过审核")
    private Boolean isPass;
    @ApiModelProperty(value = "是否在现场")
    private Boolean isSpot;
    @ApiModelProperty(value = "是否监护人")
    private Boolean isGuardian;
    @ApiModelProperty(value = "是否团队")
    private Boolean isTeam;


    public Boolean getGuardian() {
        return isGuardian;
    }

    public void setGuardian(Boolean guardian) {
        isGuardian = guardian;
    }

    public Boolean getTeam() {
        return isTeam;
    }

    public void setTeam(Boolean team) {
        isTeam = team;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRequirement_text() {
        return requirement_text;
    }

    public void setRequirement_text(String requirement_text) {
        this.requirement_text = requirement_text;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public Boolean getSpot() {
        return isSpot;
    }

    public void setSpot(Boolean spot) {
        isSpot = spot;
    }
}
