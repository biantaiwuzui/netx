package com.netx.common.user.enums;

public enum StatScoreEnum {

    SS_ADD_FRIEND{
        @Override
        public double score() {
            return 2;
        }
        @Override
        public String getDetail() {
            return "添加一个好友";
        }
    },
    SS_VERIFY_FRIEND{
        @Override
        public double score() {
            return 3;
        }
        @Override
        public String getDetail() {
            return "好友通过实名认证";
        }
    },
    SS_USER_DETAIL{
        @Override
        public double score() {
            return 2;
        }
        @Override
        public String getDetail() {
            return "个人中心被浏览";
        }
    },
    SS_PUBLISH_ARTICLE{
        @Override
        public double score() {
            return 15;
        }
        @Override
        public String getDetail() {
            return "发布图文";
        }
    },
    SS_SUGGEST_PASS{
        @Override
        public double score() {
            return 15;
        }
        @Override
        public String getDetail() {
            return "用户建议通过或搁置";
        }
    },
    SS_READ_ARTICLE{
        @Override
        public double score() {
            return 2;
        }
        @Override
        public String getDetail() {
            return "图文被浏览";
        }
    },
    SS_READ_PAY_ARTICLE{
        @Override
        public double score() {
            return 3;
        }
        @Override
        public String getDetail() {
            return "付费图文被浏览";
        }
    },
    SS_PUBLISH_WORTH{
        @Override
        public double score() {
            return 10;
        }
        @Override
        public String getDetail() {
            return "发布网能";
        }
    },
    SS_FINISH_WORTH{
        @Override
        public double score() {
            return 10;
        }
        @Override
        public String getDetail() {
            return "免费网能完成";
        }
    },
    SS_FINISH_PAY_WORTH{
        @Override
        public double score() {
            return 15;
        }
        @Override
        public String getDetail() {
            return "付费网能完成";
        }
    },
    SS_OTHER_FINISH_WORTH{
        @Override
        public double score() {
            return 5;
        }
        @Override
        public String getDetail() {
            return "参与网能完成";
        }
    },
    SS_OTHER_FINISH_PAY_WORTH{
        @Override
        public double score() {
            return 20;
        }
        @Override
        public String getDetail() {
            return "参与付费网能完成";
        }
    },
    SS_MERCHANT{
        @Override
        public double score() {
            return 50;
        }
        @Override
        public String getDetail() {
            return "注册商家并发布三个商品";
        }
    },
    SS_PRODUCT{
        @Override
        public double score() {
            return 10;
        }
        @Override
        public String getDetail() {
            return "三个商品以上";
        }
    },
    SS_MERCHANT_ORDER{
        @Override
        public double score() {
            return 15;
        }
        @Override
        public String getDetail() {
            return "完成交易";
        }
    },
    SS_USER_ORDER{
        @Override
        public double score() {
            return 20;
        }
        @Override
        public String getDetail() {
            return "购买商品";
        }
    };

    public abstract double score();

    public abstract String getDetail();
}
