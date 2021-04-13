package com.magazine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据过滤器
 * <p>只显示包含的数据列</p>
 *
 * @author Ziegler
 * date 2021/4/12
 */
public class SheetFilter {
    /** 过滤器名称 */
    private String filterName;
    /** 绑定的表名称 */
    private String sheetName;
    /** 过滤的列 */
    private List<String> fields = new ArrayList<>();

    public SheetFilter() {

    }

    /**
     * 处理 {@link SheetRow}，过滤列，
     * <li>1.只保留过滤器中存在的列</li>
     * <li>2.过滤器不包含过滤列，则显示所有列</li>
     * 并返回过滤后的 {@link SheetRow}
     * @param sheetRow 要处理的行数据
     * @return 处理后的行数据
     */
    public SheetRow applySheetRow(SheetRow sheetRow) {
        final SheetRow result = new SheetRow();
        result.setSheetName(sheetRow.getSheetName());
        result.setId(sheetRow.getId());
        final List<SheetFieldValue> filterColumns = sheetRow.getColumns().stream()
                .filter(sheetFieldValue -> hasField(sheetFieldValue.getKey()) || fields.size() == 0)
                .collect(Collectors.toList());
        result.setColumns(filterColumns);
        return result;
    }

    /**
     * 当前过滤器是否存在这个字段名称
     * @param fieldKey 测试的字段名称
     * @return
     */
    private boolean hasField(String fieldKey) {
        return fields.stream().anyMatch(s -> s.equals(fieldKey));
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

    public void addField(String fieldName) {
        fields.add(fieldName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("filterName", filterName)
                .append("sheetName", sheetName)
                .append("fields", fields)
                .toString();
    }
}
