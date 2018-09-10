package com.netx.shopping.enums;

public enum BusinessJobEnum {
    INSPECTOR(1,90,3000,PromotionAwardEnum.INSPECTOR_AWARD),
    CEO(2,150,5000,PromotionAwardEnum.CEO_AWARD);

    private Integer job;
    private Integer num;
    private Integer salary;
    private PromotionAwardEnum promotionAwardEnum;

    BusinessJobEnum(Integer job, Integer num, Integer salary, PromotionAwardEnum promotionAwardEnum) {
        this.job = job;
        this.num = num;
        this.salary = salary;
        this.promotionAwardEnum = promotionAwardEnum;
    }

    public PromotionAwardEnum getPromotionAwardEnum() {
        return promotionAwardEnum;
    }

    public void setPromotionAwardEnum(PromotionAwardEnum promotionAwardEnum) {
        this.promotionAwardEnum = promotionAwardEnum;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
