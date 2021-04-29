package com.magazine.model;

import java.util.*;
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

    /** 默认搜索字段 */
    private String searchField;
    /** 排序字段 */
    private String sortField;

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
        // 如果存在默认搜索键，则使用默认搜索。否则使用全局默认搜索键
        sheet.fields.stream()
                .filter(f -> f.getDefaultKey() != null && f.getDefaultKey())
                .map(showField -> showField.getField())
                .findAny()
                .ifPresent(sheet::setSearchField);

        // 默认全局搜索键为排序字段
        sheet.setSortField(SearchField);
        sheet.fields.stream()
                .filter(f -> f.getSortKey() != null && f.getSortKey())
                .map(showField -> showField.getField())
                .findAny()
                .ifPresent(sheet::setSortField);

        // 按照order排序
        sheet.fields.sort(Comparator.comparingInt(ShowField::getOrder));
        final List<Map<String, Object>> filterRows = rows.stream()
                .map(keyValues -> sheetFilter.applyFilter(keyValues))
                .collect(Collectors.toList());
        sheet.setRows(filterRows);
        return sheet;
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

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
