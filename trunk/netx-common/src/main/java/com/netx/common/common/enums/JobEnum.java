package com.netx.common.common.enums;

public enum JobEnum {
    DEMO_JOB("DemoJobHandler"),
    SHARDING_JOB("ShardingJobHandler"),
    ARTICLE_DELETED_JOB("ArticleDeletedJobHandler"),
    CHECK_IS_SEND_FINISH_JOB("CheckIsSendFinishJobHandler"),
    PUSH_RED_PACKET_INFORMATION_JOB("PushRedPacketInformationJobHandler"),
    SKILL_CHECK_EVALUATE_JOB("SkillCheckEvaluateJobHandle"),//检测预约双方是否评论，没有评论的一方扣除信用
    SKILL_CHECK_REGISTER_JOB("SkillRegisterAcceptJobHandler"),//检测未接受或预约者设定的时间开始后，发布者均未响应，则预约失败，托管费用解冻并退回给预约者
    SKILL_CHECK_ORDER_JOB("SkillCheckOrderStartJobHandler"),
    SKILL_CHECK_SUCCESS_JOB("SkillCheckSuccessJobHandle"),//检测技能预约验证码和距离是否通过
    SKILL_CHECK_START_JOB("SkillCheckStartJobHandle"),//检测技能预约开始时间到了是否已经启动预约
    SKILL_CHECK_REFUND_JOB("SkillCheckTimeoutRefundJobHandle"),//检测预约者申请退款36个小时后是否发布者是否处理
    DEMAND_CHECK_START_JOB("DemandCheckStartHandler"),//检查需求开始，报名方是否确认启动
    DEMAND_CHECK_ORDER_JOB("DemandCheckEvaluateJobHandler"),
    DEMAND_CHECK_AUTO_CANCEL_JOB("DemandCheckAutoCancelJobHandler"),//检测需求是否因无入选人而自动取消
    DEMAND_CHECK_AUTO_PAY_JOB("DemandCheckAutoPayJobHandler"),//检测需求完成二十四小时后,是否自动结算入选者报酬
    DEMAND_CHECK_PASS_CODE_JOB("DemandCheckPassCodeJobHandler"),//检测需求开始后30分钟是否有入选者验证码通过
    DEMAND_CHECK_REGISTER_START("DemandCheckRegisterStartJobHandler"),//检测需求开始前60分钟入选者是否已启动需求
    MERCHANT_SUBTRACT_CREDIT_JOB("MerchantSubtractCreditJobHandler"),
    MERCHANT_TRANSACTION_PUNISH_JOB("MerchantTransactionPunishJobHandler"),
    MEETING_CHECK_SELECTED_CONDITIONS_JOB("MeetingCheckRegisterJobHandler"),//检查确定入选人条件
    MEETING_SURE_SELECTED_JOB("MeetingSureSelectedJobHandler"),//确认活动入选人
    MEETING_CHECK_AMOUNT_JOB("MeetingCheckAmountJobHandler"),//检查活动报名费用是否足够
    MEETING_CHECK_CONFIRM_JOB("MeetingCheckConfirmHandler"),//检查确认活动细节
    MEETING_CHECK_PUBLISH_START_JOB("MeetingCheckPublishStartJobHandler"),//检查活动发布者是否同意开始活动
    MEETING_CHECK_SUCCESS_JOB("MeetingCheckSuccessJobHandler"),//检查活动校验是否通过
    MEETING_AUTO_SETTLE_JOB("MeetingAutoSettleJobHandler"),//活动结束自动结算
    MEETING_CHECK_EVALUATE_JOB("MeetingCheckEvaluateJobHandler"),//活动结束后检查24小时内是否评价
    MEETING_REMIND_ATTEND_jOB("MeetingRemindAttendHandler"),//活动开始前30分钟提醒参与者出席
    WISH_CHECK_EVALUATE_JOB("WishCheckEvaluateJobHandler"),//创建“检测是否双方已评价”定时任务
    WISH_CHECK_REFEREE_JOB("WishCheckRefereeJobHandler"),//创建“检测好友推荐”定时任务
    WISH_CHECK_SUCCESS_JOB("WishcheckSuccessHandler"),//创建“检测是否发起成功”定时任务
    WISH_CHECK_sixtyDay_JOB("WishcheckSixtyDayJobHandler"),//创建“检查60天内有无新的用款申请”的定时任务
    MATCH_BUY_TICKET_ROLLBACK_JOB("MatchBuyTicketRollBackHandler"),//赛事购票回滚
    Match_CLEAR_MONEY_JOB("MatchClearMoneyHandler");//赛事结算

    private String name;

    JobEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
