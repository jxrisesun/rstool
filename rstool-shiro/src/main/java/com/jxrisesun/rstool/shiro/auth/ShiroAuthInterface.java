package com.jxrisesun.rstool.shiro.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;

public interface ShiroAuthInterface {

	/**
	 * shiro认证
	 * @param realm
	 * @param token
	 * @return
	 */
	AuthenticationInfo doAuthenticate(AuthorizingRealm realm, AuthenticationToken token);
	
	/**
	 * shiro 授权
	 * @param principal shiro认证主体
	 */
	AuthorizationInfo doAuthorize(Object principal);
	
	/**
	 * 获取认证缓存token key
	 * @param token
	 * @return
	 */
	Object getAuthenticationTokenCacheKey(AuthenticationToken token);
	
	/**
	 * 获取认证缓存principal key
	 * @param token
	 * @return
	 */
	Object getAuthenticationPrincipalCacheKey(Object principal);
	
	/**
	 * 获取授权缓存key
	 * @param principal
	 * @return
	 */
	Object getAuthorizationCacheKey(Object principal);
}
