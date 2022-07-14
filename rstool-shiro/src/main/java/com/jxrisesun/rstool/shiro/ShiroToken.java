package com.jxrisesun.rstool.shiro;

import com.jxrisesun.rstool.core.auth.AuthenticationToken;
import com.jxrisesun.rstool.core.util.StringUtils;

/**
 * shiro token
 * @author zhangzl
 *
 */
public class ShiroToken extends AuthenticationToken implements org.apache.shiro.authc.AuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7593138366490731545L;
	
	public ShiroToken() {
		super();
	}
	
	public ShiroToken(String value) {
		super(value);
	}
	
	private Object principal;

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
	
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}

	private Object credentials;
	
	@Override
	public Object getCredentials() {
		if(this.credentials == null) {
			this.credentials = super.getTokenValue();
		}
		return this.credentials;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	
	private String cacheKey;
	
	public String getCacheKey() {
		if(this.cacheKey == null) {
			this.cacheKey = StringUtils.format("{}:{}:{}:{}", this.getTokenApp(), this.getTokenType(), this.getTokenDevice(), this.getTokenValue());
		}
		return this.cacheKey;
	}
	
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
