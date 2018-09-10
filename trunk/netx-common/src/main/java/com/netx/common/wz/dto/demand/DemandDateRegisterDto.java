package com.netx.common.wz.dto.demand;

import com.netx.common.user.util.ComputeAgeUtils;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DemandDateRegisterDto {

    @ApiModelProperty("报酬")
    private BigDecimal wage;

    @ApiModelProperty("申请时间")
    private Date createtime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级")
    private Integer lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("出生年月日")
    private Date birthday;

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        this.age = ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public void setBirthday(String birthday){
        if(birthday!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                setBirthday(dateFormat.parse(birthday));
            }catch (Exception e){ }
        }
    }

    public Date getBirthday() {
        return birthday;
    }
}
