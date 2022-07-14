package com.jxrisesun.rstool.core.util;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class FastJson2Utils {

	public static JSONObject parseObject(String text) {
		return JSON.parseObject(text);
	}
	
	public static JSONArray parseArray(String text) {
		return JSON.parseArray(text);
	}
	
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}
	
	public static <T> T parseObject(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}
	
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object);
	}
	
	public static String totring(Object object) {
		return JSON.toJSONString(object);
	}
}
