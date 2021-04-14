package com.magazine.model;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 行数据
 *
 * @author Ziegler
 * date 2021/4/12
 */
public class SheetRow {

    private String sheetName;

    private Long id;

    private List<KeyValue> columns = new ArrayList<>();

    public SheetRow() {}

    public static SheetRow create(String sheetName, Long id) {
        final SheetRow sheetRow = new SheetRow();
        sheetRow.sheetName = sheetName;
        sheetRow.setId(id);
        return sheetRow;
    }

    public void addFieldValue(KeyValue keyValue) {
        columns.add(keyValue);
    }

    public String sheetKey() {
        return sheetName + ":" + id;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<KeyValue> getColumns() {
        return columns;
    }

    public void setColumns(List<KeyValue> values) {
        this.columns = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sheetKey", sheetKey())
                .append("sheetName", sheetName)
                .append("id", id)
                .append("columns", columns)
                .toString();
    }
}
