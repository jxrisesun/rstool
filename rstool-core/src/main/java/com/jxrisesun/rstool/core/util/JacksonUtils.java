package com.jxrisesun.rstool.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson 工具类
 * @author zhangzl
 *
 */
public class JacksonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public static <T> T readValue(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (Exception e) {
			LogUtils.error(JacksonUtils.class, e.getMessage(), e);
			return null;
		}
	}
	
	public static String writeValueAsString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			LogUtils.error(JacksonUtils.class, e.getMessage(), e);
			return null;
		}
	}
	
	public static String toJsonString(Object obj) {
		return writeValueAsString(obj);
	}
}
