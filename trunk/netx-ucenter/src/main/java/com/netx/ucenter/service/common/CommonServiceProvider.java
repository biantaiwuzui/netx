package com.netx.ucenter.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonServiceProvider {

    @Autowired
    private ArbitrationResultService arbitrationResultService;

    @Autowired
    private ArbitrationService arbitrationService;

    @Autowired
    private ArticleLimitedService articleLimitedService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private BillService billService;

    @Autowired
    private CostService costService;

    @Autowired
    private DepositBillService depositBillService;

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private ExamineFinanceService examineFinanceService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private JobHistoryService jobHistoryService;

    @Autowired
    private LuckyMoneyService luckyMoneyService;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private OtherSetService otherSetService;

    @Autowired
    private ReceivablesOrderService receivablesOrderService;

    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private SensitiveSuggestService sensitiveSuggestService;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private WalletFrozenService walletFrozenService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WzCommonImHistoryService wzCommonImHistoryService;

    public ArbitrationResultService getArbitrationResultService() {
        return arbitrationResultService;
    }

    public ArbitrationService getArbitrationService() {
        return arbitrationService;
    }

    public AreaService getAreaService() {
        return areaService;
    }

    public ArticleLimitedService getArticleLimitedService() {
        return articleLimitedService;
    }

    public BillService getBillService() {
        return billService;
    }

    public CostService getCostService() {
        return costService;
    }

    public DepositBillService getDepositBillService() {
        return depositBillService;
    }

    public EvaluateService getEvaluateService() {
        return evaluateService;
    }

    public ExamineFinanceService getExamineFinanceService() {
        return examineFinanceService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public GroupUserService getGroupUserService() {
        return groupUserService;
    }

    public JobHistoryService getJobHistoryService() {
        return jobHistoryService;
    }

    public LuckyMoneyService getLuckyMoneyService() {
        return luckyMoneyService;
    }

    public MessagePushService getMessagePushService() {
        return messagePushService;
    }

    public OtherSetService getOtherSetService() {
        return otherSetService;
    }

    public ReceivablesOrderService getReceivablesOrderService() {
        return receivablesOrderService;
    }

    public SensitiveService getSensitiveService() {
        return sensitiveService;
    }

    public SensitiveSuggestService getSensitiveSuggestService() {
        return sensitiveSuggestService;
    }

    public SuggestionService getSuggestionService() {
        return suggestionService;
    }

    public TagsService getTagsService() {
        return tagsService;
    }

    public WalletFrozenService getWalletFrozenService() {
        return walletFrozenService;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public WzCommonImHistoryService getWzCommonImHistoryService() {
        return wzCommonImHistoryService;
    }
}
