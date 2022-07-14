package com.jxrisesun.rstool.shiro.config;

/**
 * shiro 接口声明
 * @author zhangzl
 *
 */
public interface ShiroConfigInterface {
	
	/**
	 * 获取会话id cookie名称
	 * @return
	 */
	String getSessionIdCookieName();
	
	/**
	 * 会话id超时时间(秒)
	 * @return
	 */
	int getSessionIdTimeout();
	
	/**
	 * 获取全局会话超时时间(秒) 
	 * @return
	 */
	int getGlobalSessionTimeout();
	
	/**
	 * 获取会话验证定时周期(秒)
	 * @return
	 */
	int getSessionValidationInterval();
	
	/**
	 * 获取redis会话超时时间(秒)
	 * @return
	 */
	int getRedisSessionTimeout();
	
	/**
	 * 获取redis 缓存超时时间(秒) 
	 * @return
	 */
	int getRedisCacheTimeout();
	
	String getPrincipalIdFieldName();
	
	String getAuthenticationCacheName();
	
	String getAuthorizationCacheName();
	
	/**
	 * 获取 token 应用
	 * @return
	 */
	String getTokenApp();
	
	/**
	 * 获取 token名称
	 * @return
	 */
	String getTokenName();
	
	/**
	 * 获取token有效时间(秒)
	 * @return
	 */
	int getTokenTimeout();
	
	/**
	 * 获取token生成密钥
	 * @return
	 */
	String getTokenSecret();
}
