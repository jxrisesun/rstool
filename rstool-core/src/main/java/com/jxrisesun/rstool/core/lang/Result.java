package com.jxrisesun.rstool.core.lang;

/**
 * 结果接口
 * @author zhangzl
 *
 * @param <T>
 */
public interface Result<T> {

	int getCode();
	
	String getMsg();
	
	T getData();
	
	boolean isSuccess();
	
	boolean isFail();
}
