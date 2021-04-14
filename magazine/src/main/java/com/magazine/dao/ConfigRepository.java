package com.magazine.dao;

import com.magazine.constant.RedisConsts;
import com.magazine.model.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ConfigRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ConfigRepository.class);

    public void saveConfig(String key, String value) {
        redisTemplate.opsForHash().put(RedisConsts.CONFIG_KEY, key, value);
    }

    public String getConfig(String key) {
        try {
            if (redisTemplate.opsForHash().hasKey(RedisConsts.CONFIG_KEY, key)) {
                final String value = (String)redisTemplate.opsForHash().get(RedisConsts.CONFIG_KEY, key);
                return value;
            }
        } catch (Exception e) {
            logger.error("从redis获取config数据失败。key = config, e = {}", e.getMessage());
        }
        return "";
    }

    public Set<String> getConfigKeys() {
        return (Set<String>)redisTemplate.opsForHash().keys(RedisConsts.CONFIG_KEY);
    }

    public void saveConfig(Map<String, String> config) {
        config.forEach((k, v) -> redisTemplate.opsForHash().put(RedisConsts.CONFIG_KEY, k, v));
    }
}
