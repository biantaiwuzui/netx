package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("用户兴趣爱好信息")
public class InsertUserInterestDetailRequest {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "兴趣类别不能为空")
    @ApiModelProperty("兴趣类别")
    private String interestType;

    @NotBlank(message = "具体内容不能为空")
    @ApiModelProperty("具体内容")
    private String interestDetail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestDetail() {
        return interestDetail;
    }

    public void setInterestDetail(String interestDetail) {
        this.interestDetail = interestDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "InsertUserInterestDetailRequest{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", interestType='" + interestType + '\'' + ", interestDetail='" + interestDetail + '\'' + '}';
    }
}
