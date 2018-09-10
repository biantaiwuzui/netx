package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Create by wongloong on 17-9-10
 */
@ApiModel
public class SensitiveSuggestAuditRequestDto {
    @NotNull
    @ApiModelProperty("审核ids")
    private List<String> ids;

    @NotBlank
    @ApiModelProperty("审核人id")
    private String auditUserId;

    @NotNull
    @ApiModelProperty("建议删除还是添加 0添加 1删除")
    private Integer delOrSave;

    @ApiModelProperty("通过还是拒绝:  0通过  1拒绝")
    private Integer passOrRefuse;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Integer getDelOrSave() {
        return delOrSave;
    }

    public void setDelOrSave(Integer delOrSave) {
        this.delOrSave = delOrSave;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public Integer getPassOrRefuse() {
        return passOrRefuse;
    }

    public void setPassOrRefuse(Integer passOrRefuse) {
        this.passOrRefuse = passOrRefuse;
    }
}
