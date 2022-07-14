package com.jxrisesun.rstool.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * gson 工具类
 * @author zhangzl
 *
 */
public class GsonUtils {

	private static Gson gson;
	
	public static synchronized Gson getGson() {
		if(GsonUtils.gson == null) {
			GsonUtils.gson = new Gson();
		}
		return GsonUtils.gson;
	}
	
	public static synchronized void setGson(Gson gson) {
		GsonUtils.gson = gson;
	}
	
	/**
	 * json字符串转对象
	 * @param <T>
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> classOfT) {
		return getGson().fromJson(json, classOfT);
	}
	
	/**
	 * 对象转json字符串
	 * @param src
	 * @return
	 */
	public static String toJsonString(Object src) {
		return getGson().toJson(src);
	}
	
	/**
	 * 对象转json字符串
	 * @param src
	 * @return
	 */
	public static String toString(Object src) {
		return getGson().toJson(src);
	}
	
	/**
	 * 解析为json元素
	 * @param json
	 * @return
	 */
	public static JsonElement parseJsonElement(String json) {
		return JsonParser.parseString(json);
	}
	
	/**
	 * 解析为json对象
	 * @param json
	 * @return
	 */
	public static JsonObject parseJsonObject(String json) {
		return JsonParser.parseString(json).getAsJsonObject();
	}
	
	/**
	 * 解析为json数组
	 * @param json
	 * @return
	 */
	public static JsonArray parseJsonArray(String json) {
		return JsonParser.parseString(json).getAsJsonArray();
	}
}
