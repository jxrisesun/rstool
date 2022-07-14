package com.jxrisesun.rstool.core.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.jxrisesun.rstool.core.lang.FuncR;

/**
 * 简单缓存，无超时实现，使用{@link WeakHashMap}实现缓存自动清理
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class SimpleCache<K,V> {
	
	/** 池 */
	private final Map<K, V> cache = new WeakHashMap<>();
	
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	private final ReadLock readLock = readWriteLock.readLock();
	
	private final WriteLock writeLock = readWriteLock.writeLock();
	
	/**
	 * 从缓存池中查找值
	 * 
	 * @param key 键
	 * @return 值
	 */
	public V get(K key) {
		// 尝试读取缓存
		this.readLock.lock();
		V value;
		try {
			value = this.cache.get(key);
		} finally {
			this.readLock.unlock();
		}
		return value;
	}
	
	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回Func0回调产生的对象
	 * 
	 * @param key 键
	 * @param supplier 如果不存在回调方法，用于生产值对象
	 * @return 值对象
	 */
	public V get(K key, FuncR<V> supplier) {
		V v = get(key);
		if (null == v && null != supplier) {
			this.writeLock.lock();
			try {
				// 双重检查锁
				v = this.cache.get(key);
				if(null == v) {
					try {
						v = supplier.call();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					this.cache.put(key, v);
				}
			} finally {
				this.writeLock.unlock();
			}
		}
		return v;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @return 值
	 */
	public V put(K key, V value){
		this.writeLock.lock();
		try {
			this.cache.put(key, value);
		} finally {
			this.writeLock.unlock();
		}
		return value;
	}

	/**
	 * 移除缓存
	 * 
	 * @param key 键
	 * @return 移除的值
	 */
	public V remove(K key) {
		this.writeLock.lock();
		try {
			return this.cache.remove(key);
		} finally {
			this.writeLock.unlock();
		}
	}

	/**
	 * 清空缓存池
	 */
	public void clear() {
		this.writeLock.lock();
		try {
			this.cache.clear();
		} finally {
			this.writeLock.unlock();
		}
	}
}
