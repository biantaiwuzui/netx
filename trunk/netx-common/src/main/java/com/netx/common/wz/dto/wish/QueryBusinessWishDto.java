package com.netx.common.wz.dto.wish;
import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

public class QueryBusinessWishDto extends CommonListDto {
    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("状态：\n" +
            "     0.待提现 \n" +
            "     1.提现成功 \n" +
            "     2.提现失败")
    private Integer status;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QueryBusinessWishDto{" +
                "userNumber='" + userNumber + '\'' +
                ", status=" + status +
                '}';
    }
}
