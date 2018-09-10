package com.netx.worth.enums;

public enum InvitationStatus {
	 SEND(1, "发出邀请"),
     ACCEPT(2, "接受邀请"),
     REFUSE(3, "拒绝邀请"),
     CANCEL(4, "超时取消，目前由用户自己拒绝");;
     public Integer status;
     private String description;

     private InvitationStatus(Integer status, String description) {
         this.status = status;
         this.description = description;
     }
}
