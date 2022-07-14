package com.jxrisesun.rstool.core.exception;

/**
 * 基础异常
 * 
 * @author zhangzl
 */
public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 所属模块
	 */
	private String module;
	
	public String getModule() {
		return this.module;
	}

	/**
	 * 错误码
	 */
	private String code;
	
	public String getCode() {
		return this.code;
	}

	/**
	 * 错误码对应的参数
	 */
	private Object[] args;
	
	public Object[] getArgs() {
		return this.args;
	}

	/**
	 * 错误消息
	 */
	private String defaultMessage;
	
	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	public BaseException(String module, String code, Object[] args, String defaultMessage) {
		this.module = module;
		this.code = code;
		this.args = args;
		this.defaultMessage = defaultMessage;
	}

	public BaseException(String module, String code, Object[] args) {
		this(module, code, args, null);
	}

	public BaseException(String module, String defaultMessage) {
		this(module, null, null, defaultMessage);
	}

	public BaseException(String code, Object[] args) {
		this(null, code, args, null);
	}

	public BaseException(String defaultMessage) {
		this(null, null, null, defaultMessage);
	}
}