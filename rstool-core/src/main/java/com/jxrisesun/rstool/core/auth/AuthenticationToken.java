package com.jxrisesun.rstool.core.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证 token 信息
 * @author zhangzl
 *
 */
public class AuthenticationToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4335854132889553455L;
	
	/** 默认token应用 */
	public static final String DEFAULT_TOKEN_APP = "app";
	
	/** 默认token类型 */
	public static final String DEFAULT_TOKEN_TYPE = "login";
	
	/** 默认token设备 */
	public static final String DEFAULT_TOKEN_DEVICE = "pc";
	
	/** 默认token名称 */
	public static final String DEFAULT_TOKEN_NAME = "accessToken";
	
	/** 默认token超时时间(秒) */
	public static final int DEFAULT_TOKEN_TIMEOUT = 7 * 24 * 60 * 60;

	public AuthenticationToken() {
		
	}
	
	public AuthenticationToken(Object value) {
		this.setTokenValue(value);
	}
	
	/**
	 * token 应用
	 */
	private String tokenApp = DEFAULT_TOKEN_APP;
	
	public String getTokenApp() {
		return this.tokenApp;
	}
	
	public void setTokenApp(String tokenApp) {
		this.tokenApp = tokenApp;
	}
	
	/**
	 * token 类型
	 */
	private String tokenType = DEFAULT_TOKEN_TYPE;
	
	public String getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	/**
	 * token设备
	 */
	private String tokenDevice = DEFAULT_TOKEN_DEVICE;
	
	public String getTokenDevice() {
		return tokenDevice;
	}
	
	public void setTokenDevice(String tokenDevice) {
		this.tokenDevice = tokenDevice;
	}
	
	/**
	 * token 名称
	 */
	private String tokenName = DEFAULT_TOKEN_NAME;
	
	public String getTokenName() {
		return this.tokenName;
	}
	
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	/**
	 * token value
	 */
	private Object tokenValue;

	public Object getTokenValue() {
		return this.tokenValue;
	}
	
	public void setTokenValue(Object tokenValue) {
		this.tokenValue = tokenValue;
	}
	
	/**
	 * token 超时时间(秒)
	 */
	private int tokenTimeout = DEFAULT_TOKEN_TIMEOUT;
	
	public int getTokenTimeout() {
		return this.tokenTimeout;
	}
	
	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}
	
	/**
	 * token主题信息
	 */
	private String tokenSubject;
	
	public String getTokenSubject() {
		return tokenSubject;
	}
	
	public void setTokenSubject(String tokenSubject) {
		this.tokenSubject = tokenSubject;
	}
	
	/**
	 * 附加属性集合
	 */
	private Map<String, Object> attributes;
	
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Object getAttributeValue(String key) {
		return this.getAttributes().get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttributeValue(String key, Class<T> cls) {
		Object value = this.getAttributeValue(key);
		return (value != null && cls.isInstance(value)) ? (T)value : null;
	}
	
	public void setAttributeValue(String key, Object value) {
		this.getAttributes().put(key, value);
	}
}
