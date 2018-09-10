package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 网信法人代表点击审批网信的弹框Vo类
 */
@ApiModel
public class ReviewApprovePopUpResponseDto {

    @ApiModelProperty("法人代表对应的商家名称")
    private List<String> sellersName;

    @ApiModelProperty("结转用户网号")
    private String importNetNum;

    @ApiModelProperty("结转用户姓名")
    private String importName;

    @ApiModelProperty("结转用户手机号")
    private String importPhone;

    @ApiModelProperty("结转用户身份证")
    private String importIdCard;

    @ApiModelProperty("收银人员用户id")
    private String cashierUserId;

    @ApiModelProperty("法人代表审核剩余时间")
    private Integer releaseTime;

    public List<String> getSellersName() { return sellersName; }

    public void setSellersName(List<String> sellersName) { this.sellersName = sellersName; }

    public String getImportNetNum() { return importNetNum; }

    public void setImportNetNum(String importNetNum) { this.importNetNum = importNetNum; }

    public String getImportName() { return importName; }

    public void setImportName(String importName) { this.importName = importName; }

    public String getImportPhone() { return importPhone; }

    public void setImportPhone(String importPhone) { this.importPhone = importPhone; }

    public String getImportIdCard() { return importIdCard; }

    public void setImportIdCard(String importIdCard) { this.importIdCard = importIdCard; }

    public String getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(String cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public Integer getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Integer releaseTime) {
        this.releaseTime = releaseTime;
    }
}
