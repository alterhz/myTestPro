package com.magazine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 字段值
 *
 * @author Ziegler
 * date 2021/4/12
 */
public class SheetFieldValue {
    String key;
    String value;

    /**
     * 构造
     * @return 返回 {@link SheetFieldValue}
     */
    public static SheetFieldValue of(String key, String value) {
        final SheetFieldValue sheetFieldValue = new SheetFieldValue();
        sheetFieldValue.setKey(key);
        sheetFieldValue.setValue(value);
        return sheetFieldValue;
    }

    /**
     * 构造
     * @return 返回 {@link SheetFieldValue}
     */
    public static SheetFieldValue of(String key, Number value) {
        final SheetFieldValue sheetFieldValue = new SheetFieldValue();
        sheetFieldValue.setKey(key);
        sheetFieldValue.setValue(String.valueOf(value));
        return sheetFieldValue;
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
