package com.netx.fuse.biz.credit;

import com.netx.common.common.enums.FrozenTypeEnum;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.credit.biz.CreditSubscriptionAction;
import com.netx.credit.model.CreditSubscription;
import com.netx.credit.model.constants.CreditStageName;
import com.netx.credit.service.CreditCashierService;
import com.netx.credit.service.CreditService;
import com.netx.credit.service.CreditSubscriptionService;
import com.netx.credit.vo.CreditSubscriptionDto;
import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import com.netx.ucenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lanyingchu
 * @date 2018/8/2 9:30
 */
@Service
public class CreditSubscriptionFuseAction {

    @Autowired
    private CreditSubscriptionAction creditSubscriptionAction;
    @Autowired
    private CreditSubscriptionService creditSubscriptionService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private CreditCashierService creditCashierService;
    @Autowired
    private UserService userService;
    @Autowired
    private WallerFrozenFuseAction wallerFrozenFuseAction;
    @Autowired
    private WalletFrozenAction walletFrozenAction;

    /**
     * 用户认购或内购人员同意认购
     */
    public String subscriptionOrAgree(CreditSubscriptionDto dto) {
        // 将认购额转化为分
        dto.setSubscriptionNumber(dto.getSubscriptionNumber() * 100);
        // 获取收银人id
        String cashierId = this.getCashierId(dto.getCreditId());
        this.noteCreditBill(dto.getCreditId(), dto.getSubscriptionNumber(), dto.getUserId(), cashierId, dto.getType());
        // 判断该用户是否为内购人员
        CreditSubscription creditSubscription = creditSubscriptionService.getMerchantInnerCreditInfo(dto.getUserId(), dto.getType(), dto.getCreditId(), dto.getMerchantId());
        String detialId = null;
        if (creditSubscription == null) {
            // 不是内购邀请用户，添加认购信息
            detialId = creditSubscriptionAction.add(dto, null);
        } else {
            // 在内购邀请范围内，修改认购信息
            creditSubscriptionAction.editSubscription(dto);
            detialId = dto.getCreditId();
        }
        this.noteCreditPay(dto.getCreditId(), dto.getUserId());
        return detialId;
    }

    /**
     * 获取收银员 id
     */
    private String getCashierId(String creditId) {
        // 获取收银员 id
        String cashierId = creditService.getCashierId(creditId);
        cashierId = creditCashierService.getCashierNetworkNumber(cashierId);
        cashierId = userService.getUserIdByUserNumber(cashierId);
        return cashierId;
    }

    /**
     * 记录付款人流水账(冻结付款人付款金额)
     */
    private void noteCreditBill(String creditId, Double subscriptionNumber, String userId, String cashierId, String type) {
        CommonWalletFrozen commonWalletFrozen = new CommonWalletFrozen();
        // 判断用户身份并记录对应折扣
        Double ratio;
        if (type.equals(CreditStageName.CSN_TOP.getStageName())) {
            ratio = CreditStageName.CSN_TOP.getSubscriptionRatio();
        } else if (type.equals(CreditStageName.CSN_FRIEND.getStageName())) {
            ratio = CreditStageName.CSN_FRIEND.getSubscriptionRatio();
        } else {
            ratio = CreditStageName.CSN_COMMON.getSubscriptionRatio();
        }
        commonWalletFrozen.setAmount((long)(subscriptionNumber * ratio));
        commonWalletFrozen.setUserId(userId);
        commonWalletFrozen.setToUserId(cashierId);
        commonWalletFrozen.setDescription("网信认购" + subscriptionNumber/100 + "网信");
        commonWalletFrozen.setTypeId(creditId);
        commonWalletFrozen.setBak1("0");
        commonWalletFrozen.setFrozenType(FrozenTypeEnum.FTZ_CREDIT.getName());
        wallerFrozenFuseAction.addFrozenAndBill(commonWalletFrozen);
    }

    /**
     * 记录收款人流水账(并实现收款人钱包更新)
     */
    private void noteCreditPay(String creditId, String userId) {
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(creditId);
        requestDto.setUserId(userId);
        requestDto.setType(FrozenTypeEnum.FTZ_CREDIT);
        walletFrozenAction.pay(requestDto);
    }
}
