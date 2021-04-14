package com.magazine.dao;

import com.magazine.constant.RedisConsts;
import com.magazine.model.KeyValue;
import com.magazine.model.SheetRow;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 页签数据
 *
 * @author Ziegler
 * date 2021/4/13
 */
@Repository
public class SheetRepository {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SheetRepository.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取 {@link SheetRow}
     * @param sheetRowKey 主键
     * @return 行数据
     */
    public Map<String, Object> getRow(String sheetRowKey) {
        try {
            return (Map<String, Object>) redisTemplate.opsForHash().entries(sheetRowKey);
        } catch (Exception e) {
            logger.error("1. json自动转换为SheetRow对象失败。key = {}, e = {}", sheetRowKey, e.getMessage());
        }

        // 如果是旧的数据类型，则转换一下
        try {
            final Map<String, Object> oldBook = (Map<String, Object>) redisTemplate.opsForValue().get(sheetRowKey);
            return oldBook;
        } catch (Exception e) {
            logger.error("2. json再次转换为Map<String, String>对象失败。key = {}, e = {}", sheetRowKey, e.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * 获取页签数据，传入页签名称。
     * <p>例如获取“book:*”的所有数据行</p>
     * @param sheetName 页签名称
     * @return
     */
    public List<Map<String, Object>> getRows(String sheetName) {
        List<Map<String, Object>> sheet = new ArrayList<>();
        Set keys = redisTemplate.keys(sheetName + ":*");
        for (Object key : keys) {
            final Map<String, Object> keyValues = getRow((String)key);
            assert keyValues != null : "获取SheetRow失败。key = " + key;
            sheet.add(keyValues);
        }
        return sheet;
    }

    /**
     * 是否存在sheet行数据
     * @param sheetKey 行数据键名称
     * @return 存在返回 {@code true}
     */
    public boolean hasRow(String sheetKey) {
        return redisTemplate.hasKey(sheetKey);
    }

    /**
     * 添加 {@link SheetRow}
     * @param sheetRow 行数据
     */
    public void setRow(String sheetKey, Map<String, Object> keyValues) {
        if (redisTemplate.type(sheetKey) != DataType.HASH) {
            redisTemplate.delete(sheetKey);
        }
        redisTemplate.opsForHash().putAll(sheetKey, keyValues);
    }



}
