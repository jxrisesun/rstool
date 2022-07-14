package com.jxrisesun.rstool.core.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 认证用户信息
 * @author zhangzl
 *
 */
public class AuthenticationUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6180589330638601935L;
	
	/**
	 * 用户所属应用
	 */
	private String userApp;
	
	public String getUserApp() {
		return this.userApp;
	}
	
	public void setUserApp(String userApp) {
		this.userApp = userApp;
	}

	/**
	 * 用户id
	 */
	private String userId;
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户名
	 */
	private String username;
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 真实姓名
	 */
	private String realname;
	
	public String getRealname() {
		return this.realname;
	}
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	/**
	 * 用户类型
	 */
	private String userType;
	
	public String getUserType() {
		return this.userType;
	}
	
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	 * 机构id
	 */
	private String orgId;
	
	public String getOrgId() {
		return this.orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 机构(单位)名称
	 */
	private String orgName;
	
	public String getOrgName() {
		return this.orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	/**
	 * 部门(科室)id
	 */
	private String deptId;
	
	public String getDeptId() {
		return this.deptId;
	}
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	/**
	 * 部门(科室)名称
	 */
	private String deptName;
	
	public String getDeptName() {
		return this.deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	/**
	 * 角色集合
	 */
	private Set<String> roles;
	
	public Set<String> getRoles() {
		if(this.roles == null) {
			this.roles = new HashSet<>();
		}
		return this.roles;
	}
	
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	/**
	 * 权限集合
	 */
	private Set<String> permissions;
	
	public Set<String> getPermissions() {
		if(this.permissions == null) {
			this.permissions = new HashSet<>();
		}
		return this.permissions;
	}
	
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
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
	
	/**
	 * token 信息
	 */
	private AuthenticationToken token;
	
	public AuthenticationToken getToken() {
		return this.token;
	}
	
	public void setToken(AuthenticationToken token) {
		this.token = token;
	}
}
