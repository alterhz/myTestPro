package com.magazine.model;

import java.util.ArrayList;
import java.util.List;
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

    /** 显示的字段列表 */
    private List<String> fields = new ArrayList<>();

    /** 行数据 */
    private List<SheetRow> rows = new ArrayList<>();

    /**
     * 创建应用过滤器过滤后的 {@link Sheet}
     * @param sheetName 页签名称
     * @param sheetRows 要处理的行数据
     * @param sheetFilter 过滤器
     * @return 返回经过过滤后的数据页
     */
    public static Sheet createSheet(String sheetName, List<SheetRow> sheetRows, SheetFilter sheetFilter) {
        final Sheet sheet = new Sheet();
        sheet.setSheetName(sheetName);
        final List<SheetRow> filterRows = sheetRows.stream().map(sheetFilter::applySheetRow).collect(Collectors.toList());
        sheet.setRows(filterRows);
//        sheetRows.forEach(sheetRow -> sheet.addRow(sheetFilter.applySheetRow(sheetRow)));
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

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<SheetRow> getRows() {
        return rows;
    }

    public void setRows(List<SheetRow> rows) {
        this.rows = rows;
    }

    public void addRow(SheetRow sheetRow) {
        rows.add(sheetRow);
    }

}
