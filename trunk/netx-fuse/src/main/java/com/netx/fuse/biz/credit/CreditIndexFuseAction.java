package com.netx.fuse.biz.credit;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.credit.biz.CreditAction;
import com.netx.credit.model.*;
import com.netx.credit.model.constants.CreditStageName;
import com.netx.credit.service.CreditService;
import com.netx.credit.service.CreditSubscriptionService;
import com.netx.credit.utils.JsonUtils;
import com.netx.credit.vo.CreditAcountDto;
import com.netx.credit.vo.CreditAcountVo;
import com.netx.credit.vo.CreditHoldDto;
import com.netx.credit.vo.CreditPreSaleVo;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.searchengine.BaseSearchService;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserCredit;
import com.netx.ucenter.service.user.UserCreditService;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.DistrictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 梓
 * @date 2018-08-02 18:30
 */
@Service
public class CreditIndexFuseAction {



    @Autowired
    private CreditSubscriptionService creditSubscriptionService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantFuseAction merchantFuseAction;

    @Autowired
    private UserCreditService userCreditService;

    @Autowired
    private UserPhotoAction userPhotoAction;


    /*首页 - 我的网信**/
    public List<CreditHoldDto> getCreditHold(String userId){
        List<CreditSubscription> creditHold = creditSubscriptionService.getCreditHoldByuserId(userId);
        List<CreditHoldDto> creditHoldDtos = new ArrayList<>();
        for (CreditSubscription creditSubscription : creditHold) {
            Credit credit = creditService.selectById(creditSubscription.getCreditId());
            User user = userService.selectById(creditSubscription.getUserId());
            Merchant merchant = merchantService.selectById(credit.getMerchantId());
            CreditHoldDto creditHoldDto = new CreditHoldDto();
            creditHoldDto.setName(credit.getName());
            creditHoldDto.setUserCredit(user.getCredit());
            creditHoldDto.setSubscriptionNumber(creditSubscription.getSubscriptionNumber() * credit.getCreditPrice());
            creditHoldDto.setUserProfileScore(user.getUserProfileScore());
            /*获取用户与网信商家的距离**/
            creditHoldDto.setDistance(merchantFuseAction.getDistance(merchant.getLat(), merchant.getLon(), user.getLat(), user.getLon()));
            /*上月收益**/
            creditHoldDto.setLastMonthEarnings(null);
            /*累计收益**/
            creditHoldDto.setTotalEarnings(null);
            creditHoldDtos.add(creditHoldDto);
        }
        return creditHoldDtos;
    }

    /*首页 - 信用流水记录**/
    public List<CreditAcountVo> selectCreditRecordList(String userId) throws Exception{
        User user = userService.selectById(userId);
        List<CreditAcountDto> creditAcountDtos = new ArrayList<>();
        List<CreditAcountVo> creditAcountVos = new ArrayList<>();
        List<UserCredit> userCredits = userCreditService.selectUserCreditByUserId(userId);
        CreditAcountVo creditAcountVo = new CreditAcountVo();
        creditAcountVo.setUserPicture(userPhotoAction.selectHeadImg(user.getId()));
        creditAcountVo.setCreditRank(percentCreditRank(userId));
        creditAcountVo.setUserCredit(user.getCredit());
        for(UserCredit userCredit : userCredits){
            CreditAcountDto creditAcountDto = new CreditAcountDto();
            creditAcountDto.setCredit(userCredit.getCredit());
            creditAcountDto.setDescription(userCredit.getDescription());
            creditAcountDto.setData(userCredit.getCreateTime().getTime());
            creditAcountDtos.add(creditAcountDto);
            creditAcountVo.setCreditAcountDtoList(creditAcountDtos);
        }
        creditAcountVos.add(creditAcountVo);
        return creditAcountVos;
    }


    /*网信 - 正在预售**/
    public List<CreditPreSaleVo> getCreditPreSale(String userId) throws  Exception{
        User user = userService.selectById(userId);
        List<CreditPreSaleVo> creditPreSaleVos = new ArrayList<>();
        List<Credit> credits = creditService.getCreditId();
        for(Credit credit  : credits){
            CreditPreSaleVo creditPreSaleVo = new CreditPreSaleVo();
            Merchant merchant = merchantService.selectById(credit.getMerchantId());
            creditPreSaleVo.setCreditName(credit.getName());
            creditPreSaleVo.setCreditPicture(credit.getPictureUrl());
//            //网信销售剩余百分比
//            creditPreSaleVo.setCreditPercentRemaining();
//            //网信预售折扣
//          creditPreSaleVo.setDiscount();
            creditPreSaleVo.setCreditLevelDiscountSettings(CreditStageName.CSN_TOP.getSubscriptionRatio());
            //网信距离
            creditPreSaleVo.setDistance(merchantFuseAction.getDistance(merchant.getLat(), merchant.getLon(), user.getLat(), user.getLon()));
//          //网信分红
            //  creditPreSaleVo.setDividends();
            creditPreSaleVo.setCreditDividendSettings(JsonUtils.jsonToList(credit.getDividendSetting(), CreditDividendSetting.class));
            //网信预售总金额
            creditPreSaleVo.setPresaleUpperLimit(credit.getPresaleUpperLimit());
            //网信已售金额
            creditPreSaleVo.setSoldAmount(creditSubscriptionService.countCreditMoney(credit.getId()));
            //网信发布者信用值
            creditPreSaleVo.setUserCredit(user.getCredit());
            //网信发布者信息完善度
            creditPreSaleVo.setUserProfileScore(user.getUserProfileScore());
            creditPreSaleVos.add(creditPreSaleVo);
        }
        return creditPreSaleVos;
    }


    /*计算用户信用值百分比排行**/
    public Double percentCreditRank(String userId){
        double countUser =  userService.selectNums();
        double creditRank = userService.getCreditRankByUserId(userId);
        double percentRanking = (countUser - creditRank) / (countUser - 1) * 100;
        return percentRanking;
    }

    /*网信销售剩余百分比**/





}