package com.netx.session;


public class SessionConfigParseException extends RuntimeException {
	
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;
	
    public SessionConfigParseException() {
    }

    public SessionConfigParseException(String message) {
        super(message);
    }

    public SessionConfigParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionConfigParseException(Throwable cause) {
        super(cause);
    }
}
