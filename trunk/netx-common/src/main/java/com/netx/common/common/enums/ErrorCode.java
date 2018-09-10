package com.netx.common.common.enums;

/**
 * Create by wongloong on 17-8-22
 */
public enum ErrorCode {
    ERROR_FILE_UPLOAD("1000", "上传文件错误"),
    ERROR_VALIDATION_FAIL("1100", "参数校验不通过"),
     ERROR_OPERATION_FAILD("1200", "操作失败"),
    ERROR_BUSINESS_FAILD("1300", "业务失败"),
    ERROR_SERVER_ERROR("9999", "服务器异常"),
    ERROR_UPDATE_OBJECT("2224", "找不到更新的对象"),

    ERROR_COSTSETTING_HAS_NOAUDIT("2223","存在未审核的费用设置"),
    ERROR_EXAMINEFINANCE_HAS_NOAUDIT("2224","存在未审核的财务记录"),
    ERROR_AREA_HAS_CHILDREN("2222", "当前区域有子区域,删除失败");
    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
