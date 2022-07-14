package com.jxrisesun.rstool.core.exception;

/**
 * 业务异常
 * 
 * @author zhangzl
 */
public final class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	private Integer code;
	
	public Integer getCode() {
		return this.code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 错误提示
	 */
	private String message;
	
	public String getMessage() {
		return this.message;
	}
	
	public ServiceException setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * 错误明细，内部调试错误
	 *
	 */
	private String detailMessage;
	
	public String getDetailMessage() {
		return this.detailMessage;
	}
	
	public ServiceException setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
		return this;
	}

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public ServiceException() {
	}

	public ServiceException(String message) {
		this.message = message;
	}

	public ServiceException(String message, Integer code) {
		this.message = message;
		this.code = code;
	}
}