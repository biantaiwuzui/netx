package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SkillDataDto {

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 预约单ID
     */
    private String id;

    /**
     * 退款ID
     */
    private String refundId;

    /**
     * 订单ID
     */
    private String orderId;


    /**
     * 预约数量

     */
    private long number;

    /**
     * 预约者ID
     * */
    private  String userId;

    /**
     * 支付
     */
    private boolean isPay;

    /**
     * 技能ID
     * */
    private String SkillId;

    /**
     * 预约者ID
     * */
    private String reId;

    /**
     *预约者创建时间
     * */
    private Date reCreateTime;

    /**
     *技能创建时间
     * */
    private Date skilCreateTime;

    /**
     * code
     * */
    private Integer code;

    /**
     * 状态
     * */
    private Integer status;

    /**
     * 状态：
     状态：
     0：初始化
     1：已开始
     2：已取消
     3：已成功
     4：已失败

     */

    /**
     * 用户网号
     * */
    private String userNumber;

    /**
     * 订单状态
     * */
    private Integer orderStu;


    /**
     * 昵称
     * */
    private String nickname;

    /**
     * 头像
     * */
    private String headImgUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户距离
     */
    private Double reDistance;

    /**
     * 预约单距离
     */
    private Double skDistance;

    /**
     * 发布者性别
     */
    private String sex;

    /**
     * 发布者生日
     */
    private Date birthday;

    /**
     * 发布者信用
     */
    private Integer credit;

    /**
     * 发布者等级
     */
    private Integer lv;

    /**
     * 发布者的年龄
     */
    private Integer age;

    /**
     * 返回信息 
     */
    private Object refund;

    @ApiModelProperty(value = "是否评论")
    private Boolean comments;

    @ApiModelProperty(value = "是否匿名")
    private Boolean isAnonymity;


    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Boolean getComments() {
        return comments;
    }

    public void setComments(Boolean comments) {
        this.comments = comments;
    }

    public Object getRefund() {
        return refund;
    }

    public void setRefund(Object refund) {
        this.refund = refund;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public Integer getLv() {
        return lv;
    }

    public Integer getCode() {
        return code;
    }

    public String getReId() {
        return reId;
    }

    public Double getSkDistance() {
        return skDistance;
    }

    public void setSkDistance(Double skDistance) {
        this.skDistance = skDistance;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getReDistance() {
        return reDistance;
    }

    public void setReDistance(Double reDistance) {
        this.reDistance = reDistance;
    }

    public Date getReCreateTime() {
        return reCreateTime;
    }

    public void setReCreateTime(Date reCreateTime) {
        this.reCreateTime = reCreateTime;
    }

    public Date getSkilCreateTime() {
        return skilCreateTime;
    }

    public void setSkilCreateTime(Date skilCreateTime) {
        this.skilCreateTime = skilCreateTime;
    }

    public String getSkillId() { return SkillId; }

    public void setSkillId(String skillId) { SkillId = skillId; }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStu() {
        return orderStu;
    }

    public void setOrderStu(Integer orderStu) {
        this.orderStu = orderStu;
    }


    @Override
    public String toString() {
        return "SkillDataDto{" +
                "amount=" + amount +
                ", id='" + id + '\'' +
                ", refundId='" + refundId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", number=" + number +
                ", userId='" + userId + '\'' +
                ", isPay=" + isPay +
                ", SkillId='" + SkillId + '\'' +
                ", reId='" + reId + '\'' +
                ", reCreateTime=" + reCreateTime +
                ", skilCreateTime=" + skilCreateTime +
                ", code=" + code +
                ", status=" + status +
                ", userNumber='" + userNumber + '\'' +
                ", orderStu=" + orderStu +
                ", nickname='" + nickname + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", reDistance=" + reDistance +
                ", skDistance=" + skDistance +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", credit=" + credit +
                ", lv=" + lv +
                ", age=" + age +
                ", refund=" + refund +
                ", comments=" + comments +
                ", isAnonymity=" + isAnonymity +
                '}';
    }
}
