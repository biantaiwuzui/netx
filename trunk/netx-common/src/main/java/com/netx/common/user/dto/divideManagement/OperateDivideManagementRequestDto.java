package com.netx.common.user.dto.divideManagement;

import com.netx.common.user.enums.UserRoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 李卓
 */
@ApiModel("操作分工管理（新增或调整分工权限）")
public class OperateDivideManagementRequestDto {

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @NotNull(message = "角色列表不能为空")
    @ApiModelProperty("角色列表，值如下：\n" +
            "null：表示暂停所有权限\n"+
            "SYSTEM_MANAGEMENT：系统管理\n" +
            "USER_MANAGEMENT：用户管理\n" +
            "BUSINESS_MANAGEMENT：商家管理\n" +
            "MESSAGE_MANAGEMENT：资讯管理\n" +
            "FINANCE_MANAGEMENT：财务管理\n" +
            "ARBITRATION_MANAGEMENT：仲裁管理\n")
    private List<UserRoleEnum> list;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserRoleEnum> getList() {
        return list;
    }

    public void setList(List<UserRoleEnum> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OperateDivideManagementDto{" +
                "userId='" + userId + '\'' +
                ", list=" + list +
                '}';
    }
}
