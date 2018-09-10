package com.netx.searchengine.model;

//import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by CloudZou on 2/10/18.
 */
public class MeetingSearchResponse {
    private String id;
    /**
     * 主发起人ID
     */
    private String userId;
    /**
     * 主题
     */
    private String title;
    /**
     * 活动形式：
     1：1对1
     2：多对多
     3：纯线上活动
     4：不发生消费的线下活动
     */
    private Integer meetingType;
    /**
     * 活动标签，逗号分隔
     */
    private List<String> meetingLabels;
    /**
     * 活动开始时间
     */
    private Date startedAt;
    /**
     * 活动结束时间
     */
    private Date endAt;
    /**
     * 活动截至报名时间
     */
    private Date regStopAt;
    /**
     * 报名对象：
     1：不限制。
     2：仅限女性报名
     3：仅限男性报名
     4：仅允许我的好友报名
     5：仅限指定人员报名
     */
    private Integer obj;
    /**
     * 指定报名对象列表，逗号分隔
     */
    private List<String> objList;
    /**
     * 报名费：0为免费
     */
    private BigDecimal amount;
    /**
     * 活动地址
     */
    private String address;
    /**
     * 订单列表
     */
    private String orderIds;
    /**
     * 活动消费
     */
    private BigDecimal orderPrice;
    /**
     * 描述
     */
    private String description;
    /**
     * 海报图片
     */
    private String posterImagesUrl;
    /**
     * 活动图片
     */
    private String meetingImagesUrl;
    /**
     * 报名人数上限
     */
    private Integer ceil;
    /**
     * 报名人数下限
     */
    private Integer floor;
    /**
     * 活动状态：
     0：已发起，报名中
     1：报名截止，已确定入选人
     2：活动取消
     3：活动失败
     4：活动成功
     5：同意开始，分发验证码
     */
    private Integer status;
    /**
     * 费用不足时：
     1：由我补足差额，活动正常进行
     2：活动自动取消，报名费用全部返回
     */
    private Integer feeNotEnough;
    /**
     * 是否确认细节（地址、消费）
     */
    private Boolean isConfirm;
    /**
     * 版本
     */
    //@Version
    private Integer lockVersion;
    /**
     * 总报名费
     */
    private BigDecimal allRegisterAmount;
    /**
     * 消费差额，需补足的部分：总订单金额-总报名费
     */
    private BigDecimal balance;
    /**
     * 是否补足
     */
    private Boolean isBalancePay;
    /**
     * 补足人
     */
    private String payFrom;
    /**
     * 距离
     */
    private Double distance;

    /**
     * 活动发布者昵称
     */
    private String nickname;

    /**
     * 活动发布者性别
     */
    private String sex;

    /**
     * 活动发布者生日
     */
    private Date birthday;

    /**
     * 活动发布者手机
     */
    private String mobile;

    /**
     * 活动发布者总积分
     */
    private BigDecimal score;

    /**
     * 活动发布者信用
     */
    private Integer credit;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 活动发布者等级
     */
    private Integer lv;

    /**
     * 发布网信数量
     */
    private Integer creditSum;

    /**
     * 报名人数
     */
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
    }

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

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public List<String> getMeetingLabels() {
        return meetingLabels;
    }

    public void setMeetingLabels(List<String> meetingLabels) {
        this.meetingLabels = meetingLabels;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Date getRegStopAt() {
        return regStopAt;
    }

    public void setRegStopAt(Date regStopAt) {
        this.regStopAt = regStopAt;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
    }

    public List<String> getObjList() {
        return objList;
    }

    public void setObjList(List<String> objList) {
        this.objList = objList;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPosterImagesUrl() {
		return posterImagesUrl;
	}

	public void setPosterImagesUrl(String posterImagesUrl) {
		this.posterImagesUrl = posterImagesUrl;
	}

	public String getMeetingImagesUrl() {
		return meetingImagesUrl;
	}

	public void setMeetingImagesUrl(String meetingImagesUrl) {
		this.meetingImagesUrl = meetingImagesUrl;
	}

	public Integer getCeil() {
        return ceil;
    }

    public void setCeil(Integer ceil) {
        this.ceil = ceil;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFeeNotEnough() {
        return feeNotEnough;
    }

    public void setFeeNotEnough(Integer feeNotEnough) {
        this.feeNotEnough = feeNotEnough;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public BigDecimal getAllRegisterAmount() {
        return allRegisterAmount;
    }

    public void setAllRegisterAmount(BigDecimal allRegisterAmount) {
        this.allRegisterAmount = allRegisterAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getBalancePay() {
        return isBalancePay;
    }

    public void setBalancePay(Boolean balancePay) {
        isBalancePay = balancePay;
    }

    public String getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(String payFrom) {
        this.payFrom = payFrom;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }
}

