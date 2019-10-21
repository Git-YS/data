package com.hrms.data.service.impl;

import com.hrms.data.bean.ExcelQuery;
import com.hrms.data.bean.ExcelSheet;
import com.hrms.data.service.IDataService;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataServiceImpl implements IDataService {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_FORMATE_FILE = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    @Override
    public List<ExcelSheet> getSheets(Workbook wb, ExcelQuery query) {
        List<ExcelSheet> excelSheets = new ArrayList<>();
        wb.sheetIterator().forEachRemaining(s -> {
            ExcelSheet sheet = new ExcelSheet();
            sheet.setName(s.getSheetName());
            int previewRowNum = query.getFirst() + ExcelSheet.PREVIEW_ROWS ;
            List<CellRangeAddress> collect = s.getMergedRegions().parallelStream().filter(m -> m.getFirstRow() < previewRowNum)
                    .collect(Collectors.toList());
            List<Row> header = new ArrayList<>(query.getHeader()+1);
            List<Row> previewRows = new ArrayList<>(ExcelSheet.PREVIEW_ROWS);
            Iterator<Row> rowIterator = s.rowIterator();
            while (rowIterator.hasNext()) {
                Row next = rowIterator.next();
                int rowNum = next.getRowNum();
                if (rowNum <= query.getHeader()){
                    header.add(next);
                }else if (rowNum<  previewRowNum) {
                    previewRows.add(next);
                }
            }
            sheet.setHeader(header);
            sheet.setPreviewRows(previewRows);
            excelSheets.add(sheet);
        });
        return excelSheets;
    }

    /**
     * 获取复杂表头
     *
     * @param headRows
     * @param cruSheet
     * @return
     */
    private Map<Integer, String> getHeader(int headRows, Sheet cruSheet) {
        Map<Integer, String> thNames = new HashMap<>(16);
        boolean hasMarge = cruSheet.getNumMergedRegions() > 0;
        Map<String, Object> rowMargeMap = new HashMap<>(16);
        if (hasMarge) {
            cruSheet.getMergedRegions().parallelStream().forEach(cellRangeAddress -> {
                int row = cellRangeAddress.getFirstRow();
                int firstColumn = cellRangeAddress.getFirstColumn();
                int lastColumn = cellRangeAddress.getLastColumn();
                //不止一列合并时，缓存首行数据
                if (firstColumn != lastColumn) {
                    Object o = formatCell(cruSheet.getRow(row).getCell(firstColumn));
                    for (int j = firstColumn; j <= lastColumn; j++) {
                        //头部结尾行
                        Object result = o;
                        if (cellRangeAddress.containsRow(headRows - 1)) {
                            result = String.format("%s$%d", o, j - firstColumn);
                        }
                        rowMargeMap.put(new CellAddress(row, j).toString(), result);
                    }
                }
            });
        }

        for (int j = headRows - 1; j >= 0; j--) {

//            StreamSupport.stream(cruSheet.getRow(j).spliterator(), true).forEach(name -> {
            cruSheet.getRow(j).forEach(name -> {
                String s;
                String address = name.getAddress().toString();
                if (hasMarge && rowMargeMap.containsKey(address)) {
                    s = rowMargeMap.get(address).toString();
                } else {
                    s = name.toString();
                }
                //标题不为空
                if (!StringUtils.isEmpty(s)) {
                    int columnIndex = name.getColumnIndex();

                    if (columnIndex >= thNames.size() ||
                            StringUtils.isEmpty(thNames.get(columnIndex))) {
                        thNames.put(columnIndex, s);
                    } else /*if (!s.equals(thNames.get(columnIndex).split("-")[0]))*/ {
                        //去除合并单元格，多次录入数据问题
                        thNames.put(columnIndex, s + '-' + thNames.get(columnIndex));
                    }
                }
            });
        }
        return thNames;
    }

    private static Object formatCell(Cell cell) {

        /*
		0, "General"
		1, "0"
		2, "0.00"
		3, "#,##0"
		4, "#,##0.00"
		5, "$#,##0_);($#,##0)"
		6, "$#,##0_);[Red]($#,##0)"
		7, "$#,##0.00);($#,##0.00)"
		8, "$#,##0.00_);[Red]($#,##0.00)"
		9, "0%"
		0xa, "0.00%"
		0xb, "0.00E+00"
		0xc, "# ?/?"
		0xd, "# ??/??"
		0xe, "m/d/yy"
		0xf, "d-mmm-yy"
		0x10, "d-mmm"
		0x11, "mmm-yy"
		0x12, "h:mm AM/PM"
		0x13, "h:mm:ss AM/PM"
		0x14, "h:mm"
		0x15, "h:mm:ss"
		0x16, "m/d/yy h:mm"
		// 0x17 - 0x24 reserved for international and undocumented 0x25, "#,##0_);(#,##0)"
		0x26, "#,##0_);[Red](#,##0)"
		0x27, "#,##0.00_);(#,##0.00)"
		0x28, "#,##0.00_);[Red](#,##0.00)"
		0x29, "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)"
		0x2a, "_($* #,##0_);_($* (#,##0);_($* \"-\"_);_(@_)"
		0x2b, "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"
		0x2c, "_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)"
		0x2d, "mm:ss"
		0x2e, "[h]:mm:ss"
		0x2f, "mm:ss.0"
		0x30, "##0.0E+0"
		0x31, "@" - This is text format.
		0x31 "text" - Alias for "@"
		*/
        try {
            CellType cellType = cell.getCellTypeEnum();
            switch (cellType) {
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case NUMERIC:
                    final double numCellVal = cell.getNumericCellValue();
                    final short dataFmt = cell.getCellStyle().getDataFormat();
                    // 日期
                    if ((dataFmt >= 14 && dataFmt <= 17) || dataFmt == 31 || dataFmt == 57 || dataFmt == 58) {
                        Date cellDate = DateUtil.getJavaDate(numCellVal);
                        if (cellDate != null) {
                            return new SimpleDateFormat("yyyy-MM-dd").format(cellDate);
                        }
                    }
                    // 时间
                    else if ((dataFmt >= 18 && dataFmt <= 21) || dataFmt == 32 || dataFmt == 33) {
                        Date cellDate = DateUtil.getJavaDate(numCellVal);
                        if (cellDate != null) {
                            return new SimpleDateFormat("HH:mm:ss").format(cellDate);
                        }
                    }
                    // 日期时间
                    else if (dataFmt == 22) {
                        Date cellDate = DateUtil.getJavaDate(numCellVal);
                        if (cellDate != null) {
                            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cellDate);
                        }
                    }
                    // 百分数
                    else if (dataFmt == 9 || dataFmt == 10) {
                        return new DecimalFormat(cell.getCellStyle().getDataFormatString()).format(numCellVal);
                    }
                    // 整数、小数、科学计数法
                    else if (dataFmt >= 0 && dataFmt <= 4) {
                        return new DecimalFormat("#.##").format(numCellVal);
                    } else {
                        return String.valueOf(numCellVal);
                    }
//                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                        return DATE_TIME_FORMAT.format(DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant());
//                    } else {
//                        long t = Math.round(cell.getNumericCellValue());
//                        double tmp = cell.getNumericCellValue();
//                        if (t == tmp) {
//                            return t;
//                        }
//                        return tmp;
//                    }
                case BLANK:
                    return "";
                case FORMULA:
                    try {
                        switch (cell.getCachedFormulaResultTypeEnum()) {
                            case STRING:
                                return cell.getStringCellValue();
                            case BOOLEAN:
                                return cell.getBooleanCellValue();
                            case NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    return DATE_TIME_FORMAT.format(DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant());
                                } else {
                                    long t = Math.round(cell.getNumericCellValue());
                                    double tmp = cell.getNumericCellValue();
//                                    if (t == tmp) {
//                                        return t;
//                                    }
                                    return BigDecimal.valueOf(tmp).longValue() == t ? t : tmp;
                                }
                            case BLANK:
                                return "";
                            default:
                                return cell.getStringCellValue();
                        }
                    } catch (Exception e) {
                        byte errorCellValue = cell.getErrorCellValue();
                        return FormulaError.forInt(errorCellValue).getString();
                    }
                default:
                    return cell.getStringCellValue();
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("第%s行解析异常", Optional.ofNullable(cell).map(cell1 -> cell1.getAddress().toString()).orElse("未知")), e);
        }
    }
}
