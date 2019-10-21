package com.hrms.data.bean;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * @author ys
 */
public class ExcelSheet {
    /**
     * 数据预览行数
     */
    public static final int PREVIEW_ROWS = 3;
    /**
     * sheet名
     */
    private String name;
    /**
     * 标题
     */
    private List<Row> header;
    /**
     * 前三行数据
     */
    private List<Row> previewRows;
    /**
     * 字段名
     */
    private List<Row> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Row> getHeader() {
        return header;
    }

    public void setHeader(List<Row> header) {
        this.header = header;
    }

    public List<Row> getPreviewRows() {
        return previewRows;
    }

    public void setPreviewRows(List<Row> previewRows) {
        this.previewRows = previewRows;
    }

    public List<Row> getParams() {
        return params;
    }

    public void setParams(List<Row> params) {
        this.params = params;
    }
}
