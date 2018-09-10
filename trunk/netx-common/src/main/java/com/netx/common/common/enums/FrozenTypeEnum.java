package com.netx.common.common.enums;
public enum FrozenTypeEnum {
    FTZ_WISH{
        @Override
        public String getName() {
            return "Wish";
        }
    },
    FTZ_MEETING{
        @Override
        public String getName() {
            return "Meeting";
        }
    },
    FTZ_DEMAND{
        @Override
        public String getName() {
            return "Demand";
        }
    },
    FTZ_SKILL{
        @Override
        public String getName() {
            return "Skill";
        }
    },
    FTZ_CREDIT{
        @Override
        public String getName() {
            return "Credit";
        }
    },
    FTZ_PRODUCT{
        @Override
        public String getName() {
            return "Product";
        }
    },
    FTZ_ARTICLE{
        @Override
        public String getName() {
            return "Article";
        }
    },
    //赛事
    FTZ_MATCH{
        @Override
        public String getName() {
            return "Match";
        }
    };

    public abstract String getName();
}
