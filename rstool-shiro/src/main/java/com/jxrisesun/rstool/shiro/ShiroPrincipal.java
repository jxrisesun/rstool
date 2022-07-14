package com.jxrisesun.rstool.shiro;

import com.jxrisesun.rstool.core.auth.AuthenticationUser;
import com.jxrisesun.rstool.core.util.StringUtils;

/**
 * shiro 认证主体信息
 * @author zhangzl
 *
 */
public class ShiroPrincipal extends AuthenticationUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6180589330638601935L;
	
	private String cacheKey;
	
	public String getCacheKey() {
		if(this.cacheKey == null && this.getToken() instanceof ShiroToken) {
			this.cacheKey = StringUtils.format("{}:{}:{}", this.getUserApp(), this.getUserId(), ((ShiroToken)this.getToken()).getCacheKey());
		}
		return this.cacheKey;
	}
	
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
