package com.jxrisesun.rstool.spring.util;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jxrisesun.rstool.core.util.StringUtils;

@Component
public class SpringUtils implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);
	
	public static final String REQUEST_METHOD_GET = "GET";
	
	public static final String REQUEST_METHOD_POST = "POST";
	
	public static final String REQUEST_SCHEME_HTTP = "http";
	
	public static final String REQUEST_SCHEME_HTTPS = "https";
	
	public static final int REQUEST_PORT_80 = 80;
	
	public static final int REQUEST_PORT_443 = 443;
	
	/**
	 * 当前 spring 应用上下文环境
	 */
	private static ApplicationContext applicationContext;
	
	public static ApplicationContext getApplicationContext() {
		if(applicationContext == null) {
			applicationContext = getWebApplicationContext(getRequestServletContext(null));
		}
		if(applicationContext == null) {
			applicationContext = ContextLoader.getCurrentWebApplicationContext();
		}
		return SpringUtils.applicationContext;
	}
	
	/** 当前 spring  环境*/
	private static Environment environment;
	
	public static Environment getEnvironment() {
		if(SpringUtils.environment == null) {
			ApplicationContext appContext = getApplicationContext();
			if(appContext != null) {
				SpringUtils.environment = appContext.getEnvironment();
			}
		}
		return SpringUtils.environment;
	}
	
	public static void setEnvironment(Environment environment) {
		SpringUtils.environment = environment;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
		SpringUtils.environment = applicationContext.getEnvironment();
	}
	
	/**
	 * 获取 spring webApplicationContext
	 * @return
	 */
	public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
		if(servletContext == null) {
			HttpServletRequest request = getRequest();
			if(request != null) {
				servletContext = request.getServletContext();
			}
		}
		if(servletContext != null) {
			return WebApplicationContextUtils.getWebApplicationContext(servletContext);
		}
		return ContextLoader.getCurrentWebApplicationContext();
	}
	
	/**
	 * 获取 bean 对象
	 * @param requiredType 请求的bean类型
	 * @param args 请求的bean参数
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType, Object... args) {
		try {
			if(args != null && args.length > 0) {
				return SpringUtils.getApplicationContext().getBean(requiredType, args);
			} else {
				return SpringUtils.getApplicationContext().getBean(requiredType);
			}
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param args 请求的bean参数
	 * @return
	 */
	public static Object getBean(String name, Object... args) {
		return SpringUtils.getApplicationContext().getBean(name, args);
	}
	
	/**
	 * 获取 bean 对象
	 * @param name 请求的bean名称
	 * @param requiredType 请求的bean类型
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return (T)SpringUtils.getApplicationContext().getBean(name, requiredType);
	}

	/**
	 * 动态注册bean
	 * @param <T>
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static <T> T registerBean(Class<T> clazz, Object... args) {
		return registerBean(clazz.getName(), clazz, args);
	}
	
	/**
	 * 动态注册bean
	 * @param <T>
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	public static <T> T registerBean(String name, Class<T> clazz, Object... args) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext)getApplicationContext();
		return registerBean(applicationContext, name, clazz, args);
		
	}
	/**
	 * 动态注册 bean
	 * @param <T>
	 * @param applicationContext
	 * @param name
	 * @param clazz
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, Object... args) {
		if(applicationContext.containsBean(name)) {
			Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T)bean;
            } else {
                throw new RuntimeException("beanName 重复 " + name);
            }
		}
		// beanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		if(args != null && args.length > 0) {
			for (Object arg : args) {
	            beanDefinitionBuilder.addConstructorArgValue(arg);
	        }
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		// beanDefinitionRegistry
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
		beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
		return applicationContext.getBean(name, clazz);
	}
	
	/**
	 * 获取web请求属性信息
	 * @return
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		try {
			RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
	        return (ServletRequestAttributes)attributes;
		} catch (Exception e) {
			LOGGER.trace(e.getMessage(), e);
			return null;
		}
    }
	
	/**
     * 获取web请求对象
     */
    public static HttpServletRequest getRequest() {
    	ServletRequestAttributes attributes = getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }
    
    /**
     * 获取web响应对象
     */
    public static HttpServletResponse getResponse() {
    	ServletRequestAttributes attributes = getRequestAttributes();
    	return attributes == null ? null : attributes.getResponse();
    }
    
	/**
	 * 获取 ServletContext
	 * @param request
	 * @return
	 */
	public static ServletContext getRequestServletContext(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		return request.getServletContext();
	}
    
	/**
	 * 获取 header map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new HashMap<>();
		}
		Map<String, String> headers = new LinkedHashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		return headers;
	}
	
	/**
	 * 获取请求 header
	 * @param request
	 * @param name header名称
	 * @return
	 */
	public static String getRequestHeader(HttpServletRequest request, String name) {
		return getRequestHeader(request, name, false);
	}
    
	/**
	 * 获取请求 header
	 * @param request
	 * @param headerName header名称
	 * @param ignoreCaseName 是否忽略header名称大小写
	 * @return
	 */
	public static String getRequestHeader(HttpServletRequest request, String headerName, boolean ignoreCaseName) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		String header = null;
		if(!ignoreCaseName) {
			header = request.getHeader(headerName);
		} else {
			Enumeration<String> names = request.getHeaderNames();
			String name = null;
			while (names.hasMoreElements()) {
				name = names.nextElement();
				if (name != null && name.equalsIgnoreCase(headerName)) {
					header = request.getHeader(name);
					break;
				}
			}
		}
		if(header == null) {
			return header;
		}
		header = header.trim();
		return header;
	}
	
	/**
	 * 设置响应的Header
	 * @param response
	 * @param name header名称
	 * @param value 值，可以是String，Date， int
	 */
	public static void setRequestHeader(HttpServletResponse response, String name, Object value) {
		if(response == null && (response = getResponse()) == null) {
			return;
		}
		if (value instanceof String) {
			response.setHeader(name, (String) value);
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			response.setDateHeader(name, ((Date) value).getTime());
		} else if (value instanceof Integer || "int".equals(value.getClass().getSimpleName().toLowerCase())) {
			response.setIntHeader(name, (Integer) value);
		} else {
			response.setHeader(name, value.toString());
		}
	}
	
	/**
	 * 获取 param map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestParameterMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new HashMap<>();
		}
		Map<String, String> params = new LinkedHashMap<>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			params.put(paramName, request.getParameter(paramName));
		}
		return params;
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @param name 参数名称
	 * @param charset 字符集
	 * @return
	 */
	public static String getRequestParameter(HttpServletRequest request, String name, String charset) {
		if(request == null && (request = getRequest()) == null) {
			return null;
		}
		String parameter = request.getParameter(name);
		if(parameter == null) {
			return parameter;
		}
		//字符集不匹配则自动转换
		if(charset != null && !charset.equalsIgnoreCase(request.getCharacterEncoding())) {
			String characterEncoding = request.getCharacterEncoding();
			try {
				parameter = new String(parameter.getBytes(characterEncoding), charset);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//清除首尾空白
		parameter = StringUtils.trim(parameter);
		return parameter;
	}
	
	/**
	 * 获取 cookie 集合
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getRequestCookieMap(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return new LinkedHashMap<>();
		}
		Map<String, Cookie> cookieMap = new LinkedHashMap<>();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return cookieMap;
		}
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie);
		}
		return cookieMap;
	}
	
	/**
	 * 获取 cookie 值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getRequestCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getRequestCookie(request, name);
		if(cookie == null) {
			return null;
		}
		String value = cookie.getValue();
		if(value != null) {
			value = value.trim();
		}
		return value;
	}
	
	/**
	 * 获取 cookie
	 * @param request
	 * @param name cookie 名称(忽略大小写)
	 * @return
	 */
	public static Cookie getRequestCookie(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = getRequestCookieMap(request);
		if(cookieMap.size() == 0) {
			return null;
		}
		for(Entry<String, Cookie> kv : cookieMap.entrySet()) {
			if(kv.getKey().equalsIgnoreCase(name)) {
				return kv.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param domain 域名
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. >0 : Cookie存在的秒数. 
	 */
	public static void setRequestCookie(HttpServletResponse response, String name, String value, String domain, int maxAgeInSeconds) {
		setRequestCookie(response, name, value, domain, "/", maxAgeInSeconds, false);
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param domain 域名
	 * @param path 路径
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. >0 : Cookie存在的秒数.
	 * @param isHttpOnly 
	 */
	public static void setRequestCookie(HttpServletResponse response, String name, String value, String domain, String path, int maxAgeInSeconds, boolean isHttpOnly) {
		if(response == null && (response = getResponse()) == null) {
			return;
		}
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if(path != null) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(isHttpOnly);
		response.addCookie(cookie);
	}
	
	/**
	 * 获取请求根路径
	 * @param request
	 * @return
	 */
	public static String getRequestBasePath(ServletRequest request){
		if(request == null && (request = getRequest()) == null) {
			return StringUtils.STRING_EMPTY;
		}
		StringBuilder str = new StringBuilder();
		str.append(request.getScheme());
		str.append("://");
		str.append(request.getServerName());
		if(request.getScheme().equalsIgnoreCase(REQUEST_SCHEME_HTTP) && request.getServerPort() == REQUEST_PORT_80) {
			str.append("");
		}else if(request.getScheme().equalsIgnoreCase(REQUEST_SCHEME_HTTPS) && request.getServerPort() == REQUEST_PORT_443) {
			str.append("");
		}else {
			str.append(":");
			str.append(request.getServerPort());
		}
		str.append(request.getServletContext().getContextPath());
		return str.toString(); 
	}
	
	/**
	 * 获取请求 servlet 路径
	 * @param request
	 * @return
	 */
	public static String getRequestServletPath(HttpServletRequest request) {
		if(request == null && (request = getRequest()) == null) {
			return StringUtils.STRING_EMPTY;
		}
		return request.getServletPath();
	}
}
