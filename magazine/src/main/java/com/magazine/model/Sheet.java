package com.magazine.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 页签数据列表
 *
 * @author Ziegler
 * date 2021/4/12
 */
public class Sheet {

    /** 页签名称 */
    private String sheetName;

    private String searchField;

    /** 显示的字段列表 */
    private List<ShowField> fields = new ArrayList<>();

    /** keyValue格式数据 */
    private List<Map<String, Object>> rows;

    /**
     * 创建应用过滤器过滤后的 {@link Sheet}
     * @param sheetName 页签名称
     * @param SearchField
     * @param sheetFilter 过滤器
     * @return 返回经过过滤后的数据页
     */
    public static Sheet createSheet(String sheetName, String SearchField, List<Map<String, Object>> rows, SheetFilter sheetFilter) {
        final Sheet sheet = new Sheet();
        sheet.setSheetName(sheetName);
        sheet.setSearchField(SearchField);
        sheet.fields.addAll(sheetFilter.getFields());
        // 按照order排序
        sheet.fields.sort(Comparator.comparingInt(ShowField::getOrder));
        final List<Map<String, Object>> filterRows = rows.stream()
                .map(keyValues -> sheetFilter.applyFilter(keyValues))
                .collect(Collectors.toList());
        sheet.setRows(filterRows);
        return sheet;
    }

    public Sheet() {}

    public Sheet(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<ShowField> getFields() {
        return fields;
    }

    public void setFields(List<ShowField> fields) {
        this.fields = fields;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}
