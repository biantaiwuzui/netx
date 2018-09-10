package com.netx.shopping.vo;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Created By wj.liu
 * Description: 网商主管列表返回结果
 * Date: 2017-09-07
 */
public class BusinessManageListResponseVo {

    /**
     * 主管id
     */
    private String id;
    /**
     * 姓名
     */
    private String manageName;

    /**
     * 手机号
     */
    private String managePhone;

    /**
     * 网号
     */
    private String manageNetworkNum;

    /**
     * 商家id
     */
    @TableField("seller_id")
    private String sellerId;
    /**
     * 是否当前商家适用中： 0：不是   1：是
     */
    @TableField("is_current")
    private Integer isCurrent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }
}
