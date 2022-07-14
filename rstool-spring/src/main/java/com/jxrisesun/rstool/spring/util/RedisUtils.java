package com.jxrisesun.rstool.spring.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 工具类
 * @author zhangzl
 *
 */
public class RedisUtils {
	
	/** 常量，表示一个key永不过期 (在一个key被标注为永远不过期时返回此值) */ 
	public static final long NEVER_EXPIRE = -1;
	
	/** 常量，表示系统中不存在这个缓存 (在对不存在的key获取剩余存活时间时返回此值) */ 
	public static final long NOT_VALUE_EXPIRE = -2;
	
	/** redis key序列化缓存*/
	private static final ConcurrentHashMap<Class<?>, RedisSerializer<?>> RedisKeySerializerCache = new ConcurrentHashMap<>();
	
	/** redis value序列化缓存 */
	private static final ConcurrentHashMap<Class<?>, RedisSerializer<?>> RedisValueSerializerCache = new ConcurrentHashMap<>();
	
	/** redis 连接工厂 */
	private static RedisConnectionFactory redisConnectionFactory;
	
	/**
	 * 获取redis连接工厂
	 * @return
	 */
	public static RedisConnectionFactory getRedisConnectionFactory() {
		if(RedisUtils.redisConnectionFactory == null) {
			RedisUtils.redisConnectionFactory = SpringUtils.getBean(RedisConnectionFactory.class);
		}
		return RedisUtils.redisConnectionFactory;
	}
	
	public static void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
		RedisUtils.redisConnectionFactory = redisConnectionFactory;
	}
	
	/**
	 * 获取redis key 序列化对象
	 * @param <K>
	 * @param clsKey
	 * @return
	 */
	public static <K> RedisSerializer<?> getRedisKeySerializer(Class<K> clsKey) {
		if(clsKey != null) {
			if(RedisKeySerializerCache.containsKey(clsKey)) {
				return RedisKeySerializerCache.get(clsKey);
			}
			RedisSerializer<?> redisSerializer = null;
			if(clsKey == String.class) {
				redisSerializer = new StringRedisSerializer();
				RedisKeySerializerCache.put(clsKey, redisSerializer);
				return redisSerializer;
			} else if(clsKey == byte[].class) {
				redisSerializer = new RedisSerializer<byte[]>() {
					@Override
					public byte[] serialize(byte[] t) throws SerializationException {
						return t;
					}
					
					@Override
					public byte[] deserialize(byte[] bytes) throws SerializationException {
						return bytes;
					}
				};
				RedisKeySerializerCache.put(clsKey, redisSerializer);
				return redisSerializer;
			}
		}
		return new StringRedisSerializer();
	}
	
	public static void setRedisKeySerializer(Class<?> clazz, RedisSerializer<?> redisKeySerializer) {
		RedisKeySerializerCache.put(clazz, redisKeySerializer);
	}
	
	public static RedisSerializer<?> getRedisValueSerializer(Class<?> clsValue) {
		if(clsValue != null) {
			if(RedisValueSerializerCache.containsKey(clsValue)) {
				return RedisValueSerializerCache.get(clsValue);
			}
		}
		return new GenericJackson2JsonRedisSerializer();
	}
	
	public static void setRedisValueSerializer(Class<?> clazz, RedisSerializer<?> redisValueSerializer) {
		RedisValueSerializerCache.put(clazz, redisValueSerializer);
	}
	
	
	/**
	 * 创建 StringRedisTemplate
	 * @return
	 */
	public static StringRedisTemplate createStringRedisTemplate() {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(getRedisConnectionFactory());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
	public static <K, V> RedisTemplate<K, V> createRedisTemplate(Class<K> clsKey, Class<V> clsV) {
		return createRedisTemplate(getRedisConnectionFactory(), clsKey, clsV);
	}
	
	/**
	 * 创建 RedisTemplate
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> RedisTemplate<K, V> createRedisTemplate(RedisConnectionFactory redisConnectionFactory, Class<K> clsKey, Class<V> clsV) {
		RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(getRedisKeySerializer(clsKey));
		redisTemplate.setHashKeySerializer(getRedisKeySerializer(clsKey));
		redisTemplate.setValueSerializer(getRedisValueSerializer(clsV));
		redisTemplate.setHashValueSerializer(getRedisValueSerializer(clsV));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
	/**
	 * redis 获取 String值，如无返空
	 * @param stringRedisTemplate
	 * @param key
	 * @return
	 */
	public static String getString(StringRedisTemplate stringRedisTemplate, String key) {
		return get(stringRedisTemplate, key);
	}
	
	/**
	 * redis写入 String，并设定存活时间 (单位: 秒)
	 * @param stringRedisTemplate
	 * @param key
	 * @param value
	 * @param timeout
	 */
	public static void setString(StringRedisTemplate stringRedisTemplate, String key, String value, long timeout) {
		set(stringRedisTemplate, key, value, timeout);
	}
	
	/**
	 * redis修改指定 key-value键值对 (过期时间不变)
	 * @param stringRedisTemplate
	 * @param key
	 * @param value
	 */
	public static void updateString(StringRedisTemplate stringRedisTemplate, String key, String value) {
		update(stringRedisTemplate, key, value);
	}

	/**
	 * redis 获取Object值，如无返空
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public static Object getObject(RedisTemplate<String, Object> redisTemplate, String key) {
		return get(redisTemplate, key);
	}
	
	/**
	 * redis写入Object，并设定存活时间 (单位: 秒)
	 * @param redisTemplate
	 * @param key
	 * @param object
	 * @param timeout
	 */
	public static void setObject(RedisTemplate<String, Object> redisTemplate, String key, Object object, long timeout) {
		set(redisTemplate, key, object, timeout);
	}
	
	/**
	 * redis更新Object (过期时间不变)
	 * @param redisTemplate
	 * @param key
	 * @param object
	 */
	public static void updateObject(RedisTemplate<String, Object> redisTemplate, String key, Object object) {
		update(redisTemplate, key, object);
	}
	
	/**
	 * redis 获取字节数组
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public static byte[] getBytes(RedisTemplate<?, ?> redisTemplate, final byte[] key) {
		return redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key);
			}
		});
	}
	
	public static void setBytes(RedisTemplate<?, ?> redisTemplate, final byte[] key, final byte[] object, final long timeout) {
		if(timeout == 0 || timeout <= NOT_VALUE_EXPIRE)  {
			return;
		}
		redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				// 判断是否为永不过期 
				if(timeout == NEVER_EXPIRE) {
					connection.set(key, object);
				} else {
					connection.set(key, object, Expiration.seconds(timeout), SetOption.UPSERT);
				}
				return object;
			}
		});
	}
	
	/**
	 * redis 获取值
	 * @param <K>
	 * @param <V>
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public static <K, V> V get(RedisTemplate<K,V> redisTemplate, K key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * redis 写入 值
	 * @param <K>
	 * @param <V>
	 * @param redisTemplate
	 * @param key
	 * @param object
	 * @param timeout
	 */
	public static <K, V> void set(RedisTemplate<K, V> redisTemplate, K key, V object, long timeout) {
		if(timeout == 0 || timeout <= NOT_VALUE_EXPIRE)  {
			return;
		}
		// 判断是否为永不过期 
		if(timeout == NEVER_EXPIRE) {
			redisTemplate.opsForValue().set(key, object);
		} else {
			redisTemplate.opsForValue().set(key, object, timeout, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * redis 更新值 (过期时间不变)
	 * @param <K>
	 * @param <V>
	 * @param redisTemplate
	 * @param key
	 * @param object
	 */
	public static <K, V> void update(RedisTemplate<K, V> redisTemplate, K key, V object) {
		long expire = getExpire(redisTemplate, key);
		// -2 = 无此键 
		if(expire == NOT_VALUE_EXPIRE) {
			return;
		}
		set(redisTemplate, key, object, expire);
	}
	
	/**
	 * redis 删除值
	 * @param <K>
	 * @param redisTemplate
	 * @param key
	 */
	public static <K> void delete(RedisTemplate<K, ?> redisTemplate, K key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * redis 获取剩余存活时间 (单位: 秒)
	 * @param <K>
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public static <K> long getExpire(RedisTemplate<K, ?> redisTemplate, K key) {
		return redisTemplate.getExpire(key);
	}
	
	/**
	 * redis 设置剩余存活时间 (单位: 秒)
	 * @param <K>
	 * @param redisTemplate
	 * @param key
	 * @param timeout
	 */
	public static <K, V> void setExpire(RedisTemplate<K, V> redisTemplate, K key, long timeout) {
		// 判断是否想要设置为永久
		if(timeout == NEVER_EXPIRE) {
			long expire = getExpire(redisTemplate, key);
			if(expire == NEVER_EXPIRE) {
				// 如果其已经被设置为永久，则不作任何处理 
			} else {
				// 如果尚未被设置为永久，那么再次set一次
				set(redisTemplate, key, get(redisTemplate, key), timeout);
			}
			return;
		}
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * redis key 搜索
	 * @param <T>
	 * @param redisTemplate
	 * @param pattern
	 * @return
	 */
	public static <K> Set<K> keys(RedisTemplate<K, ?> redisTemplate, K pattern) {
		return redisTemplate.keys(pattern);
	}
	
	/**
	 * redis 数据库大小
	 * @param <K>
	 * @param redisTemplate
	 * @return
	 */
	public static <K> long dbSize(RedisTemplate<K, ?> redisTemplate) {
		return redisTemplate.getConnectionFactory().getConnection().dbSize();
	}
}
