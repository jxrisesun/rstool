package com.jxrisesun.rstool.shiro.bean;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.jxrisesun.rstool.shiro.ShiroRealm;
import com.jxrisesun.rstool.shiro.ShiroWebSessionManager;
import com.jxrisesun.rstool.shiro.config.ShiroConfigInterface;
import com.jxrisesun.rstool.shiro.config.ShiroConfigInterfaceImpl;
import com.jxrisesun.rstool.shiro.redis.IRedisManager;
import com.jxrisesun.rstool.shiro.redis.RedisCacheManager;
import com.jxrisesun.rstool.shiro.redis.RedisManager;
import com.jxrisesun.rstool.shiro.redis.RedisSessionDAO;
import com.jxrisesun.rstool.spring.util.RedisUtils;

public abstract class ShiroBeanInterface {
	
	protected IRedisManager redisManager;
	
	public IRedisManager getRedisManager() {
		if(this.redisManager == null && RedisUtils.getRedisConnectionFactory() != null) {
			this.redisManager = new RedisManager();
		}
		return this.redisManager;
	}
	
	/** authorizingRealm */
	protected AuthorizingRealm authorizingRealm;
	
	public AuthorizingRealm getAuthorizingRealm() {
		if(this.authorizingRealm == null) {
			this.authorizingRealm = new ShiroRealm();
			this.authorizingRealm.setCachingEnabled(true);
			// 设置缓存管理器
			CacheManager newCacheManager = this.getCacheManager();
			if(newCacheManager != null) {
				this.authorizingRealm.setCacheManager(this.getCacheManager());
			}
			// 启用认证缓存
			this.authorizingRealm.setAuthenticationCachingEnabled(true);
			this.authorizingRealm.setAuthenticationCacheName(this.getShiroConfigInterface().getAuthenticationCacheName());
			// 启用授权缓存
			this.authorizingRealm.setAuthorizationCachingEnabled(true);
			this.authorizingRealm.setAuthorizationCacheName(this.getShiroConfigInterface().getAuthorizationCacheName());
		}
		return this.authorizingRealm;
	}
	
	/** cacheManager */
	protected CacheManager cacheManager;

	public CacheManager getCacheManager() {
		if(this.cacheManager == null) {
			if(this.getRedisManager() != null) {
				RedisCacheManager redisCacheManager = new RedisCacheManager();
				redisCacheManager.setRedisManager(this.getRedisManager());
				redisCacheManager.setExpire(this.getShiroConfigInterface().getRedisCacheTimeout());
				redisCacheManager.setPrincipalIdFieldName(this.getShiroConfigInterface().getPrincipalIdFieldName());
				this.cacheManager = redisCacheManager;
			}
		}
		return this.cacheManager;
	}
	
	/** sessionDAO */
	protected SessionDAO sessionDAO;

	public SessionDAO getSessionDAO() {
		if(this.sessionDAO == null) {
			if(this.getRedisManager() != null) {
				RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
				redisSessionDAO.setRedisManager(this.getRedisManager());
				// session在redis中的保存时间,最好大于session会话超时时间
				// redis session 过期时间，比 sessionTimeout 大1分钟
				redisSessionDAO.setExpire(this.getShiroConfigInterface().getSessionIdTimeout() + 60);
				//session存储值频繁变动时，此处禁用内存缓存
				redisSessionDAO.setSessionInMemoryEnabled(false);
				// 自定义会话id生成器
				SessionIdGenerator sessionIdGenerator = this.getSessionIdGenerator();
				if(sessionIdGenerator != null) {
					redisSessionDAO.setSessionIdGenerator(sessionIdGenerator);
				}
				this.sessionDAO = redisSessionDAO;
			}
		}
		return this.sessionDAO;
	}
	
	public SessionIdGenerator getSessionIdGenerator() {
		return null;
	}
	
	/** sessionIdCookie */
	protected Cookie sessionIdCookie;
	
	protected Cookie getSessionIdCookie() {
		if(this.sessionIdCookie == null) {
			this.sessionIdCookie = new SimpleCookie();
			this.sessionIdCookie.setName(this.getShiroConfigInterface().getSessionIdCookieName());
			this.sessionIdCookie.setPath("/");
			this.sessionIdCookie.setHttpOnly(true);
			this.sessionIdCookie.setMaxAge(this.getShiroConfigInterface().getSessionIdTimeout());
		}
		return this.sessionIdCookie;
	}


	/** defaultWebSessionManager */
	protected DefaultWebSessionManager sessionManager;
	
	public DefaultWebSessionManager getSessionManager() {
		if(this.sessionManager == null) {
			this.sessionManager = new ShiroWebSessionManager();
			// 设置缓存管理
			CacheManager newCacheManager = this.getCacheManager();
			if(newCacheManager != null) {
				this.sessionManager.setCacheManager(newCacheManager);
			}
			
			// 设置会话访问
			SessionDAO newSessionDAO = this.getSessionDAO();
			if(newSessionDAO != null) {
				this.sessionManager.setSessionDAO(newSessionDAO);
			}
			
			//sessionIdCookie
			if(this.getSessionIdCookie() != null) {
				this.sessionManager.setSessionIdCookieEnabled(true);
				this.sessionManager.setSessionIdCookie(this.getSessionIdCookie());
				this.sessionManager.setSessionIdUrlRewritingEnabled(false);//去掉URL中的JSESSIONID
			}

			// 全局的session会话超时时间，单位为毫秒
			this.sessionManager.setGlobalSessionTimeout(this.getShiroConfigInterface().getGlobalSessionTimeout() * 1000L);
			
			// session的失效扫描间隔，单位为毫秒
			this.sessionManager.setSessionValidationInterval(this.getShiroConfigInterface().getSessionValidationInterval() * 1000L);
			
			// 无效的Session定时调度器
			ExecutorServiceSessionValidationScheduler validationScheduler = new ExecutorServiceSessionValidationScheduler(this.sessionManager);
	        validationScheduler.setInterval(this.getShiroConfigInterface().getSessionValidationInterval() * 1000L);
	        this.sessionManager.setSessionValidationScheduler(validationScheduler);
	        this.sessionManager.setSessionValidationSchedulerEnabled(true);
			
			// 删除所有无效的Session对象
	        this.sessionManager.setDeleteInvalidSessions(true);
	        
	        // 会话监听器
	        Collection<SessionListener> sessionListeners = this.getSessionListeners();
	        if(sessionListeners != null) {
	        	this.sessionManager.setSessionListeners(sessionListeners);
	        }
		}
		return this.sessionManager;
	}
	
	/**
	 * 获取会话监听器
	 * @return
	 */
	public Collection<SessionListener> getSessionListeners() {
		return null;
	}
	
	/** securityManager */
	protected org.apache.shiro.mgt.SecurityManager securityManager;

	public org.apache.shiro.mgt.SecurityManager getSecurityManager() {
		if(this.securityManager == null) {
			DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
			defaultWebSecurityManager.setRealm(this.getAuthorizingRealm());
			// 设置缓存管理器
			CacheManager newCacheManager = this.getCacheManager();
			if(newCacheManager != null) {
				defaultWebSecurityManager.setCacheManager(newCacheManager);
			}
			defaultWebSecurityManager.setSessionManager(this.getSessionManager());
			this.securityManager = defaultWebSecurityManager;
		}
		return this.securityManager;
	}
	
	/** defaultAdvisorAutoProxyCreator */
	protected DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator;

	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		if(this.defaultAdvisorAutoProxyCreator == null) {
			this.defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
			this.defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		}
		return this.defaultAdvisorAutoProxyCreator;
	}
	
	/** authorizationAttributeSourceAdvisor */
	protected AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor;
	
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		if(this.authorizationAttributeSourceAdvisor == null) {
			this.authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
			this.authorizationAttributeSourceAdvisor.setSecurityManager(this.getSecurityManager());
		}
		return authorizationAttributeSourceAdvisor;
	}
	
	protected DelegatingFilterProxy delegatingFilterProxy;
	
	public DelegatingFilterProxy getDelegatingFilterProxy() {
		if(this.delegatingFilterProxy == null) {
			this.delegatingFilterProxy = new DelegatingFilterProxy();
			this.delegatingFilterProxy.setTargetFilterLifecycle(true);
		}
		return this.delegatingFilterProxy;
	}
	
	protected LifecycleBeanPostProcessor lifecycleBeanPostProcessor;
	
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		if(this.lifecycleBeanPostProcessor == null) {
			this.lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
		}
		return this.lifecycleBeanPostProcessor;
	}
	
	protected ShiroFilterFactoryBean shiroFilterFactoryBean;
	
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		if(this.shiroFilterFactoryBean == null) {
			this.shiroFilterFactoryBean = new ShiroFilterFactoryBean();
			this.shiroFilterFactoryBean.setSecurityManager(this.getSecurityManager());
			this.shiroFilterFactoryBean.setFilters(this.getShiroFilters());
			this.shiroFilterFactoryBean.setFilterChainDefinitionMap(this.getShiroFilterChainDefinitionMap());
		}
		return this.shiroFilterFactoryBean;
	}
	
	/**
	 * 获取 shiro过滤器集合
	 * @return
	 */
	public Map<String, Filter> getShiroFilters() {
		return new LinkedHashMap<>();
	}
	
	/**
	 * 获取shiro过滤器规则定义
	 * @return
	 */
	public Map<String, String> getShiroFilterChainDefinitionMap() {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/**", "anon");
		return filterChainDefinitionMap;
	}
	
	/**
	 * shiro 配置接口
	 */
	protected ShiroConfigInterface shiroConfigInterface;
	
	public ShiroConfigInterface getShiroConfigInterface() {
		if(this.shiroConfigInterface == null) {
			this.shiroConfigInterface = new ShiroConfigInterfaceImpl();
		}
		return this.shiroConfigInterface;
	}
}
