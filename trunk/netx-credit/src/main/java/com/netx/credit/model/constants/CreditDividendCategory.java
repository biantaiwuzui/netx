package com.netx.credit.model.constants;

/**
 * 网信分红类别
 */
public enum CreditDividendCategory {


    /**
     * 网能
     */
    CDC_WORTH {
        @Override
        public String getCategoryName() {
            this.getRatio();
            return "CDC_WORTH";
        }
        @Override
        public Double getRatio() {
            return 0.05;
        }
    },
    /**
     * 网商
     */
    CDC_MERCHANT {
        @Override
        public String getCategoryName() {
            return "CDC_MERCHANT";
        }
        @Override
        public Double getRatio() {
            return 0.03;
        }
    };

    public abstract String getCategoryName();

    public abstract Double getRatio();
}
