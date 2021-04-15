package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ControllerUtils {

    /**
     * 根据指定的字段排序
     * @param bookList 要排序的数据
     * @param searchField 排序字段
     */
    public static void sortByField(List<Map<String, Object>> bookList, String searchField) {
        final Collator collator = Collator.getInstance(Locale.CHINA);
        Collections.sort(bookList, (o1, o2) -> collator.compare(o1.getOrDefault(searchField, ""), o2.getOrDefault(searchField, "")));
    }

    /**
     * 获取搜索字段
     * @param redisTemplate 连接对象
     * @return 搜搜字段名称
     */
    public static String getConfigSearchField(RedisTemplate redisTemplate) {
        final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        final String redisSearchField = hashOperations.get(RedisConsts.CONFIG_KEY, RedisConsts.CONFIG_KEY_SEARCH_FIELD);
        final String searchField = redisSearchField == null ? "" : redisSearchField;
        return searchField;
    }

    /**
     * 获取BOOK表的字段结构
     * @param redisTemplate redis连接对象
     * @return 字段列表
     */
    public static List<String> getBookSchemaFields(RedisTemplate redisTemplate) {
        final List<String> fields = redisTemplate.opsForList().range(RedisConsts.BOOK_SCHEMA_KEY, 0, -1);
        Collections.reverse(fields);
        return fields;
    }

}
