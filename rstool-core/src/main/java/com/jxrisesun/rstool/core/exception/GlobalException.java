package com.jxrisesun.rstool.core.exception;

/**
 * 全局异常
 * 
 * @author zhangzl
 */
public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误提示
	 */
	private String message;
	
	public String getMessage() {
		return this.message;
	}

	public GlobalException setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * 错误明细，内部调试错误
	 *
	 */
	private String detailMessage;
	
	public String getDetailMessage() {
		return detailMessage;
	}

	public GlobalException setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
		return this;
	}

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public GlobalException() {
	}

	public GlobalException(String message) {
		this.message = message;
	}
}