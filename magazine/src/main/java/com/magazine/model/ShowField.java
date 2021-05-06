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
    /** 检索键 */
    private Boolean searchKey;
    /** 默认检索键 */
    private Boolean defaultKey;
    /** 默认排序字段 */
    private Boolean sortKey;
    /** 排序类型：0=默认排序;1=数值排序;2=汉字字母表顺序; */
    private Integer sortType;

    public static ShowField of(String field, Integer order) {
        final ShowField showField = new ShowField();
        showField.field = field;
        showField.order = order;
        return showField;
    }

    public static ShowField of(String field, Integer order, Boolean searchKey, Boolean defaultKey) {
        final ShowField showField = new ShowField();
        showField.field = field;
        showField.order = order;
        showField.searchKey = searchKey;
        showField.defaultKey = defaultKey;
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

    public Boolean getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(Boolean searchKey) {
        this.searchKey = searchKey;
    }

    public Boolean getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(Boolean defaultKey) {
        this.defaultKey = defaultKey;
    }

    public Boolean getSortKey() {
        return sortKey;
    }

    public void setSortKey(Boolean sortKey) {
        this.sortKey = sortKey;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("field", field)
                .append("order", order)
                .append("searchKey", searchKey)
                .append("defaultKey", defaultKey)
                .append("sortKey", sortKey)
                .append("sortType", sortType)
                .toString();
    }
}
