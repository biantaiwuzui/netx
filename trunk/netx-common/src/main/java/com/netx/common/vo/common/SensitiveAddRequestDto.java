package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-10
 */
@ApiModel
public class SensitiveAddRequestDto {

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty(value = "建议者id", required = true)
    @NotBlank(message = "建议者ID不能为空")
    private String suggestUserId;

    @ApiModelProperty(value = "建议者昵称", required = true)
    @NotBlank(message = "建议者昵称不能为空")
    private String suggestUserName;

    @ApiModelProperty("删除的理由,建议添加的时候可以不用这个参数,删除就要这个参数")
    private String delReason;

    @ApiModelProperty(value = "过滤词，多个用,号隔开(英文逗号)", required = true)
    @NotBlank(message = "过滤词不能为空")
    private String value;

    @ApiModelProperty(value = "建议新增:0,建议删除1", required = true)
    @NotNull(message = "建议新增:0,建议删除1 不能为空")
    private Integer delOrSave;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSuggestUserId() {
        return suggestUserId;
    }

    public void setSuggestUserId(String suggestUserId) {
        this.suggestUserId = suggestUserId;
    }

    public String getSuggestUserName() {
        return suggestUserName;
    }

    public void setSuggestUserName(String suggestUserName) {
        this.suggestUserName = suggestUserName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDelOrSave() {
        return delOrSave;
    }

    public void setDelOrSave(Integer delOrSave) {
        this.delOrSave = delOrSave;
    }

    public String getDelReason() { return delReason; }

    public void setDelReason(String delReason) { this.delReason = delReason; }
}
