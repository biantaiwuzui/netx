package com.netx.common.common.enums;


/**
 * 推送docType枚举类，推送跳转根据这个来实现跳转
 * @Author 黎子安
 * @Date 2018-5-2
 */
public enum PushMessageDocTypeEnum {
    //boss系统暂不跳转2018.6.27
    BossSuggest{
        @Override
        public int getCode() {
            return 19;
        }
        @Override
        public String getValue() {
            return "bossSuggest";
        }
        @Override
        public String getName() {
            return "Boss系统审批";
        }
    },
    GoodsOrderDetail{
        @Override
        public int getCode() {
            return 1;
        }
        @Override
        public String getValue() {
            return "goodsOrderDetail";
        }
        @Override
        public String getName() {
            return "订单详情";
        }
    },
    CurrencyDetial{
        @Override
        public int getCode() {
            return 2;
        }
        @Override
        public String getValue() {
            return "currencyDetail";
        }
        @Override
        public String getName() {
            return "网币详情";
        }
    },
    LuckyMoneyDetail{
        @Override
        public int getCode() {
            return 3;
        }
        @Override
        public String getValue() {
            return "luckyMoneyDetail";
        }
        @Override
        public String getName() {
            return "红包详情";
        }
    },
    ENROLLAPPLY{
        @Override
        public int getCode() {
            return 4;
        }
        @Override
        public String getValue() {
            return "enroolApply";
        }
        @Override
        public String getName() {
            return "报名申购";
        }
    },
    ADDCUSTOMERAGENT{
        @Override
        public int getCode() {
            return 5;
        }
        @Override
        public String getValue() {
            return "addCustomeragent";
        }
        @Override
        public String getName() {
            return "申请新增客服";
        }
    },
    MEETINGDETAIL{
        @Override
        public int getCode() {
            return 6;
        }
        @Override
        public String getValue() {
            return "meetingDetail";
        }
        @Override
        public String getName() {
            return "活动详情";
        }
    },
    WZ_INVOCATION{
        @Override
        public int getCode() {
            return 7;
        }
        @Override
        public String getValue() {
            return "wzInvocationDetail";
        }
        @Override
        public String getName() {
            return "网值邀请详情";
        }
    },
    WZ_GIFTDETAIL{
        @Override
        public int getCode() {
            return 8;
        }
        @Override
        public String getValue() {
            return "wzGiftDetail";
        }
        @Override
        public String getName() {
            return "网值赠送详情";
        }
    },
    WZ_SKILLDETAIL{
        @Override
        public int getCode() {
            return 9;
        }
        @Override
        public String getValue() {
            return "wzSkillDetail";
        }
        @Override
        public String getName() {
            return "网值技能详情";
        }
    },
    WZ_DEMANDDETAIL{
        @Override
        public int getCode() {
            return 10;
        }
        @Override
        public String getValue() {
            return "wzDemandDetail";
        }
        @Override
        public String getName() {
            return "网值需求详情";
        }
    },
    WZ_WISHDETAIL{
        @Override
        public int getCode() {
            return 11;
        }
        @Override
        public String getValue() {
            return "wzWishDetail";
        }
        @Override
        public String getName() {
            return "网值心愿详情";
        }
    },
    ADD_FRIEND{
        @Override
        public int getCode() {
            return 12;
        }
        @Override
        public String getValue() {
            return "addFriend";
        }
        @Override
        public String getName() {
            return "添加好友";
        }
    },
    SELLER_DETAIL{
        @Override
        public int getCode() {
            return 13;
        }
        @Override
        public String getValue() {
            return "sellerDetail";
        }
        @Override
        public String getName() {
            return "商家详情";
        }
    },
    ARTICLE_DETAIL{
        @Override
        public int getCode() {
            return 14;
        }
        @Override
        public String getValue() {
            return "articleDetail";
        }
        @Override
        public String getName() {
            return "图文详情";
        }
    },
    ARTICLE_PAY_DETAIL{
        @Override
        public int getCode() {
            return 15;
        }
        @Override
        public String getValue() {
            return "articlePayDetail";
        }
        @Override
        public String getName() {
            return "图文费用提醒";
        }
    },
    PRODUCT_DETAIL{
        @Override
        public int getCode() {
            return 16;
        }
        @Override
        public String getValue() {
            return "productDetail";
        }
        @Override
        public String getName() {
            return "商品详情";
        }
    },
    USER_DETAIL{
        @Override
        public int getCode() {
            return 17;
        }
        @Override
        public String getValue() {
            return "userDetail";
        }
        @Override
        public String getName() {
            return "用户详情";
        }
    },
    DEAL_CUSTOMERAGENT{
        @Override
        public int getCode() {
            return 18;
        }
        @Override
        public String getValue() {
            return "dealCustomeragent";
        }
        @Override
        public String getName() {
            return "新增客服处理结果";
        }
    },
    PRODUCT_INVENTORY_DETAIL{
        @Override
        public int getCode() {
            return 19;
        }

        @Override
        public String getValue() {
            return "InventoryDetail";
        }

        @Override
        public String getName() {
            return "商品库存提示";
        }
    },
    WZ_MATCH{
        @Override
        public int getCode() {
            return 20;
        }
        @Override
        public String getValue() {
            return "wzMatch";
        }
        @Override
        public String getName() {
            return "网值比赛";
        }
    },

    WZ_WALLET{
        @Override
        public int getCode() {
            return 21;
        }
        @Override
        public String getValue() {
            return "walletDetail";
        }
        @Override
        public String getName() {
            return "钱包详情";
        }
    },
    WZ_MATCH_MEMBERS{
        @Override
        public int getCode() {
            return 22;
        }
        @Override
        public String getValue() {
            return "wzMatchMembers";
        }
        @Override
        public String getName() {
            return "网值比赛工作人员";
        }
    },
    WZ_MATCH_REASON{
        @Override
        public int getCode() {
            return 22;
        }
        @Override
        public String getValue() {
            return "wzMatchReason";
        }
        @Override
        public String getName() {
            return "网值比赛理由";
        }
    };

    public abstract int getCode();

    public abstract String getValue();

    public abstract String getName();

}
