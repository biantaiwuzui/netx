package com.netx.ucenter.vo.response;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AddSuggestPassDto {

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("建议")
    @NotBlank(message = "建议不能为空")
    private String suggest;

    @ApiModelProperty("管理员通过描述,可以为空")
    private String result;

    @ApiModelProperty("状态")
    private Integer effective;

    @ApiModelProperty("审核人名字")
    private String auditUserName;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getEffective() {
        return effective;
    }

    public void setEffective(Integer effective) {
        this.effective = effective;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
