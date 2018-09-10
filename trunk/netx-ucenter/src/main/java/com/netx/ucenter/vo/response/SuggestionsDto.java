package com.netx.ucenter.vo.response;

import javax.xml.crypto.Data;
import java.util.Date;

public class SuggestionsDto {

    /**
     * User表找
     */

    /**
     * 用户网号
     */
    private String userNumber;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 信用
     */
    private Integer credit;
    /**
     * UserSuggest表找
     */
    /**
     * 建议
     */
    private String suggest;
    /**
     * 是否有效(状态)
     */
    private Integer isEffective;

    /**
     * 用户id
     */

    private String userId;

    /**
     * 审批结果
     */
    private String result;

    /**
     * 审批人id
     */
    private String auditUserId;

    /**
     * 审批人名字
     */
    private String auditUserName;

    /**
     * UserSuggest表id
     */
    private String id;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date createTime;

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }


    @Override
    public String toString() {
        return "SuggestionsDto{" +
                "userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", credit=" + credit +
                ", suggest='" + suggest + '\'' +
                ", isEffective=" + isEffective +
                ", userId='" + userId + '\'' +
                ", result='" + result + '\'' +
                ", auditUserId='" + auditUserId + '\'' +
                ", auditUserName='" + auditUserName + '\'' +
                ", id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
