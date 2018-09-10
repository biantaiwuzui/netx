package com.netx.fuse.biz.credit;

import com.alibaba.fastjson.JSON;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.credit.model.*;
import com.netx.credit.model.constants.CreditSubscriptionTypeEnum;
import com.netx.credit.service.*;
import com.netx.credit.utils.JsonUtils;
import com.netx.credit.vo.*;
import com.netx.shopping.biz.merchantcenter.MerchantPictureAction;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.service.merchantcenter.MerchantCategoryService;
import com.netx.shopping.service.merchantcenter.MerchantPictureService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.ArticleService;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserVerifyCreditService;
import com.netx.worth.service.WorthServiceprovider;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CreditDetailFuseAction {

    @Autowired
    private CreditService creditService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private CreditSubscriptionService creditSubscriptionService;
    @Autowired
    private CreditCategoryService creditCategoryService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MerchantCategoryService merchantCategoryService;
    @Autowired
    private MerchantPictureService merchantPictureService;
    @Autowired
    private UserPhotoAction userPhotoAction;
    @Autowired
    private CreditCashierService creditCashierService;
    @Autowired
    private UserVerifyCreditService userVerifyCreditService;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private MerchantPictureAction merchantPictureAction;

    /* 网信详情 **/
    public CreditDetailDto getCreditDetail(String creditId, String userId){

        CreditDetailDto creditDetailDto = new CreditDetailDto();
        /* 获取网信数据 **/
        Credit credit = creditService.selectById(creditId);
        if(credit == null || credit.getDeleted() == 1){
            return null;
        }
        /* 判断用户身份  0.发布者 1.商家内购人员或内购好友 2.用户好友 3.普通用户*/
        if (userId.equals(credit.getUserId())) {
            creditDetailDto.setUserType(0);
        } else if (creditSubscriptionService.selectUserType(creditId, userId) != null) {
            creditDetailDto.setUserType(1);
        } else if (friendsService.checkFriend(credit.getUserId(), userId)) {
            creditDetailDto.setUserType(2);
        } else {
            creditDetailDto.setUserType(3);
        }
        VoPoConverter.copyProperties(credit,creditDetailDto);
        // 金额单位转化
        creditDetailDto.setCreditPrice(credit.getCreditPrice()/100);
        creditDetailDto.setPresaleUpperLimit(credit.getPresaleUpperLimit()/100);
        creditDetailDto.setEnsureDeal(credit.getEnsureDeal()/100);
        /* 获取用户发起的活动数量 **/
        creditDetailDto.setMeetingNumber(worthServiceprovider.getMeetingService().getSendCount(credit.getUserId()));
        /* 获取用户发布的技能数量 **/
        creditDetailDto.setDemandNumber(worthServiceprovider.getDemandService().getDemandCountByUserId(credit.getUserId()));
        /* 获取用户的商家数量 **/
        creditDetailDto.setSkillNumber(worthServiceprovider.getSkillService().getSkillCountByUserId(credit.getUserId()));
        /*获取用户的图文数量*/
        creditDetailDto.setMerchantNumber(merchantService.getSumCountByUserId(credit.getUserId()));
        /*获取用户的音视数量*/
        creditDetailDto.setGraphicNumber(articleService.getArticleCountByUserId(credit.getUserId()));
        /* 获取申购折扣详情 */
        creditDetailDto.setCreditLevelDiscountSettings(JsonUtils.jsonToList(credit.getSubscriptionDiscount(), CreditLevelDiscountSetting.class));
        /* 获取网信分红详情 */
        creditDetailDto.setCreditDividendSettings(JsonUtils.jsonToList(credit.getDividendSetting(), CreditDividendSetting.class));
        /* 获取适用范围详情 */
        creditDetailDto.setCreditScope(JsonUtils.jsonToPojo(credit.getScope(), CreditScope.class));
        /* 获取已售份额(转换单位) */
        creditDetailDto.setSellAmount(creditSubscriptionService.countCreditMoney(creditId) /100);
        /* 获取发售金额 */
        creditDetailDto.setTotalNumber(credit.getCreditPrice() * credit.getIssueNumber() /100);
        /* 获取内购期限时间戳 */
        creditDetailDto.setInnerPurchaseDate(credit.getInnerPurchaseDate().getTime());
        /* 获取 类别 - 标签 */
        List<String> categoryIds = creditCategoryService.getCategoryIdsByCreditId(creditId);
        creditDetailDto.setCategoryId(categoryService.getParentCategoryName(categoryIds));
        creditDetailDto.setTagIds(categoryService.getKidCategoryName(categoryIds));
        creditDetailDto.setCreditCategoryVos(this.getCreditCategory(creditId));
        // 新 * 获取受邀好友列表
        creditDetailDto.setCreditInviteFriendsVos(this.getInviteFriendsDetail(creditId));
        // 新 * 获取受邀商家内购详情
        creditDetailDto.setCreditInnerDetailVos(this.getInnerMerchantDetial(creditId));
        // 获取发布者信息
        creditDetailDto.setCreditPublisherInformationDto(this.getCreditPublisherInfo(credit.getUserId()));
        // 回滚到编辑网信备用数据
        creditDetailDto.setPublishCreditRequestDto(this.getEditCreditInfo(creditDetailDto, credit.getCashierId()));
        // 持有人列表(开放认购后的网信详情用)
        creditDetailDto.setCreditHolderVos(this.getHolder(creditId));
        //用户认购状态
        creditDetailDto.setUserCreditStatus(creditSubscriptionService.selectUserStatus(creditId,userId));
        return creditDetailDto;
    }

    /**
     * 获取受邀内购好友认购详情
     */
    private List<CreditInviteFriendsVo> getInviteFriendsDetail(String creditId) {
        List<CreditSubscription> creditSubscriptionList = creditSubscriptionService.getInnerFriendIdByCreditId(creditId, CreditSubscriptionTypeEnum.INNERFRIEND.getName());
        List<CreditInviteFriendsVo> creditInviteFriendsVos = new ArrayList<>();
        for (CreditSubscription creditSubscription : creditSubscriptionList) {
            CreditInviteFriendsVo creditInviteFriendsVo = new CreditInviteFriendsVo();
            User user = userService.getUserInfoByUserId(creditSubscription.getUserId());
            creditInviteFriendsVo.setUserPhoto(userPhotoAction.selectHeadImg(creditSubscription.getUserId()));
            creditInviteFriendsVo.setNickName(user.getNickname());
            creditInviteFriendsVo.setSex(user.getSex());
            creditInviteFriendsVo.setAge(ComputeAgeUtils.getAgeByBirthday(user.getBirthday()));
            creditInviteFriendsVo.setCredit(user.getCredit());
            creditInviteFriendsVo.setStatus(creditSubscription.getStatus());
            creditInviteFriendsVo.setAmount(creditSubscription.getSubscriptionNumber());
            creditInviteFriendsVos.add(creditInviteFriendsVo);
        }
        return creditInviteFriendsVos;
    }

    /**
     * 获取受邀商家内购详情
     */
    private List<CreditInnerDetailVo> getInnerMerchantDetial(String creditId) {
        List<String> merchantIdList = creditSubscriptionService.getInnerMerchangIdByCreditId(creditId);
        List<CreditInnerDetailVo> creditInnerDetailVos = new ArrayList<>();
        for (String merchantId : merchantIdList) {
            if (merchantId == null) {
                continue;
            }
            // 添加商家名称，分类，标签
            CreditInnerDetailVo creditInnerDetailVo = new CreditInnerDetailVo();
            creditInnerDetailVo.setMerchantName(merchantService.getMerchantNameById(merchantId));
            List<String> merchantCategoryIds = merchantCategoryService.getCategoryIdByMerchantId(merchantId);
            creditInnerDetailVo.setCategorys(categoryService.getParentCategoryName(merchantCategoryIds));
            creditInnerDetailVo.setTags(categoryService.getKidCategoryName(merchantCategoryIds));
            // 添加商家logo
            List<String> logoImagesUrl = merchantPictureAction.getPictureUrl(merchantId, MerchantPictureEnum.LOGO.getType());
            if(logoImagesUrl != null && logoImagesUrl.size() > 0) {
                creditInnerDetailVo.setLogoImagesUrl(logoImagesUrl);
            }
            // 添加商家人员各个身份内购详情
            List<CreditMerchantUserTypeDto> merchantUserTypeDtos = new ArrayList<>();
            List<CreditSubscription> creditSubscriptionList = creditSubscriptionService.getInnerMerchantManagerInfoByCreditId(creditId, merchantId);
            for (CreditSubscription creditSubscription : creditSubscriptionList) {
                CreditMerchantUserTypeDto creditMerchantUserTypeDto = new CreditMerchantUserTypeDto();
                creditMerchantUserTypeDto.setMerchantType(creditSubscription.getType());
                creditMerchantUserTypeDto.setRealName(userService.getUserById(creditSubscription.getUserId()).getRealName());
                creditMerchantUserTypeDto.setStatus(creditSubscription.getStatus());
                creditMerchantUserTypeDto.setAmount(creditSubscription.getSubscriptionNumber());
                merchantUserTypeDtos.add(creditMerchantUserTypeDto);
            }
            creditInnerDetailVo.setMerchantUserTypeDtos(merchantUserTypeDtos);
            creditInnerDetailVos.add(creditInnerDetailVo);
        }
        return creditInnerDetailVos;
    }

    /**
     * 获取发布者信息
     */
    private CreditPublisherInformationDto getCreditPublisherInfo(String creditUserId) {
        CreditPublisherInformationDto dto = new CreditPublisherInformationDto();
        //获取各种认证信息,并插入dto,传到前端
        User user = userService.selectById(creditUserId);
        //获取发布者头像
        dto.setPictureUrl(userPhotoAction.selectHeadImg(user.getId()));
        dto.setUserRealName(user.getRealName());
        //获取发布者信用值
        dto.setUserCredit(user.getCredit());
        //获取发布者信息完整度
        dto.setUserProfileScore(user.getUserProfileScore());
        //判断是否已认证
        if (user.getMobile() != null) {
            dto.setUserMobile(true);
        }
        if (user.getIdNumber() != null) {
            dto.setUserIdNumber(true);
        }
        if (user.getCar() != null) {
            dto.setUserCar(true);
        }
        if (user.getHouse() != null) {
            dto.setUserHouse(true);
        }
        if (user.getDegree() != null) {
            dto.setUserDegree(true);
        }

        return dto;
    }

    /**
     * 回滚到编辑网信备用数据
     */
    private PublishCreditRequestDto getEditCreditInfo(CreditDetailDto creditDetailDto, String cashierId) {
        PublishCreditRequestDto publishCreditRequestDto = new PublishCreditRequestDto();
        VoPoConverter.copyProperties(creditDetailDto, publishCreditRequestDto);
        publishCreditRequestDto.setCreditId(creditDetailDto.getId());
        publishCreditRequestDto.setCreditScope(creditDetailDto.getCreditScope());
        publishCreditRequestDto.setCreditDividendSettings(creditDetailDto.getCreditDividendSettings());
        CreditCashier creditCashier = creditCashierService.selectById(cashierId);
        publishCreditRequestDto.setCashierName(creditCashier.getCashierName());
        publishCreditRequestDto.setCashierIdNumber(userVerifyCreditService.getUserIdentityByUserId(userService.getUserIdByUserNumber(creditCashier.getCashierNetworkNum())));
        publishCreditRequestDto.setCashierPhone(creditCashier.getCashierPhone());
        publishCreditRequestDto.setCashierNetworkNum(creditCashier.getCashierNetworkNum());
        publishCreditRequestDto.setToUserId(StringUtils.join(creditSubscriptionService.getInnerFriendIds(creditDetailDto.getId(), "内购好友"), ','));
        return publishCreditRequestDto;
    }

    /**
     * 持有人列表
     */
    private List<CreditHolderVo> getHolder(String creditId) {
        List<CreditHolderVo> creditHolderVos = new ArrayList<>();

        // 获取该网信所有认购者信息
        List<CreditSubscription> creditSubscriptionList = creditSubscriptionService.getHolderListByCreditId(creditId);
        for (CreditSubscription creditSubscription : creditSubscriptionList) {
            CreditHolderVo creditHolderVo = new CreditHolderVo();
            Map<String, Object> result = userService.getCreditAndNicknameByUserId(creditSubscription.getUserId());
            creditHolderVo.setName((String)result.get("nickname"));
            creditHolderVo.setCredit((int)result.get("credit"));
            creditHolderVo.setHeadPhoto(userPhotoAction.selectHeadImg(creditSubscription.getUserId()));
            creditHolderVo.setSubscriptionDate(creditSubscription.getUpdateTime().getTime());
            creditHolderVo.setSubscriptionNumber(creditSubscription.getSubscriptionNumber());
            creditHolderVos.add(creditHolderVo);
        }
        return creditHolderVos;

    }

    /**
     * 获取网信分类标签的 id 和 名称
     */
    private List<CreditCategoryVo> getCreditCategory(String creditId) {
        List<String> categoryIds = creditCategoryService.getCategoryIdsByCreditId(creditId);
        List<CreditCategoryVo> creditCategoryVoList = new ArrayList<>();
        CreditCategoryVo creditCategoryVo = null;
        // 插入分类
        List<Category> categoryList = categoryService.getParentCategory(categoryIds);
        for (Category category : categoryList) {
            creditCategoryVo = new CreditCategoryVo();
            creditCategoryVo.setCategoryId(category.getId());
            creditCategoryVo.setName(category.getName());
            creditCategoryVo.setCreditCategoryStatus(0);
            creditCategoryVoList.add(creditCategoryVo);
        }
        // 插入标签
        List<Category> categoryTagList = categoryService.getKidCategory(categoryIds);
        for (Category category : categoryTagList) {
            creditCategoryVo = new CreditCategoryVo();
            creditCategoryVo.setCategoryId(category.getId());
            creditCategoryVo.setName(category.getName());
            creditCategoryVo.setCreditCategoryStatus(1);
            creditCategoryVoList.add(creditCategoryVo);
        }
        return creditCategoryVoList;
    }
}
