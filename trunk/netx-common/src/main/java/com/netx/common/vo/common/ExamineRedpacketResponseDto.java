package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ExamineRedpacketResponseDto {
    @ApiModelProperty(required = true, name = "审核人id")
    @NotBlank
    private String examineUserId;

    @ApiModelProperty(required = true, name = "设置id数组")
    private List<String> ids;

    @ApiModelProperty(required = true, name = "审核意见", notes = "0:拒绝,1通过")
    @NotNull
    private Integer status;


    public String getExamineUserId() {
        return examineUserId;
    }

    public void setExamineUserId(String examineUserId) {
        this.examineUserId = examineUserId;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
