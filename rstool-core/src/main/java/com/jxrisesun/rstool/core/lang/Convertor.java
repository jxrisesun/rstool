package com.jxrisesun.rstool.core.lang;

/**
 * 泛型通用转换回调
 * @author zhangzl
 *
 * @param <I> 输入类型
 * @param <O> 输出类型
 */
public interface Convertor<I, O> {

	/**
	 * 转换回调方法
	 * @param input
	 * @return
	 */
	O convert(I input);
}
