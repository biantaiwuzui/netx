package com.netx.common.router.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserInfoListRequestDto extends UserInfoBaseRequestDto {

    @NotNull(message = "查询条件数据列表不能为空")
    @ApiModelProperty("查询条件数据列表")
    private List<String> selectDataList;

    public List<String> getSelectDataList() {
        return selectDataList;
    }

    public void setSelectDataList(List<String> selectDataList) {
        this.selectDataList = selectDataList;
    }

}
