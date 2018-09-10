package com.netx.worth.service;

import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.model.DemandOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorthServiceprovider {
    @Autowired
    private DemandOrderService demandOrderService;
    @Autowired
    private DemandRegisterService demandRegisterService;
    @Autowired
    private DemandService demandService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private MeetingRegisterService meetingRegisterService;
    @Autowired
    private MeetingSendService meetingSendService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillRegisterService skillRegisterService;
    @Autowired
    private SkillOrderService skillOrderService;
    @Autowired
    private WishService wishService;
    @Autowired
    private WishSupportService wishSupportService;
    @Autowired
    private WishAuthorizeService wishAuthorizeService;
    @Autowired
    private WishRefereeService wishRefereeService;
    @Autowired
    private WishHistoryService wishHistoryService;
    @Autowired
    private WishApplyService wishApplyService;
    @Autowired
    private WishManagerService wishManagerService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private SettlementAmountService settlementAmountService;
    @Autowired
    private SettlementCreditService settlementCreditService;
    @Autowired
    private SettlementLogService settlementLogService;
    @Autowired
    private ActiveLogService activeLogService;
    @Autowired
    private WishBankService wishBankService;
    @Autowired
    private WishGroupService wishGroupService;
    @Autowired
    private WorthClickHistoryService worthClickHistoryService;

    public SkillService getSkillService() {
        return skillService;
    }

    public DemandOrderService getDemandOrderService() {
        return this.demandOrderService;
    }

    public DemandRegisterService getDemandRegisterService() {
        return this.demandRegisterService;
    }

    public DemandService getDemandService() {
        return this.demandService;
    }

    public GiftService getGiftService() {
        return this.giftService;
    }

    public InvitationService getInvitationService() {
        return this.invitationService;
    }

    public MeetingRegisterService getMeetingRegisterService() {
        return this.meetingRegisterService;
    }

    public MeetingSendService getMeetingSendService() {
        return this.meetingSendService;
    }

    public MeetingService getMeetingService() {
        return this.meetingService;
    }

    public SkillRegisterService getSkillRegisterService() {
        return this.skillRegisterService;
    }

    public SkillOrderService getSkillOrderService() {
        return this.skillOrderService;
    }

    public WishService getWishService() {
        return this.wishService;
    }

    public WishSupportService getWishSupportService() {
        return this.wishSupportService;
    }

    public WishAuthorizeService getWishAuthorizeService() {
        return this.wishAuthorizeService;
    }

    public WishRefereeService getWishRefereeService() {
        return this.wishRefereeService;
    }

    public WishHistoryService getWishHistoryService() {
        return this.wishHistoryService;
    }

    public WishApplyService getWishApplyService() {
        return this.wishApplyService;
    }

    public WishGroupService getWishGroupService() {
        return wishGroupService;
    }

    public WishBankService getWishBankService() {
        return wishBankService;
    }

    public WishManagerService getWishManagerService() {
        return this.wishManagerService;
    }

    public RefundService getRefundService() {
        return this.refundService;
    }

    public SettlementService getSettlementService() {
        return this.settlementService;
    }

    public SettlementAmountService getSettlementAmountService() {
        return this.settlementAmountService;
    }

    public SettlementCreditService getSettlementCreditService() {
        return this.settlementCreditService;
    }

    public SettlementLogService getSettlementLogService() {
        return this.settlementLogService;
    }

    public ActiveLogService getActiveLogService() {
        return this.activeLogService;
    }

    public WorthClickHistoryService getWorthClickHistoryService() {
        return worthClickHistoryService;
    }

}
