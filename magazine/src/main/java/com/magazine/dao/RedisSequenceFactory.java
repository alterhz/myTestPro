package com.magazine.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RedisSequenceFactory {
    private static final Logger log = LoggerFactory.getLogger(RedisSequenceFactory.class);
    @Autowired
    private RedisTemplate redisTemplate;

    public RedisSequenceFactory() {
    }

    public void set(String key, long value) {
        RedisAtomicLong counter = new RedisAtomicLong(key, this.redisTemplate.getConnectionFactory());
        counter.set(value);
    }

    public long generate(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, this.redisTemplate.getConnectionFactory());
        return counter.addAndGet((long)increment);
    }

}

