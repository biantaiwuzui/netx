package com.netx.common.router.dto.request;

import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class UserInfoRequestDto extends UserInfoBaseRequestDto {

    @NotBlank(message = "查询条件数据不能为空")
    @ApiModelProperty("查询条件数据")
    private String selectData;

    public String getSelectData() {
        return selectData;
    }

    public void setSelectData(String selectData) {
        this.selectData = selectData;
    }

    public UserInfoRequestDto() {}

    public UserInfoRequestDto(String selectData, SelectConditionEnum selectConditionEnum, List<SelectFieldEnum> selectFieldEnumList) {
        this.selectData = selectData;
        super.setSelectConditionEnum(selectConditionEnum);
        super.setSelectFieldEnumList(selectFieldEnumList);
    }
}
