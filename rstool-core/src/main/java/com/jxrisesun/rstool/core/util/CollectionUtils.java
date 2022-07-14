package com.jxrisesun.rstool.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxrisesun.rstool.core.lang.Filter;
import com.jxrisesun.rstool.core.lang.FuncP;
import com.jxrisesun.rstool.core.lang.Func;

/**
 * 集合工具类
 * @author zhangzl
 *
 */
public class CollectionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtils.class);
	
	/**
	 * 判断集合是否为空
	 * @param c
	 * @return
	 */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
    
    /**
     * 判断集合是否为非空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }
    
    /**
     * 判断Map是否为空
     * @param m
     * @return
     */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
    /**
     * 判断Map是否为非空
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return !isEmpty(m);
    }
    
    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    @SafeVarargs
    public static <T> boolean isEmpty(T... array) {
    	return array == null || array.length == 0;
    }
    
    /**
     * 判断数组是否为非空
     * @param array
     * @return
     */
    @SafeVarargs
	public static <T> boolean isNotEmpty(T... array) {
    	return !isEmpty(array);
    }
    
    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
    	return list == null || list.isEmpty();
	}
    
	/**
	 * 判断List是否为非空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}
	
	/**
	 * 判断 iterable 是否为空
	 * @param iterable
	 * @return
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return iterable == null || isEmpty(iterable.iterator());
	}
	
	/**
	 * 判断 iterable 是否为非空
	 * @param iterable
	 * @return
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return !isEmpty(iterable);
	}
	
	/**
	 * 判断 Iterator 是否为空
	 * @param iterator
	 * @return
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return iterator == null || !iterator.hasNext();
	}
	
	/**
	 * 判断 Iterator 是否为非空
	 * @param iterator
	 * @return
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return !isEmpty(iterator);
	}
	
	/**
	 * 判断 {@link Set} 是否为空
	 * @param set
	 * @return
	 */
	public static boolean isEmpty(Set<?> set) {
		return set == null || set.isEmpty();
	}
	
	/**
	 * 判断 {@link Set} 是否为非空
	 * @param set
	 * @return
	 */
	public static boolean isNotEmpty(Set<?> set) {
		return !isEmpty(set);
	}
	
	/**
	 * 是否包含{@code null}元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含{@code null}元素
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(T... array) {
		if (isNotEmpty(array)) {
			for (T element : array) {
				if (null == element) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 对象是否为数组对象
	 *
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		return null != obj && obj.getClass().isArray();
	}
	
	/**
	 * 新建一个空数组
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[])Array.newInstance(componentType, newSize);
	}
	
	/**
	 * 新建一个空的 {@link List}
	 * @param componentType
	 * @param newSize
	 * @return
	 */
	public static <T> List<T> newList(Class<?> componentType, int newSize){
		return new ArrayList<>(newSize);
	}
	
	/**
	 * 将多个数组合并在一起
	 * @param arrays 数组集合
	 * @return
	 */
	@SafeVarargs
	public static <T> T[] join(T[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}
		int length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			length += array.length;
		}
		T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);
		length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			System.arraycopy(array, 0, result, length, array.length);
			length += array.length;
		}
		return result;
	}
	
	/**
	 * 遍历集合
	 * @param <T>
	 * @param list
	 * @param func
	 */
	public static <T> void each(List<T> list, FuncP<T> func) {
		if(isEmpty(list)) {
			return;
		}
		for(T item : list) {
			if(func != null) {
				func.call(item);
			}
		}
	}
	
	/**
	 * 查找所有匹配的元素(多个)
	 * @param <T>
	 * @param list
	 * @param filter
	 * @return
	 */
	public static <T> List<T> findAll(List<T> list, Filter<T> filter) {
		if(isEmpty(list)) {
			return new ArrayList<>();
		}
		List<T> findList = new ArrayList<>();
		for(T item : list) {
			if(filter.accept(item)) {
				findList.add(item);
			}
		}
		return findList;
	}
	
	/**
	 * 获取集合首个元素
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> T first(Collection<T> list) {
		if(list != null) {
			Iterator<T> iterator = list.iterator();
			if(iterator.hasNext()) {
				return iterator.next();
			}
		}
		return null;
	}
	
	/**
	 * 转换为List
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @return
	 */
	public static <T, V> List<V> toList(Collection<T> list, Func<T, V> func) {
		return toList(list, func, false);
	}
	
	/**
	 * 转换为List
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @param allowNull
	 * @return
	 */
	public static <T, V> List<V> toList(Collection<T> list, Func<T, V> func, boolean allowNull) {
		if(isEmpty(list)) {
			return new ArrayList<>();
		}
		List<V> newList = new ArrayList<>();
		for (T item : list) {
			if(func != null) {
				V newItem = func.call(item);
				if(!allowNull && newItem == null) {
					continue;
				}
				newList.add(newItem);
			}
		}
		return newList;
	}
	
	/**
	 * 转换为Set
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @return
	 */
	public static <T, V> Set<V> toSet(Collection<T> list, Func<T, V> func) {
		return toSet(list, func, false);
	}
	
	/**
	 * 转换为Set
	 * @param <T>
	 * @param <V>
	 * @param list
	 * @param func
	 * @param allowNull
	 * @return
	 */
	public static <T, V> Set<V> toSet(Collection<T> list, Func<T, V> func, boolean allowNull) {
		if(isEmpty(list)) {
			return new HashSet<>();
		}
		Set<V> newList = new HashSet<>();
		for (T item : list) {
			if(func != null) {
				V newItem = func.call(item);
				if(!allowNull && newItem == null) {
					continue;
				}
				newList.add(newItem);
			}
		}
		return newList;
	}
	
	/**
	 * 数组或集合转String
	 *
	 * @param obj 集合或数组对象
	 * @return 数组字符串，与集合转字符串格式相同
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		if(!isArray(obj)) {
			return obj.toString();
		}
		if (obj instanceof long[]) {
			return Arrays.toString((long[]) obj);
		} else if (obj instanceof int[]) {
			return Arrays.toString((int[]) obj);
		} else if (obj instanceof short[]) {
			return Arrays.toString((short[]) obj);
		} else if (obj instanceof char[]) {
			return Arrays.toString((char[]) obj);
		} else if (obj instanceof byte[]) {
			return Arrays.toString((byte[]) obj);
		} else if (obj instanceof boolean[]) {
			return Arrays.toString((boolean[]) obj);
		} else if (obj instanceof float[]) {
			return Arrays.toString((float[]) obj);
		} else if (obj instanceof double[]) {
			return Arrays.toString((double[]) obj);
		} else if (CollectionUtils.isArray(obj)) {
			// 对象数组
			try {
				return Arrays.deepToString((Object[]) obj);
			} catch (Exception ignore) {
				LOGGER.warn(ignore.getMessage(), ignore);
			}
		}
		return obj.toString();
	}
}
