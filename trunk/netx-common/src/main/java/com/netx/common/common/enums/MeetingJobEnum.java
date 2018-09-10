//package com.netx.common.common.enums;
//
//import java.util.Date;
//
//public enum MeetingJobEnum {
//
//    MEETING_CHECK_REGISTER_JOB("MeetingCheckRegisterJobHandler"),//确认活动入选人
//    MEETING_CHECK_AMOUNT_JOB("MeetingCheckAmountJobHandler"),//检查活动报名费用是否足够
//    MEETING_CHECK_REGCOUNT_JOB("MeetingCheckRegCountJobHandler"),//检查活动报名人数是否足够
//    MEETING_CHECK_CONFIRM_JOB("MeetingCheckConfirmHandler"),//提醒一次发起人确认活动细节
//    MEETING_CHECK_NOCONFIRM_JOB("MeetingCheckNoConfirmHandler"),//检查是否确认活动细节
//    MEETING_CHECK_PUBLISH_START_JOB("MeetingCheckPublishStartJobHandler"),//检查活动发布者是否同意开始活动
//    MEETING_CHECK_SUCCESS_JOB("MeetingCheckSuccessJobHandler"),//检查活动校验是否通过
//    MEETING_AUTO_SETTLE_JOB("MeetingAutoSettleJobHandler"),//活动结束自动结算
//    MEETING_CHECK_EVALUATE_JOB("MeetingCheckEvaluateJobHandler");//活动结束后检查24小时内是否评价

//    private String param;
//    private String typeId;
//    private String typeName;
//    private Date executeTime;
//    private AuthorEmailEnum authorEmailEnum;
//
//    MeetingJobEnum(String param, String typeId, String typeName, Date executeTime, AuthorEmailEnum authorEmailEnum) {
//        this.param = param;
//        this.typeId = typeId;
//        this.typeName = typeName;
//        this.executeTime = executeTime;
//        this.authorEmailEnum = authorEmailEnum;
//    }
//    
//
//    public String getParam() {
//        return param;
//    }
//
//    public String getTypeId() {
//        return typeId;
//    }
//
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public Date getExecuteTime() {
//        return executeTime;
//    }
//
//    public AuthorEmailEnum getAuthorEmailEnum() {
//        return authorEmailEnum;
//    }
//    
//}
