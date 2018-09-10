package com.netx.common.common.enums;

public enum EvaluateTypeEnum {
    USER_EVALUATE{
        @Override
        public String getName(){
            return "";
        }
        @Override
        public String getValue() {
            return "user";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.USER_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return null;
        }
    },
    SELLER_EVALUATE{
        @Override
        public String getName(){
            return "的商家";
        }
        @Override
        public String getValue() {
            return "seller";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.PRODUCT_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.SELLER_DETAIL;
        }
    },
    WISH_EVALUATE{
        @Override
        public String getName(){
            return "的心愿";
        }
        @Override
        public String getValue() {
            return "wish";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.ACTIVITY_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.WZ_WISHDETAIL;
        }
    },
    SKILL_EVALUATE{
        @Override
        public String getName(){
            return "的技能";
        }
        @Override
        public String getValue() {
            return "skill";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.ACTIVITY_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.WZ_SKILLDETAIL;
        }
    },
    DEMAND_EVALUATE{
        @Override
        public String getName(){
            return "的需求";
        }
        @Override
        public String getValue() {
            return "demand";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.ACTIVITY_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.WZ_DEMANDDETAIL;
        }
    },
    MEETING_EVALUATE{
        @Override
        public String getName(){
            return "的活动";
        }
        @Override
        public String getValue() {
            return "meeting";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.ACTIVITY_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.MEETINGDETAIL;
        }
    },
    ARTICLE_EVALUATE{
        @Override
        public String getName(){
            return "的图文";
        }
        @Override
        public String getValue() {
            return "article";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.USER_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.ARTICLE_DETAIL;
        }
    },
    PRODUCT_EVALUATE{
        @Override
        public String getName(){
            return "的商品";
        }
        @Override
        public String getValue() {
            return "product";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.PRODUCT_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.PRODUCT_DETAIL;
        }
    },
    CREDIT_EVALUATE{
        @Override
        public String getName(){
            return "的网信";
        }
        @Override
        public String getValue() {
            return "credit";
        }
        @Override
        public MessageTypeEnum getMessageTypeEnum() {
            return MessageTypeEnum.CREDIT_TYPE;
        }
        @Override
        public PushMessageDocTypeEnum getPushMessageDocTypeEnum() {
            return PushMessageDocTypeEnum.CurrencyDetial;
        }
    };

    public abstract String getName();

    public abstract String getValue();

    public abstract MessageTypeEnum getMessageTypeEnum();

    public abstract PushMessageDocTypeEnum getPushMessageDocTypeEnum();

}
