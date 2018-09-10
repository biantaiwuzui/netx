package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WishdeaDto {
    @ApiModelProperty(value = "收款者昵称")
    private String nickName;


    @ApiModelProperty(value = "申请金额")
    @NotNull(message = "心愿表ID不能为空")
    @Min(value = 1, message = "金额不能为0")
    private double amount;

    @ApiModelProperty(value = "希望筹集的金额")
    @Min(value = 1, message = "金额不能为0")
    private long amounts;

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "时间")
    private Date createTime;

    @ApiModelProperty(value = "已通过")
    private int ispasso;

    @ApiModelProperty(value = "未通过")
    private int ispassz;

    @ApiModelProperty(value = "待通过")
    private int ispassw;

    @ApiModelProperty(value = "id")
    private String id;


    private List<WishdeaDtotwo> stausList;

    private Integer isPass;

    //凭据
    private String pic;
    //群号
    private Long groupId;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getAmounts() {
        return amounts;
    }

    public void setAmounts(long amounts) {
        this.amounts = amounts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIspasso() {
        return ispasso;
    }

    public void setIspasso(int ispasso) {
        this.ispasso = ispasso;
    }

    public int getIspassz() {
        return ispassz;
    }

    public void setIspassz(int ispassz) {
        this.ispassz = ispassz;
    }

    public int getIspassw() {
        return ispassw;
    }

    public void setIspassw(int ispassw) {
        this.ispassw = ispassw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WishdeaDtotwo> getStausList() {
        return stausList;
    }

    public void setStausList(List<WishdeaDtotwo> stausList) {
        this.stausList = stausList;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "WishdeaDto{" +
                "nickName='" + nickName + '\'' +
                ", amount=" + amount +
                ", amounts=" + amounts +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", ispasso=" + ispasso +
                ", ispassz=" + ispassz +
                ", ispassw=" + ispassw +
                ", id='" + id + '\'' +
                ", stausList=" + stausList +
                ", isPass=" + isPass +
                ", pic='" + pic + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}

