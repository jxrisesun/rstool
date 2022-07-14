package com.jxrisesun.rstool.shiro.config;

import com.jxrisesun.rstool.spring.util.SpringUtils;

/**
 * 默认 shiro 接口实现
 * @author zhangzl
 *
 */
public class ShiroConfigInterfaceImpl implements ShiroConfigInterface {

	private static final String DEFAULT_SESSION_IDCOOKIE = "SESSIONID";
	
	/** sessionTimeout */
	private static final int DEFAULT_SESSION_TIMEOUT = 30 * 60;
	
	/** 默认token应用 */
	public static final String DEFAULT_TOKEN_APP = "app";
	
	/** 默认token名称 */
	private static final String DEFAULT_TOKEN_NAME = "accessToken";
	
	/** 默认token有效期 */
	private static final int DEFAULT_TOKEN_TIMEOUT = 7 * 24 * 60 * 60;
	
	/** 默认token生成密钥 */
	private static final String DEFAULT_TOKEN_SECRET = "tokenSecret";
	
	public static final String CACHE_AUTHENTICATIONCACHE = "authenticationCache";
	
	public static final String CACHE_AUTHORIZATIONCACHE = "authorizationCache";
	
	private static ShiroConfigInterface instance;
	
	/**
	 * 获取实例
	 * @return
	 */
	public static ShiroConfigInterface getInstance() {
		if(instance == null) {
			instance = SpringUtils.getBean(ShiroConfigInterface.class);
		}
		if(instance == null) {
			instance = new ShiroConfigInterfaceImpl();
		}
		return instance;
	}
	
	/** 会话id cookie名称 */
	protected String sessionIdCookieName = DEFAULT_SESSION_IDCOOKIE;
	
	@Override
	public String getSessionIdCookieName() {
		return this.sessionIdCookieName;
	}
	
	public void setSessionIdCookieName(String sessionIdCookieName) {
		this.sessionIdCookieName = sessionIdCookieName;
	}
	
	/** 会话id超时时间(秒)  */
	protected int sessionIdTimeout = DEFAULT_SESSION_TIMEOUT;
	
	public int getSessionIdTimeout() {
		return this.sessionIdTimeout;
	}
	
	public void setSessionIdTimeout(int sessionIdTimeout) {
		this.sessionIdTimeout = sessionIdTimeout;
	}
	
	/** 全局会话超时时间(秒) */
	protected int globalSessionTimeout = DEFAULT_SESSION_TIMEOUT;
	
	public int getGlobalSessionTimeout() {
		return this.globalSessionTimeout;
	}
	
	public void setGlobalSessionTimeout(int globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}
	
	/** 会话验证定时周期(秒) */
	protected int sessionValidationInterval = DEFAULT_SESSION_TIMEOUT / 2;
	
	public int getSessionValidationInterval() {
		return this.sessionValidationInterval;
	}
	
	public void setSessionValidationInterval(int sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}
	
	/** redis会话超时时间(秒)  */
	protected int redisSessionTimeout = DEFAULT_SESSION_TIMEOUT + 60;
	
	public int getRedisSessionTimeout() {
		return this.redisSessionTimeout;
	}
	
	public void setRedisSessionTimeout(int redisSessionTimeout) {
		this.redisSessionTimeout = redisSessionTimeout;
	}
	
	/** redis 缓存超时时间(秒) */
	protected int redisCacheTimeout = DEFAULT_SESSION_TIMEOUT;
	
	public int getRedisCacheTimeout() {
		return this.redisCacheTimeout;
	}
	
	public void setRedisCacheTimeout(int redisCacheTimeout) {
		this.redisCacheTimeout = redisCacheTimeout;
	}
	
	protected String principalIdFieldName = "id";
	
	@Override
	public String getPrincipalIdFieldName() {
		return this.principalIdFieldName;
	}
	
	protected String authenticationCacheName = CACHE_AUTHENTICATIONCACHE;
	
	@Override
	public String getAuthenticationCacheName() {
		return this.authenticationCacheName;
	}
	
	protected String authorizationCacheName = CACHE_AUTHORIZATIONCACHE;
	
	@Override
	public String getAuthorizationCacheName() {
		return this.authorizationCacheName;
	}
	
	/**
	 * token 应用
	 */
	protected String tokenApp = DEFAULT_TOKEN_APP;
	
	public String getTokenApp() {
		return this.tokenApp;
	}
	
	public void setTokenApp(String tokenApp) {
		this.tokenApp = tokenApp;
	}
	
	/**
	 * token 名称
	 */
	protected String tokenName = DEFAULT_TOKEN_NAME;
	
	public String getTokenName() {
		return this.tokenName;
	}
	
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	/**
	 * token 有效时间(秒)
	 */
	protected int tokenTimeout = DEFAULT_TOKEN_TIMEOUT;
	
	public int getTokenTimeout() {
		return this.tokenTimeout;
	}
	
	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}
	
	/**
	 * token 生成密钥
	 */
	protected String tokenSecret = DEFAULT_TOKEN_SECRET;
	
	public String getTokenSecret() {
		return this.tokenSecret;
	}
	
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
}
