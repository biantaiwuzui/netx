package com.netx.fuse.biz.credit;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.credit.biz.CreditAction;
import com.netx.credit.model.Credit;
import com.netx.credit.model.CreditSubscription;
import com.netx.credit.service.CreditService;
import com.netx.credit.service.CreditSubscriptionService;
import com.netx.credit.vo.CreditAcountDto;
import com.netx.credit.vo.CreditHoldDto;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.searchengine.BaseSearchService;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.service.merchantcenter.MerchantService;
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
public class CreditHoldFuseAction {



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
    public List<CreditAcountDto> selectCreditRecordList(String userId) throws Exception{
        User user = userService.selectById(userId);
        List<CreditAcountDto> creditAcountDtos = new ArrayList<>();
        List<UserCredit> userCredits = userCreditService.selectUserCreditByUserId(userId);
        for(UserCredit userCredit : userCredits){
            CreditAcountDto creditAcountDto = new CreditAcountDto();
            creditAcountDto.setCreditRank(null);
            creditAcountDto.setUserCredit(user.getCredit());
            creditAcountDto.setCredit(userCredit.getCredit());
            creditAcountDto.setDescription(userCredit.getDescription());
            creditAcountDto.setData(userCredit.getCreateTime().getTime());
            creditAcountDtos.add(creditAcountDto);
        }
        return creditAcountDtos;
    }

}