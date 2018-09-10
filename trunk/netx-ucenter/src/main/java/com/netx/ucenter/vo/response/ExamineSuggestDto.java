package com.netx.ucenter.vo.response;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ExamineSuggestDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("状态：\n" +
            "     0.无效 \n" +
            "     1.有效 \n" +
            "     2.搁置 \n" +
            "     null.未审核")
    private Integer effective;

    @ApiModelProperty("建议")
    private String result;

    @ApiModelProperty("审核人名字")
    private String auditUserName;

    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public Integer getEffective() {
        return effective;
    }

    public void setEffective(Integer effective) {
        this.effective = effective;
    }

    @Override
    public String toString() {
        return "ExamineSuggestDto{" +
                "userId='" + userId + '\'' +
                ", id='" + id + '\'' +
                ", isEffective=" + effective +
                ", result='" + result + '\'' +
                ", auditUserId='" + auditUserName + '\'' +
                '}';
    }
}
