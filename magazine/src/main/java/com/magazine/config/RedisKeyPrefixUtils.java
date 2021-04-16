package com.magazine.config;

import org.apache.commons.lang3.StringUtils;

/**
 * redis主键名称前缀工具
 *
 * @author Ziegler
 * date 2021/4/16
 */
public class RedisKeyPrefixUtils {

    /** 过滤器主键key前缀 */
    public static final String FILTER = "filter:";

    /**
     * 增加过滤器前缀
     * @param name 名称
     * @return 返回增加了过滤器前缀“filter:”后的主键
     */
    public static String filter(String name) {
        return StringUtils.startsWith(name, FILTER) ? name : FILTER + name;
    }
}
