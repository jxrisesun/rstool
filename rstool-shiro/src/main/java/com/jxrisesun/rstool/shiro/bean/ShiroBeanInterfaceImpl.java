package com.jxrisesun.rstool.shiro.bean;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;

import com.jxrisesun.rstool.shiro.config.ShiroConfigInterface;
import com.jxrisesun.rstool.shiro.redis.IRedisManager;

public class ShiroBeanInterfaceImpl extends ShiroBeanInterface {

	@Bean
	public IRedisManager createRedisManager() {
		return this.getRedisManager();
	}

	@Bean
	public AuthorizingRealm createAuthorizingRealm() {
		return this.getAuthorizingRealm();
	}

	@Bean
	public CacheManager createCacheManager() {
		return this.getCacheManager();
	}
	
	@Bean
	public SessionDAO createSessionDAO() {
		return this.getSessionDAO();
	}
	
	@Bean
	public Cookie createSessionIdCookie() {
		return this.getSessionIdCookie();
	}
	
	@Bean
	public DefaultWebSessionManager createSessionManager() {
		return this.getSessionManager();
	}
	
	@Bean
	public org.apache.shiro.mgt.SecurityManager createSecurityManager() {
		return this.getSecurityManager();
	}
	
	@Bean
	public ShiroFilterFactoryBean createShiroFilterFactoryBean() {
		return this.getShiroFilterFactoryBean();
	}
	
	@Bean
	public ShiroConfigInterface createShiroConfigInterface() {
		return this.getShiroConfigInterface();
	}
	
	/*
	@Bean
	public DefaultAdvisorAutoProxyCreator createDefaultAdvisorAutoProxyCreator() {
		return this.getDefaultAdvisorAutoProxyCreator();
	}
	*/
	
	/*
	@Bean
	public AuthorizationAttributeSourceAdvisor createAuthorizationAttributeSourceAdvisor() {
		return this.getAuthorizationAttributeSourceAdvisor();
	}
	*/
	
	/*
	@Bean
	public DelegatingFilterProxy createDelegatingFilterProxy() {
		return this.getDelegatingFilterProxy();
	}
	*/
	
	/*
	@Bean
	public LifecycleBeanPostProcessor createLifecycleBeanPostProcessor() {
		return this.getLifecycleBeanPostProcessor();
	}
	*/
}
