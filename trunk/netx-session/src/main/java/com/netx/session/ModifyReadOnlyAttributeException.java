package com.netx.session;


public class ModifyReadOnlyAttributeException extends RuntimeException {
	
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;
	
    public ModifyReadOnlyAttributeException() {
    }

    public ModifyReadOnlyAttributeException(String message) {
        super(message);
    }

    public ModifyReadOnlyAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModifyReadOnlyAttributeException(Throwable cause) {
        super(cause);
    }
}
