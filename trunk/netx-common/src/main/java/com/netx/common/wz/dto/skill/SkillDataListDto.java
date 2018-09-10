package com.netx.common.wz.dto.skill;

import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预约单数据
 * */

public class SkillDataListDto {

        /**
         * 技能ID
         */
        private String id;

        /**
         * 单价
         */
        private BigDecimal amount;

        /**
         * 发布者网号
         * */
        private String userNumber;

        /**
         * 昵称
         * */
        private String nickname;

        /**
         * 头像
         * */
        private String headImgUrl;

        /**
         * 用户-技能距离
         */
        private Double wdistance;

        /**
         * 发布者性别
         */
        private String sex;

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
         * 价格说明
         */
        private String intr;
        /**
         * 预约对象：
         1：不限制。
         2：仅限线上交易
         3：仅接受附近预约
         4：仅限女性预约
         5：仅限男性预约
         6：仅限好友预约
         */
        private Integer obj;
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
         * 已预约人数
         */

        private Integer registerCount;
        /**
         * 已成功人数
         */

        private Integer successCount;
        /**
         * 状态：
         1：已发布
         2：已取消
         3：已结束
         */
        private Integer status;

        private Date createTime;

        private String createUserId;

        private Date updateTime;

        private String updateUserId;

        private Integer deleted;

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

        public String getId() {
                return id;
            }

        public void setId(String id) {
            this.id = id;
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

        public Integer getCredit() {
            return credit;
        }

        public void setCredit(Integer credit) {
            this.credit = credit;
        }

        public Integer getLv() {
            return lv;
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

        public Double getWdistance() {
            return wdistance;
        }

        public void setWdistance(Double wdistance) {
            this.wdistance = wdistance;
        }


}
