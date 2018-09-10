package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 跨模块网币用户信息统计数量Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectCurrencyUserDataDto {
    @ApiModelProperty("活动数")
    private int activityNum;

    @ApiModelProperty("技能数")
    private int skillNum;

    @ApiModelProperty("商家数")
    private int shopNum;

    @ApiModelProperty("商品数")
    private int goodsNum;

    @ApiModelProperty("图文数")
    private int imageWriteNum;

    @ApiModelProperty("音频数")
    private int vedioNum;

    public SelectCurrencyUserDataDto() {
        activityNum=skillNum=shopNum=goodsNum=imageWriteNum=vedioNum=0;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public int getSkillNum() {
        return skillNum;
    }

    public void setSkillNum(int skillNum) {
        this.skillNum = skillNum;
    }

    public int getShopNum() {
        return shopNum;
    }

    public void setShopNum(int shopNum) {
        this.shopNum = shopNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getImageWriteNum() {
        return imageWriteNum;
    }

    public void setImageWriteNum(int imageWriteNum) {
        this.imageWriteNum = imageWriteNum;
    }

    public int getVedioNum() {
        return vedioNum;
    }

    public void setVedioNum(int vedioNum) {
        this.vedioNum = vedioNum;
    }
}
