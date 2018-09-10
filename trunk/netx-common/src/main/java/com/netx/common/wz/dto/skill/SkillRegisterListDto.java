package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

    public class SkillRegisterListDto {

        private static final long serialVersionUID = 1L;

        private String id;

        private String userId;
        /**
         * 主表id
         */

        private String skillId;

        /**
         * 预约者距离
         */
        private Double reDistance;

        /**
         * 技能距离
         */
        private Double skDistance;

        /**
         * 预约者预约的时间
         */

        private Date skilCreateTime;

        /**
         * 建议的开始时间
         */

        private Date startAt;

        /**
         * 订单状态
         */

        private Integer orStatus;

        /**
         * 建议的结束时间
         */

        private Date endAt;
        /**
         * 单位
         */
        private String unit;
        /**
         * 单价
         */
        private BigDecimal amount;
        /**
         * 数量
         */
        private Integer number;
        /**
         * 总价（单价*数量）
         */
        private BigDecimal fee;


        /**
         * 描述
         */
        private String description;
        /**
         * 地址
         */
        private String address;
        /**距离*/
        /**
         * 经度
         */
        @Range(min = -180,max = 180,message = "经度输入不在范围内")
        private BigDecimal lon;
        /**
         * 纬度
         */
        @Range(min = -90,max = 90,message = "纬度输入不在范围内")
        private BigDecimal lat;
        /**
         * 报名状态：
         0：待入选
         1：已入选
         2：已拒绝
         3：已过期
         */
        private Integer status;
        /**
         * 是否支付给发布方
         */

        private Boolean isPay;
        /**
         * 已托管的费用
         */
        private BigDecimal bail;
        /**
         * 托管的付款方式：0：网币，1：平台垫付
         */

        private Integer payWay;
        /**
         * 是否匿名
         */

        private Boolean isAnonymity;
        /**
         * 验证状态
         （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
         0：未验证
         1：已验证
         */
        
        @ApiModelProperty(value = "验证码")
        private Integer code;

        private Boolean validationStatus;
        /**
         * 验证码是否通过
         */

        private Boolean isValidation;
        /**
         * 邀请码重试次数
         */
        private Integer times;

        /**
         * 退款信息对象
         * create by JayJay 2018-09-10
         */
        private Object refund;

        private Date createTime;

        private String createUserId;

        private Date updateTime;

        private String updateUserId;


        private Integer deleted;



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

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public Date getStartAt() {
            return startAt;
        }

        public void setStartAt(Date startAt) {
            this.startAt = startAt;
        }

        public Date getEndAt() {
            return endAt;
        }

        public void setEndAt(Date endAt) {
            this.endAt = endAt;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getOrStatus() {
            return orStatus;
        }

        public void setOrStatus(Integer orStatus) {
            this.orStatus = orStatus;
        }

        public BigDecimal getFee() {
            return fee;
        }

        public void setFee(BigDecimal fee) {
            this.fee = fee;
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

        public BigDecimal getLon() {
            return lon;
        }

        public void setLon(BigDecimal lon) {
            this.lon = lon;
        }

        public BigDecimal getLat() {
            return lat;
        }

        public void setLat(BigDecimal lat) {
            this.lat = lat;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Boolean getPay() {
            return isPay;
        }

        public void setPay(Boolean isPay) {
            this.isPay = isPay;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getBail() {
            return bail;
        }

        public void setBail(BigDecimal bail) {
            this.bail = bail;
        }

        public Integer getPayWay() {
            return payWay;
        }

        public void setPayWay(Integer payWay) {
            this.payWay = payWay;
        }

        public Boolean getAnonymity() {
            return isAnonymity;
        }

        public void setAnonymity(Boolean isAnonymity) {
            this.isAnonymity = isAnonymity;
        }

        public Boolean getValidationStatus() {
            return validationStatus;
        }

        public void setValidationStatus(Boolean validationStatus) {
            this.validationStatus = validationStatus;
        }

        public Boolean getValidation() {
            return isValidation;
        }

        public void setValidation(Boolean isValidation) {
            this.isValidation = isValidation;
        }

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUserId() {
            return updateUserId;
        }

        public void setUpdateUserId(String updateUserId) {
            this.updateUserId = updateUserId;
        }

        public Integer getDeleted() {
            return deleted;
        }

        public void setDeleted(Integer deleted) {
            this.deleted = deleted;
        }
        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        protected Serializable pkVal() {
            return this.id;
        }

        public Date getSkilCreateTime() {
            return skilCreateTime;
        }

        public Double getReDistance() {
            return reDistance;
        }

        public void setReDistance(Double reDistance) {
            this.reDistance = reDistance;
        }

        public Double getSkDistance() {
            return skDistance;
        }

        public void setSkDistance(Double skDistance) {
            this.skDistance = skDistance;
        }

        public void setSkilCreateTime(Date skilCreateTime) {
            this.skilCreateTime = skilCreateTime;
        }

        public Object getRefund() {
            return refund;
        }

        public void setRefund(Object refund) {
            this.refund = refund;
        }

        @Override
        public String toString() {
            return "SkillRegisterListDto{" +
                    "id='" + id + '\'' +
                    ", userId='" + userId + '\'' +
                    ", skillId='" + skillId + '\'' +
                    ", reDistance=" + reDistance +
                    ", skDistance=" + skDistance +
                    ", skilCreateTime=" + skilCreateTime +
                    ", startAt=" + startAt +
                    ", orStatus=" + orStatus +
                    ", endAt=" + endAt +
                    ", unit='" + unit + '\'' +
                    ", amount=" + amount +
                    ", number=" + number +
                    ", fee=" + fee +
                    ", description='" + description + '\'' +
                    ", address='" + address + '\'' +
                    ", lon=" + lon +
                    ", lat=" + lat +
                    ", status=" + status +
                    ", isPay=" + isPay +
                    ", bail=" + bail +
                    ", payWay=" + payWay +
                    ", isAnonymity=" + isAnonymity +
                    ", code=" + code +
                    ", validationStatus=" + validationStatus +
                    ", isValidation=" + isValidation +
                    ", times=" + times +
                    ", createTime=" + createTime +
                    ", createUserId='" + createUserId + '\'' +
                    ", updateTime=" + updateTime +
                    ", updateUserId='" + updateUserId + '\'' +
                    ", deleted=" + deleted +
                    '}';
        }
    }
