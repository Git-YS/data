package com.hrms.data.bean;

import com.hrms.common.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author ys
 */
public class ExcelCell {
    private int rowspan;
    private int colspan;

    private Object value;

    public ExcelCell(Cell cell, CellRangeAddress merge) {
        value = ExcelUtils.formatCell(cell);
        if (merge != null){
            this.rowspan = merge.getLastRow() - merge.getFirstRow() + 1;
            this.colspan = merge.getLastColumn() - merge.getFirstColumn() + 1;
        }
    }

    public ExcelCell() {
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
