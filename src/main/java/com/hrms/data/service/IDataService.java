package com.hrms.data.service;

import com.hrms.data.bean.ExcelQuery;
import com.hrms.data.bean.ExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author ys
 */
public interface IDataService {

    List<ExcelSheet> getSheets(Workbook wb, ExcelQuery query);
}
