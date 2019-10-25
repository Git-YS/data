package com.hrms.data.service.impl;

import com.hrms.common.ExcelUtils;
import com.hrms.data.bean.ExcelCell;
import com.hrms.data.bean.ExcelQuery;
import com.hrms.data.bean.ExcelSheet;
import com.hrms.data.service.IDataService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataServiceImpl implements IDataService {


    @Override
    public List<ExcelSheet> getSheets(Workbook wb, ExcelQuery query) {
        List<ExcelSheet> excelSheets = new ArrayList<>();
        wb.sheetIterator().forEachRemaining(s -> {
            ExcelSheet sheet = new ExcelSheet();
            sheet.setName(s.getSheetName());
            Map<CellAddress, CellRangeAddress> map = new HashMap<>();
            Map<CellAddress, CellAddress> splitCells = new HashMap<>();
            Map<CellAddress, Object> values = new HashMap<>();
            int previewRowNum = query.getFirst() + ExcelSheet.PREVIEW_ROWS;
            //拆分标题栏与数据首行
            s.getMergedRegions().parallelStream()
                    .filter(m -> m.getFirstRow() < previewRowNum)
                    .forEach(m -> {
                        CellAddress first = new CellAddress(m.getFirstRow(), m.getFirstColumn());
                        if (m.containsRow(query.getFirst())) {
                            CellRangeAddress data = new CellRangeAddress(query.getFirst(), m.getLastRow(), m.getFirstColumn(), m.getLastColumn());
                            CellAddress splitAddress = new CellAddress(query.getFirst(), m.getFirstColumn());
                            map.put(splitAddress, data);
                            map.put(first, new CellRangeAddress(m.getFirstRow(), query.getFirst() - 1, m.getFirstColumn(), m.getLastColumn()));
                            splitCells.put(first, splitAddress);
                        } else {
                            map.put(first, m);
                        }
                    });
            List<CellRangeAddress> collect = new ArrayList<>(map.values());
            List<List<Object>> header = new ArrayList<>(query.getHeader() + 1);
            List<List<Object>> previewRows = new ArrayList<>(ExcelSheet.PREVIEW_ROWS);
            Iterator<Row> rowIterator = s.rowIterator();
            while (rowIterator.hasNext()) {
                Row next = rowIterator.next();
                int rowNum = next.getRowNum();
                List<Object> list = StreamSupport.stream(next.spliterator(), true)
                        .filter(cell -> {
                            CellAddress address = cell.getAddress();
                            return !StringUtils.isEmpty(cell.toString()) || map.containsKey(address) || splitCells.containsValue(address);
                        })
                        .peek(cell -> {
                            CellAddress address = cell.getAddress();
                            if (splitCells.containsKey(cell.getAddress())) {
                                values.put(splitCells.get(address), ExcelUtils.formatCell(cell));
                            } else if (splitCells.containsValue(cell.getAddress())) {
                                cell.setCellValue(values.get(address).toString());
                            }
                        })
                        .map(cell -> new ExcelCell(cell, map.get(cell.getAddress())))
                        .collect(Collectors.toList());
                if (rowNum <= query.getHeader()) {
                    header.add(list);
                } else if (rowNum < previewRowNum) {
                    previewRows.add(list);
                }
            }
            sheet.setHeader(header);
            sheet.setPreviewRows(previewRows);
            sheet.setMergedRegions(collect);
            sheet.setId(s.hashCode());
            excelSheets.add(sheet);
        });
        return excelSheets;
    }

    /**
     * 获取复杂表头
     *
     * @param headRows 标题行数
     * @param cruSheet 当前sheet
     * @return 行号：标题名
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
                    Object o = ExcelUtils.formatCell(cruSheet.getRow(row).getCell(firstColumn));
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


}
