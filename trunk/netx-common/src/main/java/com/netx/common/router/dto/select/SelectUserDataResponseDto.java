package com.netx.common.router.dto.select;

import com.netx.common.router.dto.bean.UserBeanData;
import com.netx.common.vo.business.*;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 个人中心Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectUserDataResponseDto {
    @ApiModelProperty("用户信息")
    private UserBeanData userBeanData;

    @ApiModelProperty("网币最新记录(持有)")
    private SelectCurrencyDetailDataResponseDto currencyNewlyNoteHold;

    @ApiModelProperty("网币最新记录(发行)")
    private SelectCurrencyDetailDataResponseDto currencyNewlyNoteIssue;

    @ApiModelProperty("商家统计数据")
    private GetRelatedSellerMessageResponseVo getRelatedSellerMessageResponseVo;

    @ApiModelProperty("商家详情最新记录(经营)")
    private GetSellerByUserIdVo getSellerDetailManagement;

    @ApiModelProperty("商家详情最新记录(收藏)")
    private GetSellerByUserIdVo getSellerDetailCollect;

    @ApiModelProperty("发行商品数量")
    private GetRelatedGoodsMessageResponseVo relatedGoodsMessageResponseVo;

    @ApiModelProperty("商品详情最新记录")
    private GetNewestGoodsMessageResponseVo newestGoodsMessageResponseVo;

    @ApiModelProperty("订单数")
    private GetRelatedOdersMessageResponseVo relatedOdersMessageResponseVo;

    @ApiModelProperty("订单详情")
    private GetNewestOdersMessageResponseVo newestOdersMessageResponseVo;

    @ApiModelProperty("发行的心愿数量统计")
    private SelectNumResponseDto numUserWishPublish;

    @ApiModelProperty("最新发行的心愿")
    private SelectWishDetailDataResponseDto wishDetailDataResponseDtoPublish;

    @ApiModelProperty("支持的心愿数量统计")
    private SelectNumResponseDto numUserWishSupport;

    @ApiModelProperty("最新支持的心愿")
    private SelectWishDetailDataResponseDto wishDetailDataResponseDtoSupport;

    @ApiModelProperty("提供的技能数量统计")
    private SelectNumResponseDto numUserSkillProvide;

    @ApiModelProperty("最新提供的技能")
    private SelectUserSkillResponse skillResponseProvide;

    @ApiModelProperty("预约的技能数量统计")
    private SelectNumResponseDto numUserSkillOrder;

    @ApiModelProperty("最新预约的技能")
    private SelectUserSkillResponse skillResponseOrder;

    @ApiModelProperty("发布的需求数量统计")
    private SelectNumResponseDto numUserDemandPublish;

    @ApiModelProperty("最新发布的需求")
    private SelectUserDemandResponseDto demandResponseDtoPublish;

    @ApiModelProperty("申请的需求数量统计")
    private SelectNumResponseDto numUserDemandRegister;

    @ApiModelProperty("最新申请的需求")
    private SelectUserDemandResponseDto demandResponseDtoRegister;

    @ApiModelProperty("发布的活动数量统计")
    private SelectNumResponseDto numUserMeetingPublish;

    @ApiModelProperty("最新发布的活动")
    private SelectUserMeetingResponseDto meetingResponseDtoPublish;

    @ApiModelProperty("报名的活动数量统计")
    private SelectNumResponseDto numUserMeetingReg;

    @ApiModelProperty("最新报名的活动")
    private SelectUserMeetingResponseDto meetingResponseDtoReg;

    public UserBeanData getUserBeanData() {
        return userBeanData;
    }

    public void setUserBeanData(UserBeanData userBeanData) {
        this.userBeanData = userBeanData;
    }

    public SelectCurrencyDetailDataResponseDto getCurrencyNewlyNoteHold() {
        return currencyNewlyNoteHold;
    }

    public void setCurrencyNewlyNoteHold(SelectCurrencyDetailDataResponseDto currencyNewlyNoteHold) {
        this.currencyNewlyNoteHold = currencyNewlyNoteHold;
    }

    public SelectCurrencyDetailDataResponseDto getCurrencyNewlyNoteIssue() {
        return currencyNewlyNoteIssue;
    }

    public void setCurrencyNewlyNoteIssue(SelectCurrencyDetailDataResponseDto currencyNewlyNoteIssue) {
        this.currencyNewlyNoteIssue = currencyNewlyNoteIssue;
    }

    public GetRelatedSellerMessageResponseVo getGetRelatedSellerMessageResponseVo() {
        return getRelatedSellerMessageResponseVo;
    }

    public void setGetRelatedSellerMessageResponseVo(GetRelatedSellerMessageResponseVo getRelatedSellerMessageResponseVo) {
        this.getRelatedSellerMessageResponseVo = getRelatedSellerMessageResponseVo;
    }

    public GetSellerByUserIdVo getGetSellerDetailManagement() {
        return getSellerDetailManagement;
    }

    public void setGetSellerDetailManagement(GetSellerByUserIdVo getSellerDetailManagement) {
        this.getSellerDetailManagement = getSellerDetailManagement;
    }

    public GetSellerByUserIdVo getGetSellerDetailCollect() {
        return getSellerDetailCollect;
    }

    public void setGetSellerDetailCollect(GetSellerByUserIdVo getSellerDetailCollect) {
        this.getSellerDetailCollect = getSellerDetailCollect;
    }

    public GetNewestGoodsMessageResponseVo getNewestGoodsMessageResponseVo() {
        return newestGoodsMessageResponseVo;
    }

    public void setNewestGoodsMessageResponseVo(GetNewestGoodsMessageResponseVo newestGoodsMessageResponseVo) {
        this.newestGoodsMessageResponseVo = newestGoodsMessageResponseVo;
    }

    public GetRelatedGoodsMessageResponseVo getRelatedGoodsMessageResponseVo() {
        return relatedGoodsMessageResponseVo;
    }

    public void setRelatedGoodsMessageResponseVo(GetRelatedGoodsMessageResponseVo relatedGoodsMessageResponseVo) {
        this.relatedGoodsMessageResponseVo = relatedGoodsMessageResponseVo;
    }

    public GetRelatedOdersMessageResponseVo getRelatedOdersMessageResponseVo() {
        return relatedOdersMessageResponseVo;
    }

    public void setRelatedOdersMessageResponseVo(GetRelatedOdersMessageResponseVo relatedOdersMessageResponseVo) {
        this.relatedOdersMessageResponseVo = relatedOdersMessageResponseVo;
    }

    public GetNewestOdersMessageResponseVo getNewestOdersMessageResponseVo() {
        return newestOdersMessageResponseVo;
    }

    public void setNewestOdersMessageResponseVo(GetNewestOdersMessageResponseVo newestOdersMessageResponseVo) {
        this.newestOdersMessageResponseVo = newestOdersMessageResponseVo;
    }

    public SelectWishDetailDataResponseDto getWishDetailDataResponseDtoPublish() {
        return wishDetailDataResponseDtoPublish;
    }

    public void setWishDetailDataResponseDtoPublish(SelectWishDetailDataResponseDto wishDetailDataResponseDtoPublish) {
        this.wishDetailDataResponseDtoPublish = wishDetailDataResponseDtoPublish;
    }

    public SelectWishDetailDataResponseDto getWishDetailDataResponseDtoSupport() {
        return wishDetailDataResponseDtoSupport;
    }

    public void setWishDetailDataResponseDtoSupport(SelectWishDetailDataResponseDto wishDetailDataResponseDtoSupport) {
        this.wishDetailDataResponseDtoSupport = wishDetailDataResponseDtoSupport;
    }

    public SelectUserSkillResponse getSkillResponseProvide() {
        return skillResponseProvide;
    }

    public void setSkillResponseProvide(SelectUserSkillResponse skillResponseProvide) {
        this.skillResponseProvide = skillResponseProvide;
    }

    public SelectUserSkillResponse getSkillResponseOrder() {
        return skillResponseOrder;
    }

    public void setSkillResponseOrder(SelectUserSkillResponse skillResponseOrder) {
        this.skillResponseOrder = skillResponseOrder;
    }

    public SelectNumResponseDto getNumUserSkillProvide() {
        return numUserSkillProvide;
    }

    public void setNumUserSkillProvide(SelectNumResponseDto numUserSkillProvide) {
        this.numUserSkillProvide = numUserSkillProvide;
    }

    public SelectNumResponseDto getNumUserSkillOrder() {
        return numUserSkillOrder;
    }

    public void setNumUserSkillOrder(SelectNumResponseDto numUserSkillOrder) {
        this.numUserSkillOrder = numUserSkillOrder;
    }

    public SelectUserDemandResponseDto getDemandResponseDtoPublish() {
        return demandResponseDtoPublish;
    }

    public void setDemandResponseDtoPublish(SelectUserDemandResponseDto demandResponseDtoPublish) {
        this.demandResponseDtoPublish = demandResponseDtoPublish;
    }

    public SelectUserDemandResponseDto getDemandResponseDtoRegister() {
        return demandResponseDtoRegister;
    }

    public void setDemandResponseDtoRegister(SelectUserDemandResponseDto demandResponseDtoRegister) {
        this.demandResponseDtoRegister = demandResponseDtoRegister;
    }

    public SelectNumResponseDto getNumUserWishPublish() {
        return numUserWishPublish;
    }

    public void setNumUserWishPublish(SelectNumResponseDto numUserWishPublish) {
        this.numUserWishPublish = numUserWishPublish;
    }

    public SelectNumResponseDto getNumUserWishSupport() {
        return numUserWishSupport;
    }

    public void setNumUserWishSupport(SelectNumResponseDto numUserWishSupport) {
        this.numUserWishSupport = numUserWishSupport;
    }

    public SelectNumResponseDto getNumUserDemandPublish() {
        return numUserDemandPublish;
    }

    public void setNumUserDemandPublish(SelectNumResponseDto numUserDemandPublish) {
        this.numUserDemandPublish = numUserDemandPublish;
    }

    public SelectNumResponseDto getNumUserDemandRegister() {
        return numUserDemandRegister;
    }

    public void setNumUserDemandRegister(SelectNumResponseDto numUserDemandRegister) {
        this.numUserDemandRegister = numUserDemandRegister;
    }

    public SelectNumResponseDto getNumUserMeetingPublish() {
        return numUserMeetingPublish;
    }

    public void setNumUserMeetingPublish(SelectNumResponseDto numUserMeetingPublish) {
        this.numUserMeetingPublish = numUserMeetingPublish;
    }

    public SelectUserMeetingResponseDto getMeetingResponseDtoPublish() {
        return meetingResponseDtoPublish;
    }

    public void setMeetingResponseDtoPublish(SelectUserMeetingResponseDto meetingResponseDtoPublish) {
        this.meetingResponseDtoPublish = meetingResponseDtoPublish;
    }

    public SelectNumResponseDto getNumUserMeetingReg() {
        return numUserMeetingReg;
    }

    public void setNumUserMeetingReg(SelectNumResponseDto numUserMeetingReg) {
        this.numUserMeetingReg = numUserMeetingReg;
    }

    public SelectUserMeetingResponseDto getMeetingResponseDtoReg() {
        return meetingResponseDtoReg;
    }

    public void setMeetingResponseDtoReg(SelectUserMeetingResponseDto meetingResponseDtoReg) {
        this.meetingResponseDtoReg = meetingResponseDtoReg;
    }
}
