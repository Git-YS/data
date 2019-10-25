package com.hrms.data.bean;

import javafx.scene.control.Cell;
import org.apache.poi.ss.util.CellAddress;

/**
 * 统一excel错误
 *
 * @author ys
 */
public class ExcelError {
    private CellAddress address;
    private String error;

    public ExcelError() {
    }

    public static ExcelError create(CellAddress address, String error) {
        ExcelError excelError = new ExcelError();
        excelError.setAddress(address);
        excelError.setError(error);
        return excelError;
    }

    public static ExcelError create(int row, int cols, String error) {
        ExcelError excelError = new ExcelError();
        excelError.setAddress(new CellAddress(row, cols));
        excelError.setError(error);
        return excelError;
    }

    public CellAddress getAddress() {
        return address;
    }

    public void setAddress(CellAddress address) {
        this.address = address;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
