package com.hrms.data.bean;

import org.apache.poi.ss.util.CellRangeAddress;

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
     * sheet hash值
     */
    private int id;
    /**
     * sheet名
     */
    private String name;
    /**
     * 标题
     */
    private List<List<Object>> header;
    /**
     * 前三行数据
     */
    private List<List<Object>> previewRows;
    /**
     * 合并单元格
     */
    private List<CellRangeAddress> mergedRegions;
    /**
     * 字段名
     */
    private List<List<Object>> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Object>> getHeader() {
        return header;
    }

    public void setHeader(List<List<Object>> header) {
        this.header = header;
    }

    public List<List<Object>> getPreviewRows() {
        return previewRows;
    }

    public void setPreviewRows(List<List<Object>> previewRows) {
        this.previewRows = previewRows;
    }

    public List<List<Object>> getParams() {
        return params;
    }

    public void setParams(List<List<Object>> params) {
        this.params = params;
    }

    public List<CellRangeAddress> getMergedRegions() {
        return mergedRegions;
    }

    public void setMergedRegions(List<CellRangeAddress> mergedRegions) {
        this.mergedRegions = mergedRegions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
