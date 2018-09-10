package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 添加或修改网商主管请求参数
 * Date: 2017-09-07
 */
@ApiModel
public class AddBusinessManageRequestDto {

    @ApiModelProperty("主管id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String manageName;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String managePhone;

    @ApiModelProperty("网号")
    @NotBlank(message = "网号不能为空")
    private String manageNetworkNum;

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

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getManagePhone() {
        return managePhone;
    }

    public void setManagePhone(String managePhone) {
        this.managePhone = managePhone;
    }

    public String getManageNetworkNum() {
        return manageNetworkNum;
    }

    public void setManageNetworkNum(String manageNetworkNum) {
        this.manageNetworkNum = manageNetworkNum;
    }
}
