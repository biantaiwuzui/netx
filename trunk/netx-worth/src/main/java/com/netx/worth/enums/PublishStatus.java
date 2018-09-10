package com.netx.worth.enums;

/* 发布活动状态的枚举 */
public enum PublishStatus {
    SAME{
        @Override
        public Integer getStatus() {
            return 0;
        }
        @Override
        public String getDescription() {
            return "发布的活动时间冲突";
        }
    },
    HASUNCOMPLETE{
        @Override
        public Integer getStatus() {
            return 1;
        }
        @Override
        public String getDescription() {
            return "你有未结束的活动";
        }
    },
    SUCCESS{
        @Override
        public Integer getStatus() {
            return 2;
        }
        @Override
        public String getDescription() {
            return "发布成功";
        }
    },
    FAIL{
        @Override
        public Integer getStatus() {
            return 3;
        }
        @Override
        public String getDescription() {
            return "发布失败";
        }
    },
    TIME_ERROR{
        @Override
        public Integer getStatus() {
            return 4;
        }
        @Override
        public String getDescription() {
            return "报名截止时间与开始时间相差需大于一小时以上";
        }
    };
    public abstract Integer getStatus();
    public abstract String getDescription();
}
