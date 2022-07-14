package com.jxrisesun.rstool.shiro.redis.serializer;

import com.jxrisesun.rstool.shiro.redis.exception.SerializationException;

public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
    
    T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException;
}
