package com.jxrisesun.rstool.shiro.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxrisesun.rstool.shiro.redis.exception.SerializationException;
import com.jxrisesun.rstool.shiro.redis.serializer.ObjectSerializer;
import com.jxrisesun.rstool.shiro.redis.serializer.RedisSerializer;
import com.jxrisesun.rstool.shiro.redis.serializer.StringSerializer;

import java.io.Serializable;
import java.util.*;

public class RedisSessionDAO extends AbstractSessionDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionDAO.class);

	/** 默认shiro会话前缀 */
	private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";

	/** 默认shiro会话内存超时时间 */
	private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
	
	private static final boolean DEFAULT_SESSION_IN_MEMORY_ENABLED = true;
	
	// expire time in seconds
	private static final int DEFAULT_EXPIRE = -2;
	
	private static final int NO_EXPIRE = -1;
	
	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	
	/** shiro redis 会话前缀 */
	private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;
	
	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	/** 是否启用shiro会话内存缓存 */
	private boolean sessionInMemoryEnabled = DEFAULT_SESSION_IN_MEMORY_ENABLED;
	
	public boolean getSessionInMemoryEnabled() {
		return this.sessionInMemoryEnabled;
	}

	public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
		this.sessionInMemoryEnabled = sessionInMemoryEnabled;
	}
	
	/**
	 * doReadSession be called about 10 times when login.
	 * Save Session in ThreadLocal to resolve this problem. sessionInMemoryTimeout is expiration of Session in ThreadLocal.
	 * The default value is 1000 milliseconds (1s).
	 * Most of time, you don't need to change it.
	 */
	private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;

	public long getSessionInMemoryTimeout() {
		return this.sessionInMemoryTimeout;
	}

	public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
		this.sessionInMemoryTimeout = sessionInMemoryTimeout;
	}

	/** 请保证有效期时间大于会话超时时间 */
	private int expire = DEFAULT_EXPIRE;
	
	public int getExpire() {
		return this.expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	/** redis 管理器 */
	private IRedisManager redisManager;
	
	public IRedisManager getRedisManager() {
		return this.redisManager;
	}

	public RedisSessionDAO setRedisManager(IRedisManager redisManager) {
		this.redisManager = redisManager;
		return this;
	}
	
	/**
	 * 会话类型
	 */
	private Class<?> sessionType = SimpleSession.class;
	
	public Class<?> getSessionType() {
		return this.sessionType;
	}
	
	public void setSessionType(Class<?> sessionType) {
		this.sessionType = sessionType;
	}
	
	/** key 序列化对象 */
	@SuppressWarnings("rawtypes")
	private RedisSerializer keySerializer = new StringSerializer();
	
	@SuppressWarnings("rawtypes")
	public RedisSerializer getKeySerializer() {
		return this.keySerializer;
	}

	@SuppressWarnings("rawtypes")
	public void setKeySerializer(RedisSerializer keySerializer) {
		this.keySerializer = keySerializer;
	}
	
	/** value序列化对象 */
	@SuppressWarnings("rawtypes")
	private RedisSerializer valueSerializer = new ObjectSerializer();
	
	@SuppressWarnings("rawtypes")
	public RedisSerializer getValueSerializer() {
		return this.valueSerializer;
	}

	@SuppressWarnings("rawtypes")
	public void setValueSerializer(RedisSerializer valueSerializer) {
		this.valueSerializer = valueSerializer;
	}
	
	/** 线程变量存储 */
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal sessionsInThread = new ThreadLocal();
	
	@Override
	protected void assignSessionId(Session session, Serializable sessionId) {
		if(session instanceof SimpleSession) {
			((SimpleSession) session).setId(sessionId);
		} else if(session instanceof RedisSession) {
			((RedisSession) session).setId(sessionId);
		}
	}
	
	/**
	 * 创建会话
	 */
	@Override
	protected Serializable doCreate(Session session) {
		if (session == null) {
			LOGGER.error("session is null");
			throw new UnknownSessionException("session is null");
		}
		Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
		return sessionId;
	}
	
	/**
	 * 更新会话
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
		if (this.sessionInMemoryEnabled) {
			this.setSessionToThreadLocal(session.getId(), session);
		}
	}
	
	/**
	 * 保存会话
	 * @param session
	 * @throws UnknownSessionException
	 */
	@SuppressWarnings("unchecked")
	protected void saveSession(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			LOGGER.error("session or session id is null");
			throw new UnknownSessionException("session or session id is null");
		}
		byte[] key;
		byte[] value;
		try {
			key = keySerializer.serialize(getRedisSessionKey(session.getId()));
			value = valueSerializer.serialize(session);
		} catch (SerializationException e) {
			LOGGER.error("serialize session error. session id=" + session.getId());
			throw new UnknownSessionException(e);
		}
		if (expire == DEFAULT_EXPIRE) {
			this.redisManager.set(key, value, (int) (session.getTimeout() / MILLISECONDS_IN_A_SECOND));
			return;
		}
		if (expire != NO_EXPIRE && expire * MILLISECONDS_IN_A_SECOND < session.getTimeout()) {
			LOGGER.warn("Redis session expire time: "
					+ (expire * MILLISECONDS_IN_A_SECOND)
					+ " is less than Session timeout: "
					+ session.getTimeout()
					+ " . It may cause some problems.");
		}
		this.redisManager.set(key, value, expire);
	}
	

	/**
	 * 读取会话
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			LOGGER.warn("session id is null");
			return null;
		}
			if (this.sessionInMemoryEnabled) {
			Session session = getSessionFromThreadLocal(sessionId);
			if (session != null) {
				return session;
			}
		}

		Session session = null;
		LOGGER.debug("read session from redis");
		try {
			byte[] value = redisManager.get(keySerializer.serialize(getRedisSessionKey(sessionId)));
			session = (Session) valueSerializer.deserialize(value, this.sessionType);
			if (this.sessionInMemoryEnabled) {
				setSessionToThreadLocal(sessionId, session);
			}
		} catch (SerializationException e) {
			LOGGER.error("read session error. settionId=" + sessionId);
		}
		return session;
	}
	
	/**
	 * 删除会话
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			LOGGER.error("session or session id is null");
			return;
		}
		try {
			redisManager.del(keySerializer.serialize(getRedisSessionKey(session.getId())));
		} catch (SerializationException e) {
			LOGGER.error("delete session error. session id=" + session.getId());
		}
	}

	/**
	 * 获取有效会话
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new LinkedHashSet<>();
		try {
			Set<byte[]> keys = redisManager.keys(this.keySerializer.serialize(this.keyPrefix + "*"));
			if (keys != null && keys.size() > 0) {
				for (byte[] key:keys) {
					Session s = (Session) valueSerializer.deserialize(redisManager.get(key));
					sessions.add(s);
				}
			}
		} catch (SerializationException e) {
			LOGGER.error("get active sessions error.");
		}
		return sessions;
	}

	/**
	 * 设置会话线程变量存储
	 * @param sessionId
	 * @param s
	 */
	@SuppressWarnings("unchecked")
	protected void setSessionToThreadLocal(Serializable sessionId, Session s) {
		Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
		if (sessionMap == null) {
            sessionMap = new HashMap<>();
            sessionsInThread.set(sessionMap);
        }
		SessionInMemory sessionInMemory = new SessionInMemory();
		sessionInMemory.setCreateTime(new Date());
		sessionInMemory.setSession(s);
		sessionMap.put(sessionId, sessionInMemory);
	}

	/**
	 * 从线程变量获取会话
	 * @param sessionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Session getSessionFromThreadLocal(Serializable sessionId) {
		Session s = null;

		if (sessionsInThread.get() == null) {
			return null;
		}

		Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
		SessionInMemory sessionInMemory = sessionMap.get(sessionId);
		if (sessionInMemory == null) {
			return null;
		}
		Date now = new Date();
		long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
		if (duration < sessionInMemoryTimeout) {
			s = sessionInMemory.getSession();
			LOGGER.debug("read session from memory");
		} else {
			sessionMap.remove(sessionId);
		}

		return s;
	}

	/**
	 * 获取 redis会话key
	 * @param sessionId
	 * @return
	 */
	protected String getRedisSessionKey(Serializable sessionId) {
		return this.getKeyPrefix() + sessionId;
	}
}
