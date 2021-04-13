package com.magazine.dao;

import com.magazine.model.SheetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * sheet页字段数据过滤器
 *
 * @author Ziegler
 * date 2021/4/12
 */
@Repository
public class SheetFilterRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取过滤器
     * @param filterName 过滤器名称
     * @return 过滤器对象
     */
    public SheetFilter getFilter(String filterName) {
        return (SheetFilter) redisTemplate.opsForValue().get(filterName);
    }

    /**
     * 是否存在过滤器
     * @param filterName 过滤器名称
     * @return 存在返回 {@code true}
     */
    public boolean hasFilter(String filterName) {
        return redisTemplate.hasKey(filterName);
    }

    /**
     * 设置过滤器
     * @param filterName 过滤器名称
     * @param sheetFilter 过滤器
     */
    public void setFilter(String filterName, SheetFilter sheetFilter) {
        redisTemplate.opsForValue().set(filterName, sheetFilter);
    }

}
