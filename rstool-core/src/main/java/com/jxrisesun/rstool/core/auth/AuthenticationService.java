package com.jxrisesun.rstool.core.auth;

import java.util.Collection;

/**
 * 认证服务接口
 * @author zhangzl
 *
 */
public interface AuthenticationService {

	/**
	 * 获取用户信息
	 * @param authenticationToken
	 * @return
	 */
	AuthenticationUser getUser(AuthenticationToken authenticationToken);
	
	/**
	 * 获取角色集合
	 * @param authenticationUser
	 * @return
	 */
	Collection<String> getRoles(AuthenticationUser authenticationUser); 
	
	/**
	 * 获取权限集合
	 * @param authenticationUser
	 * @return
	 */
	Collection<String> getPermissions(AuthenticationUser authenticationUser);
}
