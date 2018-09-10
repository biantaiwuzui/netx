package com.netx.common.common.exception;

import com.netx.common.common.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientException是由客户端引起的异常
 * 比如输入非法参数或者用户登录输入错误的密码导致登录失败而业务逻辑进入到非主线分支
 * 这类问题其实都是客户端导致的，只需要拒绝服务让客户端提交正确的参数即可恢复。
 * 因此当遇到这种异常的时候，其实业务逻辑是执行成功的，只不过进入了非主线分支而已。
 *
 * @author chengl
 * @version 1.0
 * @Date 2011-8-17
 */
public class ClientException extends Exception {

    public Logger logger = LoggerFactory.getLogger(ClientException.class);

    private static final long serialVersionUID = -8443499461307209843L;


    private String errorCode = ErrorCode.ERROR_BUSINESS_FAILD.getCode();


    public ClientException() {
        super();
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String title, String message) {
        super(message);
    }

    public ClientException(String title, String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
