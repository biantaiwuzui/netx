package com.netx.common.router.dto.select;

import com.netx.common.router.dto.bean.UserBeanData;
import com.netx.common.vo.business.GetNewestGoodsMessageResponseVo;
import com.netx.common.vo.business.GetRegisterSellerMessageResponseVo;
import com.netx.common.vo.business.GetRelatedGoodsMessageResponseVo;
import com.netx.common.vo.business.GetSellerByUserIdVo;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 个人详情Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectOtherUserDataResponseDto {
    @ApiModelProperty("用户信息")
    private UserBeanData userBeanData;

    @ApiModelProperty("网币最新记录(发行)")
    private SelectCurrencyDetailDataResponseDto currencyNewlyNoteIssue;

    @ApiModelProperty("注册商家数")
    private GetRegisterSellerMessageResponseVo getRegisterSellerMessageResponseVo;

    @ApiModelProperty("商家详情最新记录(注册)")
    private GetSellerByUserIdVo getSellerDetailRegister;

    @ApiModelProperty("发行商品数量")
    private GetRelatedGoodsMessageResponseVo relatedGoodsMessageResponseVo;

    @ApiModelProperty("商品详情最新记录")
    private GetNewestGoodsMessageResponseVo newestGoodsMessageResponseVo;

    @ApiModelProperty("发行的心愿数量统计")
    private SelectNumResponseDto numUserWishPublish;

    @ApiModelProperty("最新发行的心愿")
    private SelectWishDetailDataResponseDto wishDetailDataResponseDtoPublish;

    @ApiModelProperty("提供的技能数量统计")
    private SelectNumResponseDto numUserSkillProvide;

    @ApiModelProperty("最新提供的技能")
    private SelectUserSkillResponse skillResponseProvide;

    @ApiModelProperty("发布的需求数量统计")
    private SelectNumResponseDto numUserDemandPublish;

    @ApiModelProperty("最新发布的需求")
    private SelectUserDemandResponseDto demandResponseDtoPublish;

    @ApiModelProperty("发布的活动数量统计")
    private SelectNumResponseDto numUserMeetingPublish;

    @ApiModelProperty("最新发布的活动")
    private SelectUserMeetingResponseDto meetingResponseDtoPublish;

    public UserBeanData getUserBeanData() {
        return userBeanData;
    }

    public void setUserBeanData(UserBeanData userBeanData) {
        this.userBeanData = userBeanData;
    }

    public SelectCurrencyDetailDataResponseDto getCurrencyNewlyNoteIssue() {
        return currencyNewlyNoteIssue;
    }

    public void setCurrencyNewlyNoteIssue(SelectCurrencyDetailDataResponseDto currencyNewlyNoteIssue) {
        this.currencyNewlyNoteIssue = currencyNewlyNoteIssue;
    }

    public GetRegisterSellerMessageResponseVo getGetRegisterSellerMessageResponseVo() {
        return getRegisterSellerMessageResponseVo;
    }

    public void setGetRegisterSellerMessageResponseVo(GetRegisterSellerMessageResponseVo getRegisterSellerMessageResponseVo) {
        this.getRegisterSellerMessageResponseVo = getRegisterSellerMessageResponseVo;
    }

    public GetSellerByUserIdVo getGetSellerDetailRegister() {
        return getSellerDetailRegister;
    }

    public void setGetSellerDetailRegister(GetSellerByUserIdVo getSellerDetailRegister) {
        this.getSellerDetailRegister = getSellerDetailRegister;
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

    public SelectWishDetailDataResponseDto getWishDetailDataResponseDtoPublish() {
        return wishDetailDataResponseDtoPublish;
    }

    public void setWishDetailDataResponseDtoPublish(SelectWishDetailDataResponseDto wishDetailDataResponseDtoPublish) {
        this.wishDetailDataResponseDtoPublish = wishDetailDataResponseDtoPublish;
    }

    public SelectNumResponseDto getNumUserSkillProvide() {
        return numUserSkillProvide;
    }

    public void setNumUserSkillProvide(SelectNumResponseDto numUserSkillProvide) {
        this.numUserSkillProvide = numUserSkillProvide;
    }

    public SelectUserSkillResponse getSkillResponseProvide() {
        return skillResponseProvide;
    }

    public void setSkillResponseProvide(SelectUserSkillResponse skillResponseProvide) {
        this.skillResponseProvide = skillResponseProvide;
    }

    public SelectUserDemandResponseDto getDemandResponseDtoPublish() {
        return demandResponseDtoPublish;
    }

    public void setDemandResponseDtoPublish(SelectUserDemandResponseDto demandResponseDtoPublish) {
        this.demandResponseDtoPublish = demandResponseDtoPublish;
    }

    public SelectNumResponseDto getNumUserWishPublish() {
        return numUserWishPublish;
    }

    public void setNumUserWishPublish(SelectNumResponseDto numUserWishPublish) {
        this.numUserWishPublish = numUserWishPublish;
    }

    public SelectNumResponseDto getNumUserDemandPublish() {
        return numUserDemandPublish;
    }

    public void setNumUserDemandPublish(SelectNumResponseDto numUserDemandPublish) {
        this.numUserDemandPublish = numUserDemandPublish;
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
}
