package com.jxrisesun.rstool.spring.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.jxrisesun.rstool.core.html.HTMLFilter;

/**
 * XSS过滤处理
 * 
 * @author zhangzl
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	/**
	 * @param request
	 */
	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				// 防xss攻击和过滤前后空格
				escapseValues[i] = new HTMLFilter().filter(values[i]).trim();
			}
			return escapseValues;
		}
		return super.getParameterValues(name);
	}
}