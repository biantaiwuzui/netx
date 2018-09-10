package com.netx.common.common.enums;

public enum CostSettingEnum {
    SHARED_FEE("分成","shared_fee",0),
    WITHDRAW_FEE("提现手续费","withdraw_fee",0),
    SHOP_MANAGER_FEE("注册商家管理费","shop_manager_fee",0),
    SHOP_MANAGER_FEE_LIMIT_DATE("注册商家管理费有效期","shop_manager_fee_limit_date",0),
    SHOP_CATEGORY_FEE("商品一级类目收费","shop_category_fee",0),
    SHOP_TAGS_FEE("商家二级类目收费","shop_tags_fee",0),
    CREDIT_ISSUE_FEE("网信发行费","credit_issue_fee",0),
    CREDIT_FUNDS_INTEREST("网信竞购系数","credit_funds_interest",0),
    CREDIT_SUBSCRIBE_FEE("网信报名认购费用","credit_subscribe_fee",0),
    CREDIT_INST("网信资金利息","credit_inst",0),
    PIC_AND_VOICE_PUBLISH_DEPOSIT("图文、音视的发布押金","pic_and_voice_publish_deposit",0),
    CLICK_FEE("点击费用","click_fee",0),
    VIOLATION_CLICK_FEE(" 违规图文、音视的点击费用","violation_click_fee",0),
    WISH_CAPITAL_MANAGE_FEE("心愿资金管理费","wish_capital_manage_fee",0),
    SALER_SHARED_FEE("销售收入分成","saler_shared_fee",0);

    private String name;

    private String value;

    private Object cost;

    CostSettingEnum(String name, String value, Object cost) {
        this.name = name;
        this.value = value;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Object getCost() {
        return cost;
    }

    public void setCost(Object cost) {
        this.cost = cost;
    }
}
