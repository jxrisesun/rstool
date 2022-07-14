package com.jxrisesun.rstool.core.lang;

import java.io.Serializable;

import com.jxrisesun.rstool.core.enums.HttpStatus;

/**
 * 返回结果信息
 *
 * @author zhangzl
 */
public class ApiResult<T> implements Result<T>, Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 成功 */
	public static int CODE_SUCCESS = HttpStatus.OK.getValue();

	/** 失败 */
	public static int CODE_FAIL = HttpStatus.INTERNAL_SERVER_ERROR.getValue();

	private int code;
	
	public int getCode() {
		return this.code;
	}

	public ApiResult<T> setCode(int code) {
		this.code = code;
		return this;
	}

	private String msg;
	
	public String getMsg() {
		return this.msg;
	}

	public ApiResult<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	private T data;
	
	public T getData() {
		return this.data;
	}

	public ApiResult<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	@Override
	public boolean isSuccess() {
		return this.code == CODE_SUCCESS;
	}
	
	@Override
	public boolean isFail() {
		return this.code == CODE_FAIL;
	}

	public static <T> ApiResult<T> ok() {
		return setResult(null, CODE_SUCCESS, null);
	}

	public static <T> ApiResult<T> ok(T data) {
		return setResult(data, CODE_SUCCESS, null);
	}

	public static <T> ApiResult<T> ok(T data, String msg) {
		return setResult(data, CODE_SUCCESS, msg);
	}

	public static <T> ApiResult<T> fail() {
		return setResult(null, CODE_FAIL, null);
	}

	public static <T> ApiResult<T> fail(String msg) {
		return setResult(null, CODE_FAIL, msg);
	}

	public static <T> ApiResult<T> fail(T data) {
		return setResult(data, CODE_FAIL, null);
	}

	public static <T> ApiResult<T> fail(T data, String msg) {
		return setResult(data, CODE_FAIL, msg);
	}

	public static <T> ApiResult<T> fail(int code, String msg) {
		return setResult(null, code, msg);
	}

	private static <T> ApiResult<T> setResult(T data, int code, String msg) {
		ApiResult<T> apiResult = new ApiResult<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}