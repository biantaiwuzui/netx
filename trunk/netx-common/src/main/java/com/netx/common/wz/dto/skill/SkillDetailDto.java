package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.Date;

public class SkillDetailDto {


        private static final long serialVersionUID = 1L;

        private String skillId;

        private String userId;
        /**
         * 技能标签，逗号分隔
         */
        private String skill;
        /**
         * 水平标签，逗号分隔
         */
        private String level;
        /**
         * 描述
         */
        private String description;
        /**
         * 发布者网号
         */
        private String userNumber;
        /**
         * 用户昵称
         */
        private String nickname;
        /**
         * 预约单Id
         */
        private String ReId;

        /**
         * 图片
         */
        private String skillImagesUrl;
        /**
         * 图片
         */

        private String skillDetailImagesUrl;
        /**
         * 单位
         */
        private String unit;
        /**
         * 单价
         */
        private BigDecimal amount;
        /**
         * 被预约次数
         */
        private int reCount;

        /**
         * 被预约，已完成次数
         */
        private int coCount;
        /**
         * 价格说明
         */
        private String intr;

        /**
         * 预约者距离
         */
        private Double reDistance;

        /**
         * 技能距离
         */
        private Double skDistance;
        /**
         * 预约对象：
         * 1：不限制。
         * 2：仅限线上交易
         * 3：仅接受附近预约
         * 4：仅限女性预约
         * 5：仅限男性预约
         * 6：仅限好友预约
         */
        private Integer obj;
        /**
         * 经度
         */
        @Range(min = -180, max = 180, message = "经度输入不在范围内")
        private BigDecimal lon;
        /**
         * 纬度
         */
        @Range(min = -90, max = 90, message = "纬度输入不在范围内")
        private BigDecimal lat;
        /**
         * 已预约人数
         */

        private Integer registerCount;
        /**
         * 已成功人数
         */

        private Integer successCount;
        /**
         * 状态：
         * 1：已发布
         * 2：已取消
         * 3：已结束
         */
        private Integer status;

        private Date createTime;

        private String createUserId;

        private Date updateTime;

        private String updateUserId;
        
        @ApiModelProperty(value = "两者间的距离")
        private Double BothDistance;

        @ApiModelProperty(value = "是否可以预约")
        private Boolean isRegister;
        
        @ApiModelProperty(value = "是否匿名")
        private Boolean isAnonymity;


    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Boolean getRegister() {
            return isRegister;
        }
    
        public void setRegister(Boolean register) {
            isRegister = register;
        }
    
        public Boolean getIsRegister() {
            return isRegister;
        }
    
        public void setIsRegister(Boolean isRegister) {
            this.isRegister = isRegister;
        }
    
        public Double getBothDistance() {
            return BothDistance;
        }
    
        public void setBothDistance(Double bothDistance) {
            BothDistance = bothDistance;
        }
    
        public String getUserNumber() {
                return userNumber;
            }
    
        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        private Integer deleted;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSkillId() {
                    return skillId;
                }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getUserId() {
                return userId;
            }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getDescription() {
            return description;
        }

        public int getReCount() {
            return reCount;
        }

        public void setReCount(int reCount) {
            this.reCount = reCount;
        }

        public int getCoCount() {
            return coCount;
        }

        public void setCoCount(int coCount) {
            this.coCount = coCount;
        }

    public void setDescription(String description) {
            this.description = description;
        }

        public String getSkillImagesUrl() {
            return skillImagesUrl;
        }

        public void setSkillImagesUrl(String skillImagesUrl) {
            this.skillImagesUrl = skillImagesUrl;
        }

        public String getSkillDetailImagesUrl() {
            return skillDetailImagesUrl;
        }

        public void setSkillDetailImagesUrl(String skillDetailImagesUrl) {
            this.skillDetailImagesUrl = skillDetailImagesUrl;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getIntr() {
                    return intr;
                }

        public void setIntr(String intr) {
            this.intr = intr;
        }

        public Integer getObj() {
            return obj;
        }

        public void setObj(Integer obj) {
            this.obj = obj;
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

        public Integer getRegisterCount() {
            return registerCount;
        }

        public void setRegisterCount(Integer registerCount) {
            this.registerCount = registerCount;
        }

        public Integer getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(Integer successCount) {
            this.successCount = successCount;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
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

        public String getReId() {
            return ReId;
        }

        public void setReId(String reId) {
            ReId = reId;
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


    @Override
    public String toString() {
        return "SkillDetailDto{" +
                "skillId='" + skillId + '\'' +
                ", userId='" + userId + '\'' +
                ", skill='" + skill + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", ReId='" + ReId + '\'' +
                ", skillImagesUrl='" + skillImagesUrl + '\'' +
                ", skillDetailImagesUrl='" + skillDetailImagesUrl + '\'' +
                ", unit='" + unit + '\'' +
                ", amount=" + amount +
                ", reCount=" + reCount +
                ", coCount=" + coCount +
                ", intr='" + intr + '\'' +
                ", reDistance=" + reDistance +
                ", skDistance=" + skDistance +
                ", obj=" + obj +
                ", lon=" + lon +
                ", lat=" + lat +
                ", registerCount=" + registerCount +
                ", successCount=" + successCount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", BothDistance=" + BothDistance +
                ", isRegister=" + isRegister +
                ", isAnonymity=" + isAnonymity +
                ", deleted=" + deleted +
                '}';
    }
}
