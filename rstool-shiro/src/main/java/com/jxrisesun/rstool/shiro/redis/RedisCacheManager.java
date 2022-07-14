package com.jxrisesun.rstool.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxrisesun.rstool.shiro.redis.serializer.ObjectSerializer;
import com.jxrisesun.rstool.shiro.redis.serializer.RedisSerializer;
import com.jxrisesun.rstool.shiro.redis.serializer.StringSerializer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RedisCacheManager implements CacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheManager.class);
	
	public static final int DEFAULT_EXPIRE = 1800;
	
	public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
	
	public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "id";

	// fast lookup by name map
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();
	
	private RedisSerializer keySerializer = new StringSerializer();
	
	private RedisSerializer valueSerializer = new ObjectSerializer();

	private IRedisManager redisManager;

	/** 默认有效期(秒) */
	private int expire = DEFAULT_EXPIRE;

	/** redis缓存key前缀 */
	private String keyPrefix = DEFAULT_CACHE_KEY_PREFIX;

	/** 认证主体字段名称 */
	private String principalIdFieldName = DEFAULT_PRINCIPAL_ID_FIELD_NAME;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		LOGGER.debug("get cache, name=" + name);

		Cache cache = caches.get(name);

		if (cache == null) {
			cache = new RedisCache<K, V>(redisManager, keySerializer, valueSerializer, keyPrefix + name + ":", expire, principalIdFieldName);
			caches.put(name, cache);
		}
		return cache;
	}

	public IRedisManager getRedisManager() {
		return this.redisManager;
	}

	public RedisCacheManager setRedisManager(IRedisManager redisManager) {
		this.redisManager = redisManager;
		return this;
	}

	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public RedisSerializer getKeySerializer() {
		return this.keySerializer;
	}

	public void setKeySerializer(RedisSerializer keySerializer) {
		this.keySerializer = keySerializer;
	}

	public RedisSerializer getValueSerializer() {
		return this.valueSerializer;
	}

	public void setValueSerializer(RedisSerializer valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public int getExpire() {
		return this.expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String getPrincipalIdFieldName() {
		return this.principalIdFieldName;
	}

	public void setPrincipalIdFieldName(String principalIdFieldName) {
		this.principalIdFieldName = principalIdFieldName;
	}
}
