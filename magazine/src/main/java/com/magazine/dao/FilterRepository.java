package com.magazine.dao;

import com.magazine.config.RedisKeyPrefixUtils;
import com.magazine.model.SheetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * sheet页字段数据过滤器
 *
 * @author Ziegler
 * date 2021/4/12
 */
@Repository
public class FilterRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取过滤器
     * @param filterName 过滤器名称
     * @return 过滤器对象
     */
    public SheetFilter getFilter(String filterName) {
        return (SheetFilter) redisTemplate.opsForValue().get(RedisKeyPrefixUtils.filter(filterName));
    }

    /**
     * 获取过滤器
     * @return 返回过滤器
     */
    public List<SheetFilter> getFilters() {
        List<SheetFilter> filterList = new ArrayList<>();
        final Set<String> keys = redisTemplate.keys(RedisKeyPrefixUtils.FILTER + "*");
        for (String key : keys) {
            final SheetFilter filter = getFilter(key);
            filterList.add(filter);
        }
        return filterList;
    }

    /**
     * 是否存在过滤器
     * @param filterName 过滤器名称
     * @return 存在返回 {@code true}
     */
    public boolean hasFilter(String filterName) {
        return redisTemplate.hasKey(RedisKeyPrefixUtils.filter(filterName));
    }

    /**
     * 设置过滤器
     * @param filterName 过滤器名称
     * @param sheetFilter 过滤器
     */
    public void setFilter(String filterName, SheetFilter sheetFilter) {
        redisTemplate.opsForValue().set(RedisKeyPrefixUtils.filter(filterName), sheetFilter);
    }

    /**
     * 删除过滤器
     * @param name 过滤器名称
     */
    public void delete(String name) {
        redisTemplate.delete(RedisKeyPrefixUtils.filter(name));
    }
}
