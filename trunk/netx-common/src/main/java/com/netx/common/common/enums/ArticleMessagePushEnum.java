package com.netx.common.common.enums;

public enum ArticleMessagePushEnum {

    DELETE_AND_RELEASE_CREDIT(1,"你发布的{0}信息因内容不合规，已被系统删除，你的信用值已被扣减5分"),
    COMMAND_UPDATE(2,"你发布的{0}信息因内容不合格，请修改后重新发布");

    private Integer code;
    private String msg;

    ArticleMessagePushEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
