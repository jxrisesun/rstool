package com.jxrisesun.rstool.core.util;

import java.lang.reflect.AccessibleObject;
import java.net.URL;

/**
 * 反射工具类
 * @author zhangzl
 *
 */
public class ReflectUtils {

	/**
     * The package separator character: {@code '&#x2e;' == {@value}}.
     */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    
	/**
	 * 获得对象数组的类数组
	 *
	 * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		Object obj;
		for (int i = 0; i < objects.length; i++) {
			obj = objects[i];
			classes[i] = (null == obj) ? Object.class : obj.getClass();
		}
		return classes;
	}
	
	// ----------------------------------- Begin Package
	
	/**
     * <p>Gets the package name of an {@code Object}.</p>
     *
     * @param object  the class to get the package name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the package name of the object, or the null value
     */
    public static String getPackageName(final Object object, final String valueIfNull) {
        if (null == object) {
            return valueIfNull;
        }
        return getPackageName(object.getClass());
    }
    
    /**
     * <p>Gets the package name of a {@code Class}.</p>
     *
     * @param cls  the class to get the package name for, may be {@code null}.
     * @return the package name or an empty string
     */
    public static String getPackageName(final Class<?> cls) {
        if (null == cls) {
            return StringUtils.STRING_EMPTY;
        }
        return getPackageName(cls.getName());
    }
    
    /**
     * <p>Gets the package name from a {@code String}.</p>
     *
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     * <p>If the class is unpackaged, return an empty string.</p>
     *
     * @param className  the className to get the package name for, may be {@code null}
     * @return the package name or an empty string
     */
    public static String getPackageName(String className) {
        if (StringUtils.isEmpty(className)) {
            return StringUtils.STRING_EMPTY;
        }

        // Strip array encoding
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        // Strip Object type encoding
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }

        final int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1) {
            return StringUtils.STRING_EMPTY;
        }
        return className.substring(0, i);
    }
    
    /**
	 * 获取当前线程的类加载器
	 * @return
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 获取类加载器
	 * 获取优先级(1、当前线程,2、当前class,3、系统)
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if (classLoader == null) {
			classLoader = ReflectUtils.class.getClassLoader();
			if (null == classLoader) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}
	
	/**
	 * 获得资源的URL(路径用/分隔)
	 * @param resource
	 * @return
	 */
	public static URL getResource(String resource) {
		return getResource(resource, null);
	}
	
	/**
	 * 获得资源相对路径对应的URL
	 * @param resource 资源相对路径，{@code null}和""都表示classpath根路径
	 * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
	 * @return
	 */
	public static URL getResource(String resource, Class<?> baseClass) {
		if(resource == null) {
			resource = StringUtils.STRING_EMPTY;
		}
		if(baseClass != null) {
			return baseClass.getResource(resource);
		} else {
			return getClassLoader().getResource(resource);
		}
	}
	
	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 */
	public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
		if (null != accessibleObject && !accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}
}
