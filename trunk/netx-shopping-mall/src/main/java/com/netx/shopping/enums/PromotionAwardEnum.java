package com.netx.shopping.enums;

public enum PromotionAwardEnum {
    BUSINESS_AWARD(1,"商务代理",new Integer[]{2000,1800,1500,1200,1000}),
    INSPECTOR_AWARD(2,"市场总监",new Integer[]{50000,30000,10000}),
    CEO_AWARD(3,"营运总裁",new Integer[]{100000,80000,50000});

    private Integer job;
    private String name;
    private Integer[] award;

    PromotionAwardEnum(Integer job, String name, Integer[] award) {
        this.job = job;
        this.name = name;
        this.award = award;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getAward() {
        return award;
    }

    public void setAward(Integer[] award) {
        this.award = award;
    }
}
