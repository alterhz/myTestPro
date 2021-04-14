package com.magazine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 表格结构
 *
 * @author Ziegler
 * date 2021/4/14
 */
public class SchemaField {
    private String name;
    private String field;
    private Integer order;

    /**
     * 构造一个 {@link SchemaField}实例
     */
    public static SchemaField of(String name, String field, Integer order) {
        final SchemaField schemaField = new SchemaField();
        schemaField.setName(name);
        schemaField.setField(field);
        schemaField.setOrder(order);
        return schemaField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("field", field)
                .append("order", order)
                .toString();
    }
}
