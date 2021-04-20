package com.magazine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 显示字段
 *
 * @author Ziegler
 * date 2021/4/20
 */
public class ShowField {
    private String field;
    private Integer order;

    public static ShowField of(String field, Integer order) {
        final ShowField showField = new ShowField();
        showField.field = field;
        showField.order = order;
        return showField;
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
                .append("field", field)
                .append("order", order)
                .toString();
    }
}
