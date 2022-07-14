package com.jxrisesun.rstool.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 * 
 * @author zhangzl
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
	
	/**
	 * 用户表的别名
	 */
	public String userAlias() default "";
	
	/**
	 * 部门表的别名
	 */
	public String deptAlias() default "";
	
	/**
	 * 机构表的别名
	 * @return
	 */
	public String orgAlias() default "";
}