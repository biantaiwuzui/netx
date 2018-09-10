package com.netx.worth.enums;

public enum SkillStatus {
	 PUBLISHED(1, "已发布"),
     CANCEL(2, "已取消"),
     STOP(3, "已结束");

     public Integer status;
     public String description;

     private SkillStatus(Integer status, String description) {
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

     public static SkillStatus getStatus(int status) {
    	 SkillStatus s = null;
         switch (status) {
             case 1:
                 s = PUBLISHED;
                 break;
             case 2:
                 s = CANCEL;
                 break;
             case 3:
                 s = STOP;
                 break;
             default:
                 s = PUBLISHED;
                 break;
         }
         return s;
     }
}
