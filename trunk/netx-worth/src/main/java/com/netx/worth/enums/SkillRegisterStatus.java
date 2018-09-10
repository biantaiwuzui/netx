package com.netx.worth.enums;

public enum SkillRegisterStatus {
	 REGISTERED(0, "待入选"),
     SUCCESS(1, "已入选"),
     CANCEL(2, "已取消"),
     FAIL(3, "已过期");

     public Integer status;
     public String description;

     private SkillRegisterStatus(Integer status, String description) {
         this.status = status;
         this.description = description;
     }

     public Integer getStatus() {
         return status;
     }

     public void setStatus(Integer status) {
         this.status = status;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public static SkillRegisterStatus getStatus(int status) {
    	 SkillRegisterStatus s = null;
         switch (status) {
             case 0:
                 s = REGISTERED;
                 break;
             case 1:
                 s = SUCCESS;
                 break;
             case 2:
                 s = CANCEL;
                 break;
             case 3:
                 s = FAIL;
                 break;
             default:
                 s = REGISTERED;
                 break;
         }
         return s;
     }
}
