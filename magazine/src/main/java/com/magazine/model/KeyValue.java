package com.magazine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 字段值
 *
 * @author Ziegler
 * date 2021/4/12
 */
public class KeyValue {
    String key;
    String value;

    /**
     * 构造
     * @return 返回 {@link KeyValue}
     */
    public static KeyValue of(String key, String value) {
        final KeyValue keyValue = new KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(value);
        return keyValue;
    }

    /**
     * 构造
     * @return 返回 {@link KeyValue}
     */
    public static KeyValue of(String key, Number value) {
        final KeyValue keyValue = new KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(String.valueOf(value));
        return keyValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("key", key)
                .append("value", value)
                .toString();
    }
}
