package com.netx.session;

/**
 * when setting the unsupport info to gu.shoppingmall.session.session,this exception will be throwed.
 *
 */
public class UnSupportNameException extends RuntimeException {
	
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

    public UnSupportNameException() {

    }

    public UnSupportNameException(String message) {
        super(message);
    }

    public UnSupportNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportNameException(Throwable cause) {
        super(cause);
    }
}
