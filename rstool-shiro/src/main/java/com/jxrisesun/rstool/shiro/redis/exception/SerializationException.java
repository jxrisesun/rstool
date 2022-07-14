package com.jxrisesun.rstool.shiro.redis.exception;

public class SerializationException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4539584075106563496L;
	
	public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
