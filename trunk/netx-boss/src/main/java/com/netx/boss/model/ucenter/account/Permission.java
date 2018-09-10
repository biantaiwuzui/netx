package com.netx.boss.model.ucenter.account;

/**
 * @author Closure.Yang
 * @since 2015/8/10
 */
public enum Permission {
    CREATE {
        @Override
        public int val() {
            return 1;
        }

        @Override
        public String desc() {
            return "增";
        }
    },
    DELETE {
        @Override
        public int val() {
            return 2;
        }

        @Override
        public String desc() {
            return "删";
        }
    },
    UPDATE {
        @Override
        public int val() {
            return 4;
        }

        @Override
        public String desc() {
            return "改";
        }
    },
    QUERY {
        @Override
        public int val() {
            return 8;
        }

        @Override
        public String desc() {
            return "查";
        }
    };

    public abstract int val();
    public abstract String desc();
}
