package com.netx.worth.enums;


public enum DemandOrderStatus {
	 ACCEPT(1, "已接受，即已确定入选人"),
     CONFIRM(2, "已确定细节"),
     START(3, "需求启动，只要入选者有人启动成功，就设置为该值"),
     CANCEL(4, "超时未确认细节"),
     SUCCESS(5, "需求成功，即：距离、验证码都通过"),
     REFUNDMENT(6,"退款状态"),
     TIMEOUT(7,"超时未启动需求"),
     ARBITRATION(8,"仲裁中"),
     AllEND(9,"支付完成 需求结束");

     public Integer status;
     private String description;

     private DemandOrderStatus(Integer status, String description) {
         this.status = status;
         this.description = description;
     }
}
